import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SaleConditions } from './sale-conditions.model';
import { SaleConditionsPopupService } from './sale-conditions-popup.service';
import { SaleConditionsService } from './sale-conditions.service';
import { Sales, SalesService } from '../sales';

@Component({
    selector: 'jhi-sale-conditions-dialog',
    templateUrl: './sale-conditions-dialog.component.html'
})
export class SaleConditionsDialogComponent implements OnInit {

    saleConditions: SaleConditions;
    isSaving: boolean;

    sales: Sales[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private saleConditionsService: SaleConditionsService,
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
        if (this.saleConditions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.saleConditionsService.update(this.saleConditions));
        } else {
            this.subscribeToSaveResponse(
                this.saleConditionsService.create(this.saleConditions));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SaleConditions>>) {
        result.subscribe((res: HttpResponse<SaleConditions>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SaleConditions) {
        this.eventManager.broadcast({ name: 'saleConditionsListModification', content: 'OK'});
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-sale-conditions-popup',
    template: ''
})
export class SaleConditionsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleConditionsPopupService: SaleConditionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.saleConditionsPopupService
                    .open(SaleConditionsDialogComponent as Component, params['id']);
            } else {
                this.saleConditionsPopupService
                    .open(SaleConditionsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
