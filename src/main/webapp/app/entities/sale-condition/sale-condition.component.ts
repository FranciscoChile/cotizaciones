import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SaleCondition } from './sale-condition.model';
import { SaleConditionService } from './sale-condition.service';
import { Principal } from '../../shared';
import { Sales, SalesService } from '../sales';

@Component({
    selector: 'jhi-sale-condition',
    templateUrl: './sale-condition.component.html'
})
export class SaleConditionComponent implements OnInit, OnDestroy {
saleConditions: SaleCondition[];
    currentAccount: any;
    eventSubscriber: Subscription;

    salesCombo: Sales[];
    filteredSales: any[];
    idSale: string;

    constructor(
        private saleConditionService: SaleConditionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private salesService: SalesService
    ) {
    }

    filterSales(event) {
        this.filteredSales = [];
        for (let i = 0; i < this.salesCombo.length; i++) {
            const prod = this.salesCombo[i];
            if (String(prod.id).toLowerCase().indexOf(event.query.toLowerCase()) === 0) {
                this.filteredSales.push(prod.id);
            }
        }
    }

    captureValue(event: any) {
        this.idSale = event;
        this.loadAll();
    }

    clearValue(event: any) {
        this.idSale = '';
        this.saleConditions = [];
    }

    loadAllCombo() {
      this.salesService.query({
          'active.equals': 1}).subscribe(
          (resCombo: HttpResponse<Sales[]>) => this.salesCombo = resCombo.body,
          (resCombo: HttpErrorResponse) => this.onError(resCombo.message)
      );
    }

    loadAll() {
        this.saleConditionService.query({
          'salesId.equals': this.idSale
        }).subscribe(
            (res: HttpResponse<SaleCondition[]>) => {
                this.saleConditions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.idSale = '';
        // this.loadAll();
        this.loadAllCombo();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSaleConditions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SaleCondition) {
        return item.id;
    }
    registerChangeInSaleConditions() {
        this.eventSubscriber = this.eventManager.subscribe('saleConditionListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
