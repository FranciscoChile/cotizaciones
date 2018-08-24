/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CotizacionesTestModule } from '../../../test.module';
import { SaleConditionComponent } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.component';
import { SaleConditionService } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.service';
import { SaleCondition } from '../../../../../../main/webapp/app/entities/sale-condition/sale-condition.model';

describe('Component Tests', () => {

    describe('SaleCondition Management Component', () => {
        let comp: SaleConditionComponent;
        let fixture: ComponentFixture<SaleConditionComponent>;
        let service: SaleConditionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SaleConditionComponent],
                providers: [
                    SaleConditionService
                ]
            })
            .overrideTemplate(SaleConditionComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleConditionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleConditionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SaleCondition(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.saleConditions[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
