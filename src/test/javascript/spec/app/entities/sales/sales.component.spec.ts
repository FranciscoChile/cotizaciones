/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CotizacionesTestModule } from '../../../test.module';
import { SalesComponent } from '../../../../../../main/webapp/app/entities/sales/sales.component';
import { SalesService } from '../../../../../../main/webapp/app/entities/sales/sales.service';
import { Sales } from '../../../../../../main/webapp/app/entities/sales/sales.model';

describe('Component Tests', () => {

    describe('Sales Management Component', () => {
        let comp: SalesComponent;
        let fixture: ComponentFixture<SalesComponent>;
        let service: SalesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CotizacionesTestModule],
                declarations: [SalesComponent],
                providers: [
                    SalesService
                ]
            })
            .overrideTemplate(SalesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Sales(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.sales[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
