import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Sales } from './sales.model';
import { SalesService } from './sales.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { Product, ProductService } from '../product';

const headerHiab = require('./hiab_head_image.png');
const blankImage = require('./white.jpg');
const portadaHiab = require('./portada.png');

let encondeWhiteAux;
let portadaHiabAux;

const pdfMake = require('./pdfmake.min.js');
const pdfFonts = require('./vfs_fonts.js');
pdfMake.vfs = pdfFonts.pdfMake.vfs;

@Component({
    selector: 'jhi-sales',
    templateUrl: './sales.component.html',
    providers: [CurrencyPipe]
})
export class SalesComponent implements OnInit, OnDestroy {

currentAccount: any;
    sales: Sales[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    es: any;
    fromDate: Date;
    toDate: Date;

    constructor(
        private salesService: SalesService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private productService: ProductService,
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    captureValue(event: any) {
        if (this.fromDate !== undefined && this.toDate !== undefined) {
          if (this.fromDate < this.toDate) {
              this.loadAll();
          } else {
            this.jhiAlertService.error('cotizacionesApp.sales.diffDate', null, null);
          }
        }
    }

    loadAll() {
        let formattedToDate;
        let formattedFromDate;

        if (this.fromDate !== undefined && this.toDate !== undefined) {
          const pipe = new DatePipe('es-CL');
          const now = Date.now();
          formattedToDate = pipe.transform(this.toDate, 'yyyy-MM-dd');
          formattedFromDate = pipe.transform(this.fromDate, 'yyyy-MM-dd');

          formattedToDate += 'T00:00:00.000Z';
          formattedFromDate += 'T00:00:00.000Z';
        }

        this.principal.identity().then((account) => {

            let userId = '';
            if (account.login !== 'admin') {
              userId = account.id;
            }

            this.salesService.query({
                'createDate.greaterOrEqualThan': formattedFromDate ? formattedFromDate : '',
                'createDate.lessOrEqualThan': formattedToDate ? formattedToDate : '',
                'userId.equals': userId,
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: HttpResponse<Sales[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
            );

        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/sales'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/sales', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.es = {
            firstDayOfWeek: 1,
            dayNames: [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ],
            dayNamesShort: [ 'dom', 'lun', 'mar', 'mié', 'jue', 'vie', 'sáb' ],
            dayNamesMin: [ 'D', 'L', 'M', 'X', 'J', 'V', 'S' ],
            monthNames: [ 'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio', 'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre' ],
            monthNamesShort: [ 'ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dic' ],
            today: 'Hoy',
            clear: 'Borrar'
        };

        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSales();
        this.loadAll();

        this.toDataUrl(blankImage, function(encodedWhite) {
          encondeWhiteAux = encodedWhite;
        });

        this.toDataUrl(portadaHiab, function(encodedImage) {
          portadaHiabAux = encodedImage;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Sales) {
        return item.id;
    }
    registerChangeInSales() {
        this.eventSubscriber = this.eventManager.subscribe('salesListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    exportPdf(idSale) {

      let docDefinition;
      let salesPdf: Sales;
      let productPdf: Product;

      const pipe = new DatePipe('es-CL'); // Use your own locale
      const now = Date.now();
      const myFormattedDate = pipe.transform(now, 'longDate');

      const currencyPipe = new CurrencyPipe('es-CL');

      this.salesService.find(idSale)
          .subscribe((salesResponse: HttpResponse<Sales>) => {
              salesPdf = salesResponse.body;

              let moneyDisplay = currencyPipe.transform( salesPdf.finalPrice, 'CLP', 'symbol-narrow', '1.0' );
              moneyDisplay = moneyDisplay.replace('$', '');
              moneyDisplay = '$ ' + moneyDisplay;

              this.productService.find(salesPdf.productId)
                .subscribe((productResponse: HttpResponse<Product>) => {
                    productPdf = productResponse.body;

                    const imageRefAux = productPdf.imageRef === null ? encondeWhiteAux : 'data:image/jpeg;base64,' + productPdf.imageRef;
                    const loadDiagramAux = productPdf.loadDiagram === null ? encondeWhiteAux : 'data:image/jpeg;base64,' + productPdf.loadDiagram;

                    this.toDataUrl(headerHiab, function(encodedImage) {

                      docDefinition = {
                          content: [
                            {
                              image: portadaHiabAux,
                              width: 500,
                              margin: [0, 0, 0, 0],
                              pageBreak: 'after'
                            },
                            {
                              stack: [
                                {text: 'Santiago, ' + myFormattedDate, style: 'subheader'},
                                {text: 'Cotización Nº '},
                              ]
                            },
                            {
                              text: 'Precio final ' + moneyDisplay
                            },
                            {
                                image: imageRefAux,
                                width: 350,
                                margin: [0, 0],
                            },
                            {
                                image: loadDiagramAux,
                                width: 350,
                                margin: [0, 0],
                                // pageBreak: 'before'
                            }
                          ],
                          styles: {
                            header: {
                              fontSize: 18,
                              bold: true,
                              alignment: 'center',
                              margin: [0, 190, 0, 80]
                            },
                            subheader: {
                              fontSize: 14,
                              alignment: 'center',
                              margin: [0, 100, 0, 0]
                            },
                            superMargin: {
                              margin: [20, 0, 40, 0],
                              fontSize: 15
                            }
                          }
                        };
                      pdfMake.createPdf(docDefinition).open();

                    });
              });
            });

    }

// convierte una imagen a formato dataurl base 64
    toDataUrl(file, callback) {
      const xhr = new XMLHttpRequest();
      xhr.responseType = 'blob';

      xhr.onload = function() {
        const reader = new FileReader();
        reader.onloadend = function() {
          callback(reader.result);
        };
        reader.readAsDataURL(xhr.response);
      };
      xhr.open('GET', file);
      xhr.send();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.sales = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
