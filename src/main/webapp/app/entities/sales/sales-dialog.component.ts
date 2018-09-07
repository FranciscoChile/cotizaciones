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
import { Product, ProductService } from '../product';
import { SaleConditions, SaleConditionsService } from '../sale-conditions';

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

    products: Product[];

    saleconditions: SaleConditions[];

    filteredContacts: any[];
    filteredLocations: any[];
    currentAccount: any;

    currentDate: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private salesService: SalesService,
        private clientService: ClientService,
        private contactService: ContactService,
        private productService: ProductService,
        private saleConditionsService: SaleConditionsService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    ngOnInit() {

      let pipe = new DatePipe('es-CL');
      let now = Date.now();
      this.currentDate = pipe.transform(now, 'longDate');

      this.principal.identity().then((account) => {
        this.currentAccount = account;
      });

        this.isSaving = false;
        this.clientService.query()
            .subscribe((res: HttpResponse<Client[]>) => { this.clients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.contactService.query()
            .subscribe((res: HttpResponse<Contact[]>) => {
              this.contacts = res.body;
              this.filterContacts(this.sales.clientId);
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.productService.query()
            .subscribe((res: HttpResponse<Product[]>) => { this.products = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.saleConditionsService.query()
            .subscribe((res: HttpResponse<SaleConditions[]>) => { this.saleconditions = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));

        if (this.sales.id !== undefined) {
          pipe = new DatePipe('es-CL');
          now = Date.now();
          const myFormattedDate = pipe.transform(this.sales.createDate, 'yyyy-MM-dd');
          this.sales.createDate = myFormattedDate;
        } else {
          pipe = new DatePipe('es-CL');
          now = Date.now();
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

    trackProductById(index: number, item: Product) {
        return item.id;
    }

    trackSaleConditionsById(index: number, item: SaleConditions) {
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
      this.filterContacts(this.sales.clientId);
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
