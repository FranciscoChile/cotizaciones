/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CotizacionesTestModule } from '../../../test.module';
import { SaleConditionsDialogComponent } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions-dialog.component';
import { SaleConditionsService } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions.service';
import { SaleConditions } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions.model';
import { SalesService } from '../../../../../../main/webapp/app/entities/sales';

describe('Component Tests', () => {

    describe('SaleConditions Management Dialog Component', () => {
        let comp: SaleConditionsDialogComponent;
        let fixture: ComponentFixture<SaleConditionsDialogComponent>;
        let service: SaleConditionsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SaleConditionsDialogComponent],
                providers: [
                    SalesService,
                    SaleConditionsService
                ]
            })
            .overrideTemplate(SaleConditionsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleConditionsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleConditionsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SaleConditions(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.saleConditions = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'saleConditionsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SaleConditions();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.saleConditions = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'saleConditionsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
