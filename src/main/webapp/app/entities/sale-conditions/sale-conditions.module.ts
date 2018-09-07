import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CotizacionesSharedModule } from '../../shared';
import {
    SaleConditionsService,
    SaleConditionsPopupService,
    SaleConditionsComponent,
    SaleConditionsDetailComponent,
    SaleConditionsDialogComponent,
    SaleConditionsPopupComponent,
    SaleConditionsDeletePopupComponent,
    SaleConditionsDeleteDialogComponent,
    saleConditionsRoute,
    saleConditionsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...saleConditionsRoute,
    ...saleConditionsPopupRoute,
];

@NgModule({
    imports: [
        CotizacionesSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SaleConditionsComponent,
        SaleConditionsDetailComponent,
        SaleConditionsDialogComponent,
        SaleConditionsDeleteDialogComponent,
        SaleConditionsPopupComponent,
        SaleConditionsDeletePopupComponent,
    ],
    entryComponents: [
        SaleConditionsComponent,
        SaleConditionsDialogComponent,
        SaleConditionsPopupComponent,
        SaleConditionsDeleteDialogComponent,
        SaleConditionsDeletePopupComponent,
    ],
    providers: [
        SaleConditionsService,
        SaleConditionsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CotizacionesSaleConditionsModule {}
