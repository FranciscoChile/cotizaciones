import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SaleConditions } from './sale-conditions.model';
import { SaleConditionsPopupService } from './sale-conditions-popup.service';
import { SaleConditionsService } from './sale-conditions.service';

@Component({
    selector: 'jhi-sale-conditions-delete-dialog',
    templateUrl: './sale-conditions-delete-dialog.component.html'
})
export class SaleConditionsDeleteDialogComponent {

    saleConditions: SaleConditions;

    constructor(
        private saleConditionsService: SaleConditionsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saleConditionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'saleConditionsListModification',
                content: 'Deleted an saleConditions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-conditions-delete-popup',
    template: ''
})
export class SaleConditionsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleConditionsPopupService: SaleConditionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.saleConditionsPopupService
                .open(SaleConditionsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
