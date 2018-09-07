/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CotizacionesTestModule } from '../../../test.module';
import { SaleConditionsDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions-delete-dialog.component';
import { SaleConditionsService } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions.service';

describe('Component Tests', () => {

    describe('SaleConditions Management Delete Component', () => {
        let comp: SaleConditionsDeleteDialogComponent;
        let fixture: ComponentFixture<SaleConditionsDeleteDialogComponent>;
        let service: SaleConditionsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SaleConditionsDeleteDialogComponent],
                providers: [
                    SaleConditionsService
                ]
            })
            .overrideTemplate(SaleConditionsDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleConditionsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleConditionsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
