import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Contact } from './contact.model';
import { ContactService } from './contact.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

@Component({
    selector: 'jhi-contact',
    templateUrl: './contact.component.html'
})
export class ContactComponent implements OnInit, OnDestroy {

    currentAccount: any;
    contacts: Contact[];
    contactsCombo: Contact[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    filteredNames: any[];
    name: string;

    constructor(
        private contactService: ContactService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

// manejo de buscadores autoComplete
    filterNames(event) {
        this.filteredNames = [];
        for (let i = 0; i < this.contactsCombo.length; i++) {
            const prod = this.contactsCombo[i];
            if (prod.name.toLowerCase().indexOf(event.query.toLowerCase()) !== -1) {
                this.filteredNames.push(prod.name);
            }
        }
    }

    captureValue(event: any) {
        this.name = event;
        this.loadAll();
    }

    clearValue(event: any) {
        this.name = '';
        this.loadAll();
    }

    loadAllCombo() {
      this.contactService.query({
          'active.equals': 1}).subscribe(
          (resCombo: HttpResponse<Contact[]>) => this.contactsCombo = resCombo.body,
          (resCombo: HttpErrorResponse) => this.onError(resCombo.message)
      );
    }

// ------

    loadAll() {
        this.contactService.query({
            'name.contains': this.name,
            'active.equals': 1,
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<Contact[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/contact'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/contact', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

// agregar llamada a lista de autoComplete
    ngOnInit() {
        this.name = '';
        this.loadAllCombo();
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInContacts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Contact) {
        return item.id;
    }
    registerChangeInContacts() {
        this.eventSubscriber =
        this.eventManager.subscribe('contactListModification', (response) => {
          this.loadAll(), this.loadAllCombo();
        });
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.contacts = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
