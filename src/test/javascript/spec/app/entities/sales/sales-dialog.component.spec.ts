/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { CotizacionesTestModule } from '../../../test.module';
import { SalesDialogComponent } from '../../../../../../main/webapp/app/entities/sales/sales-dialog.component';
import { SalesService } from '../../../../../../main/webapp/app/entities/sales/sales.service';
import { Sales } from '../../../../../../main/webapp/app/entities/sales/sales.model';
import { ClientService } from '../../../../../../main/webapp/app/entities/client';
import { ContactService } from '../../../../../../main/webapp/app/entities/contact';
import { ProductService } from '../../../../../../main/webapp/app/entities/product';
import { SaleConditionsService } from '../../../../../../main/webapp/app/entities/sale-conditions';

describe('Component Tests', () => {

    describe('Sales Management Dialog Component', () => {
        let comp: SalesDialogComponent;
        let fixture: ComponentFixture<SalesDialogComponent>;
        let service: SalesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SalesDialogComponent],
                providers: [
                    ClientService,
                    ContactService,
                    ProductService,
                    SaleConditionsService,
                    SalesService
                ]
            })
            .overrideTemplate(SalesDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Sales(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.sales = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Sales();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.sales = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
