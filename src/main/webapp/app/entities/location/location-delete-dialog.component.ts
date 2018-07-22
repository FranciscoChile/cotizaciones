import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Location } from './location.model';
import { LocationPopupService } from './location-popup.service';
import { LocationService } from './location.service';

@Component({
    selector: 'jhi-location-delete-dialog',
    templateUrl: './location-delete-dialog.component.html'
})
export class LocationDeleteDialogComponent {

    location: Location;

    constructor(
        private locationService: LocationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {

      this.location.id = id;
      this.location.active = 0;

      this.locationService.update(this.location).subscribe((response) => {
        this.eventManager.broadcast({
          name: 'locationListModification',
          content: 'Deleted an location'
        });
        this.activeModal.dismiss(true);
      });
    }

}

@Component({
    selector: 'jhi-location-delete-popup',
    template: ''
})
export class LocationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private locationPopupService: LocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.locationPopupService
                .open(LocationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
