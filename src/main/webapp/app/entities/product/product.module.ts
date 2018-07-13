import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AutoCompleteModule } from 'primeng/primeng';

import { CotizacionesSharedModule } from '../../shared';
import {
    ProductService,
    ProductPopupService,
    ProductComponent,
    ProductDetailComponent,
    ProductDialogComponent,
    ProductPopupComponent,
    ProductDeletePopupComponent,
    ProductDeleteDialogComponent,
    productRoute,
    productPopupRoute,
    ProductResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...productRoute,
    ...productPopupRoute,
];

@NgModule({
    imports: [
        CotizacionesSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        BrowserAnimationsModule,
        AutoCompleteModule
    ],
    declarations: [
        ProductComponent,
        ProductDetailComponent,
        ProductDialogComponent,
        ProductDeleteDialogComponent,
        ProductPopupComponent,
        ProductDeletePopupComponent,
    ],
    entryComponents: [
        ProductComponent,
        ProductDialogComponent,
        ProductPopupComponent,
        ProductDeleteDialogComponent,
        ProductDeletePopupComponent,
    ],
    providers: [
        ProductService,
        ProductPopupService,
        ProductResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CotizacionesProductModule {}
