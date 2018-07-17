/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CotizacionesTestModule } from '../../../test.module';
import { SalesDetailComponent } from '../../../../../../main/webapp/app/entities/sales/sales-detail.component';
import { SalesService } from '../../../../../../main/webapp/app/entities/sales/sales.service';
import { Sales } from '../../../../../../main/webapp/app/entities/sales/sales.model';

describe('Component Tests', () => {

    describe('Sales Management Detail Component', () => {
        let comp: SalesDetailComponent;
        let fixture: ComponentFixture<SalesDetailComponent>;
        let service: SalesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SalesDetailComponent],
                providers: [
                    SalesService
                ]
            })
            .overrideTemplate(SalesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Sales(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.sales).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
