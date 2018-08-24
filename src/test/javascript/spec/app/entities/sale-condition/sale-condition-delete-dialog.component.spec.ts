/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CotizacionesTestModule } from '../../../test.module';
import { SaleConditionDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition-delete-dialog.component';
import { SaleConditionService } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.service';

describe('Component Tests', () => {

    describe('SaleCondition Management Delete Component', () => {
        let comp: SaleConditionDeleteDialogComponent;
        let fixture: ComponentFixture<SaleConditionDeleteDialogComponent>;
        let service: SaleConditionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SaleConditionDeleteDialogComponent],
                providers: [
                    SaleConditionService
                ]
            })
            .overrideTemplate(SaleConditionDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleConditionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleConditionService);
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
