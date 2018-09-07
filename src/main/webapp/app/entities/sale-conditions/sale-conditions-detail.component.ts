import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SaleConditions } from './sale-conditions.model';
import { SaleConditionsService } from './sale-conditions.service';

@Component({
    selector: 'jhi-sale-conditions-detail',
    templateUrl: './sale-conditions-detail.component.html'
})
export class SaleConditionsDetailComponent implements OnInit, OnDestroy {

    saleConditions: SaleConditions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private saleConditionsService: SaleConditionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSaleConditions();
    }

    load(id) {
        this.saleConditionsService.find(id)
            .subscribe((saleConditionsResponse: HttpResponse<SaleConditions>) => {
                this.saleConditions = saleConditionsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSaleConditions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'saleConditionsListModification',
            (response) => this.load(this.saleConditions.id)
        );
    }
}
