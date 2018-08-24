/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { CotizacionesTestModule } from '../../../test.module';
import { SaleConditionDetailComponent } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition-detail.component';
import { SaleConditionService } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.service';
import { SaleCondition } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.model';

describe('Component Tests', () => {

    describe('SaleCondition Management Detail Component', () => {
        let comp: SaleConditionDetailComponent;
        let fixture: ComponentFixture<SaleConditionDetailComponent>;
        let service: SaleConditionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SaleConditionDetailComponent],
                providers: [
                    SaleConditionService
                ]
            })
            .overrideTemplate(SaleConditionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleConditionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleConditionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new SaleCondition(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.saleCondition).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
