import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SaleCondition } from './sale-condition.model';
import { SaleConditionPopupService } from './sale-condition-popup.service';
import { SaleConditionService } from './sale-condition.service';

@Component({
    selector: 'jhi-sale-condition-delete-dialog',
    templateUrl: './sale-condition-delete-dialog.component.html'
})
export class SaleConditionDeleteDialogComponent {

    saleCondition: SaleCondition;

    constructor(
        private saleConditionService: SaleConditionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saleConditionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'saleConditionListModification',
                content: 'Deleted an saleCondition'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-condition-delete-popup',
    template: ''
})
export class SaleConditionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleConditionPopupService: SaleConditionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.saleConditionPopupService
                .open(SaleConditionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
