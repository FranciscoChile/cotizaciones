import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SaleConditions } from './sale-conditions.model';
import { SaleConditionsService } from './sale-conditions.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-sale-conditions',
    templateUrl: './sale-conditions.component.html'
})
export class SaleConditionsComponent implements OnInit, OnDestroy {
saleConditions: SaleConditions[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private saleConditionsService: SaleConditionsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.saleConditionsService.query().subscribe(
            (res: HttpResponse<SaleConditions[]>) => {
                this.saleConditions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSaleConditions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SaleConditions) {
        return item.id;
    }
    registerChangeInSaleConditions() {
        this.eventSubscriber = this.eventManager.subscribe('saleConditionsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
