import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SaleCondition } from './sale-condition.model';
import { SaleConditionPopupService } from './sale-condition-popup.service';
import { SaleConditionService } from './sale-condition.service';
import { Sales, SalesService } from '../sales';

@Component({
    selector: 'jhi-sale-condition-dialog',
    templateUrl: './sale-condition-dialog.component.html'
})
export class SaleConditionDialogComponent implements OnInit {

    saleCondition: SaleCondition;
    isSaving: boolean;

    sales: Sales[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private saleConditionService: SaleConditionService,
        private salesService: SalesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.salesService.query()
            .subscribe((res: HttpResponse<Sales[]>) => { this.sales = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.saleCondition.id !== undefined) {
            this.subscribeToSaveResponse(
                this.saleConditionService.update(this.saleCondition));
        } else {
            this.subscribeToSaveResponse(
                this.saleConditionService.create(this.saleCondition));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SaleCondition>>) {
        result.subscribe((res: HttpResponse<SaleCondition>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SaleCondition) {
        this.eventManager.broadcast({ name: 'saleConditionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSalesById(index: number, item: Sales) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sale-condition-popup',
    template: ''
})
export class SaleConditionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleConditionPopupService: SaleConditionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.saleConditionPopupService
                    .open(SaleConditionDialogComponent as Component, params['id']);
            } else {
                this.saleConditionPopupService
                    .open(SaleConditionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
