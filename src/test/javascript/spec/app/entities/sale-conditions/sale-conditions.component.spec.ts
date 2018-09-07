/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CotizacionesTestModule } from '../../../test.module';
import { SaleConditionsComponent } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions.component';
import { SaleConditionsService } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions.service';
import { SaleConditions } from '../../../../../../main/webapp/app/entities/sale-conditions/sale-conditions.model';

describe('Component Tests', () => {

    describe('SaleConditions Management Component', () => {
        let comp: SaleConditionsComponent;
        let fixture: ComponentFixture<SaleConditionsComponent>;
        let service: SaleConditionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SaleConditionsComponent],
                providers: [
                    SaleConditionsService
                ]
            })
            .overrideTemplate(SaleConditionsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleConditionsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleConditionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SaleConditions(123)],
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
