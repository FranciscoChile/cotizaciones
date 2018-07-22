import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Sales } from './sales.model';
import { SalesPopupService } from './sales-popup.service';
import { SalesService } from './sales.service';

@Component({
    selector: 'jhi-sales-delete-dialog',
    templateUrl: './sales-delete-dialog.component.html'
})
export class SalesDeleteDialogComponent {

    sales: Sales;

    constructor(
        private salesService: SalesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'salesListModification',
                content: 'Deleted an sales'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sales-delete-popup',
    template: ''
})
export class SalesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesPopupService: SalesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.salesPopupService
                .open(SalesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
