import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Location } from './location.model';
import { LocationService } from './location.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

@Component({
    selector: 'jhi-location',
    templateUrl: './location.component.html'
})
export class LocationComponent implements OnInit, OnDestroy {

    currentAccount: any;
    locations: Location[];
    locationsCombo: Location[];
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

    filteredDescription: any[];
    description: string;

    constructor(
        private locationService: LocationService,
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
    filterDescription(event) {
        this.filteredDescription = [];
        for (let i = 0; i < this.locationsCombo.length; i++) {
            const prod = this.locationsCombo[i];
            if (prod.description.toLowerCase().indexOf(event.query.toLowerCase()) !== -1) {
                this.filteredDescription.push(prod.description);
            }
        }
    }

    captureValue(event: any) {
        this.description = event;
        this.loadAll();
    }

    clearValue(event: any) {
        this.description = '';
        this.loadAll();
    }

    loadAllCombo() {
      this.locationService.query({
          'active.equals': 1}).subscribe(
          (resCombo: HttpResponse<Location[]>) => this.locationsCombo = resCombo.body,
          (resCombo: HttpErrorResponse) => this.onError(resCombo.message)
      );
    }

// ------

    loadAll() {
        this.locationService.query({
            'description.contains': this.description,
            'active.equals': 1,
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<Location[]>) => this.onSuccess(res.body, res.headers),
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
        this.router.navigate(['/location'], {queryParams:
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
        this.router.navigate(['/location', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.description = '';
        this.loadAllCombo();
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInLocations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Location) {
        return item.id;
    }
    registerChangeInLocations() {
        this.eventSubscriber =
        this.eventManager.subscribe('locationListModification',
        (response) => {
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
        this.locations = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
