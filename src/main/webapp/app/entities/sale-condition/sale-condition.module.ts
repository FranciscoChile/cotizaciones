import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AutoCompleteModule } from 'primeng/primeng';

import { CotizacionesSharedModule } from '../../shared';
import {
    SaleConditionService,
    SaleConditionPopupService,
    SaleConditionComponent,
    SaleConditionDetailComponent,
    SaleConditionDialogComponent,
    SaleConditionPopupComponent,
    SaleConditionDeletePopupComponent,
    SaleConditionDeleteDialogComponent,
    saleConditionRoute,
    saleConditionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...saleConditionRoute,
    ...saleConditionPopupRoute,
];

@NgModule({
    imports: [
        CotizacionesSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        BrowserAnimationsModule,
        AutoCompleteModule
    ],
    declarations: [
        SaleConditionComponent,
        SaleConditionDetailComponent,
        SaleConditionDialogComponent,
        SaleConditionDeleteDialogComponent,
        SaleConditionPopupComponent,
        SaleConditionDeletePopupComponent,
    ],
    entryComponents: [
        SaleConditionComponent,
        SaleConditionDialogComponent,
        SaleConditionPopupComponent,
        SaleConditionDeleteDialogComponent,
        SaleConditionDeletePopupComponent,
    ],
    providers: [
        SaleConditionService,
        SaleConditionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CotizacionesSaleConditionModule {}
