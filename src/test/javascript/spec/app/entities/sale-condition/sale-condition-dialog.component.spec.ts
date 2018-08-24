/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CotizacionesTestModule } from '../../../test.module';
import { SaleConditionDialogComponent } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition-dialog.component';
import { SaleConditionService } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.service';
import { SaleCondition } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.model';
import { SalesService } from '../../../../../../main/webapp/app/entities/sales';

describe('Component Tests', () => {

    describe('SaleCondition Management Dialog Component', () => {
        let comp: SaleConditionDialogComponent;
        let fixture: ComponentFixture<SaleConditionDialogComponent>;
        let service: SaleConditionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SaleConditionDialogComponent],
                providers: [
                    SalesService,
                    SaleConditionService
                ]
            })
            .overrideTemplate(SaleConditionDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleConditionDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleConditionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SaleCondition(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.saleCondition = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'saleConditionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SaleCondition();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.saleCondition = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'saleConditionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
