import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Sales } from './sales.model';
import { SalesService } from './sales.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

import { CurrencyPipe, DatePipe } from '@angular/common';

const headerHiab = require('./header_wide.png');
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

      const fullName = currentAccountAux.firstName + ' ' + currentAccountAux.lastName;
      let initials = fullName.charAt(0);

      for (let i = 0; i < fullName.length; i++) {
        const c = fullName.charAt(i);
        if (c === ' ') {
          initials = initials + fullName.charAt(i + 1);
        }
      }

      initials = initials.toUpperCase();

      this.salesService.find(idSale)
          .subscribe((salesResponse: HttpResponse<Sales>) => {
              salesPdf = salesResponse.body;
              productsList = salesPdf.products;

              let moneyDisplay = currencyPipe.transform( salesPdf.finalPrice, 'CLP', 'symbol-narrow', '1.0' );
              moneyDisplay = moneyDisplay.replace('$', '');
              moneyDisplay = '$ ' + moneyDisplay;

              const prodModel = productsList[0].model;

              docDefinition = {
                  pageSize: 'LETTER',
                  footer: function(currentPage, pageCount) {
                    if (currentPage === pageCount) {
                      return {
                        // alignment: 'left',
                        margin: [10, -100, 0, 0],
                        table: {
                          widths: ['*'],
                          body: [
                             [{text: fullName,
                             fontSize: 10, bold: true}],
                             [{text: 'Asistente comercial', fontSize: 10}],
                             [''],
                             [{text: 'Tel. ' + currentAccountAux.telephone, fontSize: 10}],
                             [{text: currentAccountAux.email,  fontSize: 10}],
                             [{image: logoHiabAux, width: 100}]
                          ]
                        },
                        layout: 'noBorders'
                      };
                    }
                  },
                  content: [
                    {
                      // portada, pagina 1
                      image: portadaHiabAux,
                      width: 632,
                      height: 820,
                      margin: [-50, -50],
                      pageBreak: 'after'
                    },
                    {
                      // encabezado
                      image: headerHiabAux,
                      absolutePosition: {x: 0, y: 0},
                      width: 632
                    },
                    {
                      margin: [0, -35, 0, 0],
                      text: 'COTIZACION ' + initials + ' ' + salesPdf.id + '\n' + prodModel,
                      fontSize: 30,
                      bold: true,
                      color: 'red'
                    },
                    {
                      margin: [0, 0, 0, 50],
                      text: 'Fecha: ' + myFormattedDate,
                      color: 'red'
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
                    }
                ]
              };

                // se agregan los equipos al PDF
                for (let i = 0; i < productsList.length; i++) {
                    const prod = productsList[i];

                    const imageRefAux = prod.imageRef === null ? encondeWhiteAux : 'data:image/jpeg;base64,' + prod.imageRef;
                    const loadDiagramAux = prod.loadDiagram === null ? encondeWhiteAux : 'data:image/jpeg;base64,' + prod.loadDiagram;

                    if (i > 0) {
                        productsListPdf.push(
                        {
                          // encabezado
                          image: headerHiabAux,
                          absolutePosition: {x: 0, y: 0},
                          width: 632
                        },
                        {
                          margin: [0, -35, 0, 0],
                          text: 'COTIZACION ' + initials + ' ' + salesPdf.id + '\n' + prodModel,
                          fontSize: 30,
                          bold: true,
                          color: 'red'
                        },
                        {
                          text: 'Fecha: ' + myFormattedDate,
                          color: 'red'
                        });
                    }

                    productsListPdf.push(
                    {
                      margin: [0, 40, 0, 0],
                      text: prod.model
                    });

                    if (prod.priceList !== null && prod.priceList > 0 ) {
                      let monProd = currencyPipe.transform( prod.priceList, 'CLP', 'symbol-narrow', '1.0' );
                      monProd = monProd.replace('$', '');
                      monProd = '$ ' + monProd;
                      productsListPdf.push({text: 'Precio: ' + monProd + '\n\n'});
                    } else {
                      productsListPdf.push({text: '\n'});
                    }

                    productsListPdf.push(
                      {
                        table: {
                          widths: [250, '*'],
                          heights: [150, 'auto', 'auto'],
                          body: [
                            [prod.description, {image: imageRefAux, width: 200, alignment: 'right'}],
                            [{text: 'Características Técnicas', colSpan: 2, alignment: 'center'}],
                            [{colSpan: 2, image: loadDiagramAux, fit: [450, 390], alignment: 'center'}]
                          ]
                        }, layout: 'noBorders'
                      },
                      {text: 'Se adjunta folleto técnico del modelo',
                        fontSize: 10, italics: true, pageBreak: 'after'}
                    );
                }

                priceConditions.push(
                  {
                    // encabezado
                    image: headerHiabAux,
                    absolutePosition: {x: 0, y: 0},
                    width: 632
                  },
                  {
                    margin: [0, -35, 0, 0],
                    text: 'COTIZACION ' + initials + ' ' + salesPdf.id + '\n' + prodModel,
                    fontSize: 30,
                    bold: true,
                    color: 'red'
                  },
                  {
                    text: 'Fecha: ' + myFormattedDate,
                    color: 'red'
                  },
                  {text: 'Precio y Condiciones Generales de Venta', bold: true, margin: [0, 60, 0, 20]},
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
