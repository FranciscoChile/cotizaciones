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
import { Principal } from '../../shared';

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

    filteredContacts: any[];
    filteredLocations: any[];
    currentAccount: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private salesService: SalesService,
        private clientService: ClientService,
        private contactService: ContactService,
        private locationService: LocationService,
        private productService: ProductService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    ngOnInit() {

      this.principal.identity().then((account) => {
        this.currentAccount = account;
      });

        this.isSaving = false;
        this.clientService.query({'active.equals': 1})
            .subscribe((res: HttpResponse<Client[]>) => { this.clients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));

        this.contactService.query({'active.equals': 1})
            .subscribe(
              (res: HttpResponse<Contact[]>) => {
                this.contacts = res.body;
                this.filterContacts(this.sales.clientId);
              }, (res: HttpErrorResponse) => this.onError(res.message));

        this.locationService.query({'active.equals': 1})
            .subscribe((res: HttpResponse<Location[]>) => {
              this.locations = res.body;
              this.filterLocations(this.sales.clientId);
            }, (res: HttpErrorResponse) => this.onError(res.message));

        this.productService.query({'active.equals': 1})
            .subscribe((res: HttpResponse<Product[]>) => { this.products = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));

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
            this.sales.userId = this.currentAccount.id;

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

    captureValueClient(event: any) {
      this.sales.contactId = undefined;
      this.sales.locationId = undefined;
      this.filterContacts(this.sales.clientId);
      this.filterLocations(this.sales.clientId);
    }

    filterContacts(value) {
        this.filteredContacts = [];
        for (let i = 0; i < this.contacts.length; i++) {
            const prod = this.contacts[i];
            if (prod.clientId === value) {
                this.filteredContacts.push(prod);
            }
        }
    }

    filterLocations(value) {
        this.filteredLocations = [];
        for (let i = 0; i < this.locations.length; i++) {
            const prod = this.locations[i];
            if (prod.clientId === value) {
                this.filteredLocations.push(prod);
            }
        }
    }

    numberOnly(event): boolean {
      const charCode = (event.which) ? event.which : event.keyCode;
      if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
      }
      return true;

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
