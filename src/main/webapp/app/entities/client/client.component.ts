import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Client } from './client.model';
import { ClientService } from './client.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

@Component({
    selector: 'jhi-client',
    templateUrl: './client.component.html'
})
export class ClientComponent implements OnInit, OnDestroy {

    currentAccount: any;
    clients: Client[];
    clientsCombo: Client[];
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
        private clientService: ClientService,
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
            for (let i = 0; i < this.clientsCombo.length; i++) {
                const prod = this.clientsCombo[i];
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
          this.clientService.query({
              'active.equals': 1}).subscribe(
              (resCombo: HttpResponse<Client[]>) => this.clientsCombo = resCombo.body,
              (resCombo: HttpErrorResponse) => this.onError(resCombo.message)
          );
        }

    // ------

// agregar filtro para busqueda con autoComplete
    loadAll() {
        this.clientService.query({
            'name.contains': this.name,
            'active.equals': 1,
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<Client[]>) => this.onSuccess(res.body, res.headers),
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
        this.router.navigate(['/client'], {queryParams:
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
        this.router.navigate(['/client', {
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
        this.registerChangeInClients();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Client) {
        return item.id;
    }
    registerChangeInClients() {
        this.eventSubscriber =
        this.eventManager.subscribe('clientListModification', (response) => {
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
        this.clients = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
