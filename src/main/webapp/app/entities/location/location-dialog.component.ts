import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Location } from './location.model';
import { LocationPopupService } from './location-popup.service';
import { LocationService } from './location.service';
import { Client, ClientService } from '../client';

import { DatePipe } from '@angular/common';

@Component({
    selector: 'jhi-location-dialog',
    templateUrl: './location-dialog.component.html'
})
export class LocationDialogComponent implements OnInit {

    location: Location;
    isSaving: boolean;

    clients: Client[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private locationService: LocationService,
        private clientService: ClientService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;

        if (this.location.id !== undefined) {
            const pipe = new DatePipe('es-CL');
            const now = Date.now();
            const myFormattedDate = pipe.transform(this.location.createDate, 'yyyy-MM-dd');
            this.location.createDate = myFormattedDate;
        } else {
            const pipe = new DatePipe('es-CL');
            const now = Date.now();
            const myFormattedDate = pipe.transform(now, 'yyyy-MM-dd');
            this.location.createDate = myFormattedDate;
        }

        this.clientService.query({'active.equals': 1})
            .subscribe((res: HttpResponse<Client[]>) => { this.clients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.location.id !== undefined) {
            this.location.createDate += ' 00:00:00';
            this.subscribeToSaveResponse(
                this.locationService.update(this.location));
        } else {
            this.location.active = 1;

            const pipe = new DatePipe('es-CL');
            const now = Date.now();
            const myFormattedDate = pipe.transform(now, 'yyyy-MM-dd 00:00:00');
            this.location.createDate = myFormattedDate;

            this.subscribeToSaveResponse(
                this.locationService.create(this.location));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Location>>) {
        result.subscribe((res: HttpResponse<Location>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Location) {
        this.eventManager.broadcast({ name: 'locationListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-location-popup',
    template: ''
})
export class LocationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private locationPopupService: LocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.locationPopupService
                    .open(LocationDialogComponent as Component, params['id']);
            } else {
                this.locationPopupService
                    .open(LocationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
