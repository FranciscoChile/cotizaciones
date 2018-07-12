import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Sales } from './sales.model';
import { SalesService } from './sales.service';

@Component({
    selector: 'jhi-sales-detail',
    templateUrl: './sales-detail.component.html'
})
export class SalesDetailComponent implements OnInit, OnDestroy {

    sales: Sales;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private salesService: SalesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSales();
    }

    load(id) {
        this.salesService.find(id)
            .subscribe((salesResponse: HttpResponse<Sales>) => {
                this.sales = salesResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSales() {
        this.eventSubscriber = this.eventManager.subscribe(
            'salesListModification',
            (response) => this.load(this.sales.id)
        );
    }
}
