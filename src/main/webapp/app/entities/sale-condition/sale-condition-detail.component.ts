import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SaleCondition } from './sale-condition.model';
import { SaleConditionService } from './sale-condition.service';

@Component({
    selector: 'jhi-sale-condition-detail',
    templateUrl: './sale-condition-detail.component.html'
})
export class SaleConditionDetailComponent implements OnInit, OnDestroy {

    saleCondition: SaleCondition;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private saleConditionService: SaleConditionService,
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
        this.saleConditionService.find(id)
            .subscribe((saleConditionResponse: HttpResponse<SaleCondition>) => {
                this.saleCondition = saleConditionResponse.body;
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
            'saleConditionListModification',
            (response) => this.load(this.saleCondition.id)
        );
    }
}
