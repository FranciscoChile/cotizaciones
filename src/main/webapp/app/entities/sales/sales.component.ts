import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Sales } from './sales.model';
import { SalesService } from './sales.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

import { CurrencyPipe, DatePipe } from '@angular/common';

const headerHiab = require('./hiab_head_image.png');
const blankImage = require('./white.jpg');
const portadaHiab = require('./portada.png');
const logoHiab = require('./logoHiabSmall.jpeg');

let encondeWhiteAux;
let portadaHiabAux;
let headerHiabAux;
let logoHiabAux;
let currentAccountAux;

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
    rangeDates: Date[];

    constructor(
        private salesService: SalesService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {

        let formattedToDate;
        let formattedFromDate;

        const pipe = new DatePipe('es-CL');

        if (this.rangeDates) {
          formattedFromDate = pipe.transform(this.rangeDates[0], 'yyyy-MM-ddTHH:mm:ss.SSS');
          formattedFromDate += 'Z';
        }
        if (this.rangeDates) {
          formattedToDate = pipe.transform(this.rangeDates[1], 'yyyy-MM-ddTHH:mm:ss.SSS');
          formattedToDate += 'Z';
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
                'active.equals': 1,
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
            currentAccountAux = account;
        });
        this.registerChangeInSales();
        this.loadAll();

        this.toDataUrl(blankImage, function(encodedWhite) {
          encondeWhiteAux = encodedWhite;
        });

        this.toDataUrl(portadaHiab, function(encodedImage) {
          portadaHiabAux = encodedImage;
        });

        this.toDataUrl(headerHiab, function(encodedImage) {
          headerHiabAux = encodedImage;
        });

        this.toDataUrl(logoHiab, function(encodedImage) {
          logoHiabAux = encodedImage;
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

    clearValue(event: any) {
        this.loadAll();
    }

    captureValue(event: any) {
        if (this.rangeDates[0] != null && this.rangeDates[1] != null) {
          this.loadAll();
        }
    }

    exportPdf(idSale) {

      let docDefinition;
      let salesPdf: Sales;
      let productsList;
      const productsListPdf = [];
      const priceConditions = [];

      const pipe = new DatePipe('es-CL'); // Use your own locale
      const now = Date.now();
      const myFormattedDate = pipe.transform(now, 'longDate');

      const currencyPipe = new CurrencyPipe('es-CL');

      this.salesService.find(idSale)
          .subscribe((salesResponse: HttpResponse<Sales>) => {
              salesPdf = salesResponse.body;
              productsList = salesPdf.products;

              let moneyDisplay = currencyPipe.transform( salesPdf.finalPrice, 'CLP', 'symbol-narrow', '1.0' );
              moneyDisplay = moneyDisplay.replace('$', '');
              moneyDisplay = '$ ' + moneyDisplay;

              docDefinition = {
                  pageSize: 'LETTER',
                  header: function(currentPage, pageCount) {
                    if (currentPage > 1) {
                      // return {text: 'COTIZACION Nº ' + salesPdf.id, italics: true, fontSize: 30, color: 'red'};
                      return {image: headerHiabAux, width: 300, alignment: 'right'};
                    }
                  },
                  footer: function(currentPage, pageCount) {
                    if (currentPage === pageCount) {
                      return {
                        // alignment: 'left',
                        margin: [10, -100, 0, 0],
                        table: {
                          widths: ['*'],
                          body: [
                             [{text: currentAccountAux.firstName + ' ' + currentAccountAux.lastName,
                             fontSize: 10, bold: true}],
                             [{text: 'Asistente comercial', fontSize: 10}],
                             [''],
                             [{text: 'Tel. +56 2 2738 6993 ', fontSize: 10}],
                             [{text: 'Cel.+56 (9) 8551 0778 ', fontSize: 10}],
                             [''],
                             [{image: logoHiabAux, width: 100}]
                          ]
                        },
                        layout: 'noBorders'
                      };
                    }
                  },
                  content: [
                    {
                      image: portadaHiabAux,
                      width: 632,
                      height: 820,
                      margin: [-50, -50],
                      pageBreak: 'after'
                    },
                    {
                      margin: [0, 100, 0, 0],
                      alignment: 'center',
                      text: 'Santiago, ' + myFormattedDate,
                    },
                    {
                      margin: [0, 0, 0, 20],
                      alignment: 'center',
                      text: 'Cotización Nº ' + salesPdf.id,
                      bold: true
                    },
                    {
                      table: {
                        widths: [80, 180, 50, '*'],
                        body: [
                           [{text: 'Cliente: ', bold: true}, salesPdf.clientName, {text: 'Rut: ', bold: true}, salesPdf.clientNumDocument],
                           [{text: 'Dirección: ', bold: true}, salesPdf.clientAddress, '', ''],
                           [{text: 'Contacto: ', bold: true}, salesPdf.contactName + ' ' + salesPdf.contactSurname, {text: 'Email: ', bold: true}, salesPdf.contactEmail],
                           [{text: 'Celular: ', bold: true}, salesPdf.contactCellPhone, '', '']
                        ]
                      },
                      layout: 'noBorders'
                    },
                    {text: '', pageBreak: 'after'}
                ]
              };

                // se agregan los equipos al PDF
                for (let i = 0; i < productsList.length; i++) {
                    const prod = productsList[i];

                    const imageRefAux = prod.imageRef === null ? encondeWhiteAux : 'data:image/jpeg;base64,' + prod.imageRef;
                    const loadDiagramAux = prod.loadDiagram === null ? encondeWhiteAux : 'data:image/jpeg;base64,' + prod.loadDiagram;

                    // productsListPdf.push({text: 'Precio: ' + prod.priceList});
                    productsListPdf.push(
                      {
                        margin: [0, 80, 0, 10],
                        text: prod.model
                      },
                      {
                        table: {
                          widths: [250, '*'],
                          heights: [160, 'auto', 'auto'],
                          body: [
                            [prod.description, {image: imageRefAux, width: 200, alignment: 'right'}],
                            [{text: 'Características Técnicas', colSpan: 2, alignment: 'center'}],
                            [{colSpan: 2, image: loadDiagramAux, fit: [450, 400], alignment: 'center'}]
                          ]
                        }, layout: 'noBorders'
                      },
                      {text: 'Se adjunta folleto técnico del modelo',
                        fontSize: 10, italics: true, pageBreak: 'after'}
                    );
                }

                priceConditions.push(
                  {text: 'Precio y Condiciones Generales de Venta', bold: true, margin: [0, 80, 0, 20]},
                  {text: 'Precio final ' + moneyDisplay + ' + IVA', alignment: 'center', margin: [0, 0, 0, 20]},
                  {text: salesPdf.conditions ? salesPdf.conditions : '' }
                );

                docDefinition.content.push(productsListPdf);
                docDefinition.content.push(priceConditions);
                pdfMake.createPdf(docDefinition).open();
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

}
