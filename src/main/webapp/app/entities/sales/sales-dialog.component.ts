import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Sales } from './sales.model';
import { SalesPopupService } from './sales-popup.service';
import { SalesService } from './sales.service';
import { Client, ClientService } from '../client';
import { Contact, ContactService } from '../contact';
import { Location, LocationService } from '../location';
import { Product, ProductService } from '../product';

import { DatePipe } from '@angular/common';

@Component({
    selector: 'jhi-sales-dialog',
    templateUrl: './sales-dialog.component.html'
})
export class SalesDialogComponent implements OnInit {

    sales: Sales;
    isSaving: boolean;

    clients: Client[];

    contacts: Contact[];

    locations: Location[];

    products: Product[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private salesService: SalesService,
        private clientService: ClientService,
        private contactService: ContactService,
        private locationService: LocationService,
        private productService: ProductService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;

        if (this.sales.id !== undefined) {
            const pipe = new DatePipe('es-CL');
            const now = Date.now();
            const myFormattedDate = pipe.transform(this.sales.createDate, 'yyyy-MM-dd');
            this.sales.createDate = myFormattedDate;
        } else {
            const pipe = new DatePipe('es-CL');
            const now = Date.now();
            const myFormattedDate = pipe.transform(now, 'yyyy-MM-dd');
            this.sales.createDate = myFormattedDate;
        }

        this.clientService.query()
            .subscribe((res: HttpResponse<Client[]>) => { this.clients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.contactService.query()
            .subscribe((res: HttpResponse<Contact[]>) => { this.contacts = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.locationService.query()
            .subscribe((res: HttpResponse<Location[]>) => { this.locations = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.productService.query()
            .subscribe((res: HttpResponse<Product[]>) => { this.products = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.sales.id !== undefined) {
            this.sales.createDate += ' 00:00:00';
            this.subscribeToSaveResponse(
                this.salesService.update(this.sales));
        } else {
            this.sales.active = 1;

            const pipe = new DatePipe('es-CL');
            const now = Date.now();
            const myFormattedDate = pipe.transform(now, 'yyyy-MM-dd 00:00:00');
            this.sales.createDate = myFormattedDate;

            this.subscribeToSaveResponse(
                this.salesService.create(this.sales));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Sales>>) {
        result.subscribe((res: HttpResponse<Sales>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Sales) {
        this.eventManager.broadcast({ name: 'salesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
    }

    trackContactById(index: number, item: Contact) {
        return item.id;
    }

    trackLocationById(index: number, item: Location) {
        return item.id;
    }

    trackProductById(index: number, item: Product) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sales-popup',
    template: ''
})
export class SalesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesPopupService: SalesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.salesPopupService
                    .open(SalesDialogComponent as Component, params['id']);
            } else {
                this.salesPopupService
                    .open(SalesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
