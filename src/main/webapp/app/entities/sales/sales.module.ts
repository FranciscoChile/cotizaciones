import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CotizacionesSharedModule } from '../../shared';
import {
    SalesService,
    SalesPopupService,
    SalesComponent,
    SalesDetailComponent,
    SalesDialogComponent,
    SalesPopupComponent,
    SalesDeletePopupComponent,
    SalesDeleteDialogComponent,
    salesRoute,
    salesPopupRoute,
    SalesResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...salesRoute,
    ...salesPopupRoute,
];

@NgModule({
    imports: [
        CotizacionesSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SalesComponent,
        SalesDetailComponent,
        SalesDialogComponent,
        SalesDeleteDialogComponent,
        SalesPopupComponent,
        SalesDeletePopupComponent,
    ],
    entryComponents: [
        SalesComponent,
        SalesDialogComponent,
        SalesPopupComponent,
        SalesDeleteDialogComponent,
        SalesDeletePopupComponent,
    ],
    providers: [
        SalesService,
        SalesPopupService,
        SalesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CotizacionesSalesModule {}
