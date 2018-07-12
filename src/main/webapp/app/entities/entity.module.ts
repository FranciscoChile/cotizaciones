import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CotizacionesClientModule } from './client/client.module';
import { CotizacionesContactModule } from './contact/contact.module';
import { CotizacionesLocationModule } from './location/location.module';
import { CotizacionesSalesModule } from './sales/sales.module';
import { CotizacionesProductModule } from './product/product.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CotizacionesClientModule,
        CotizacionesContactModule,
        CotizacionesLocationModule,
        CotizacionesSalesModule,
        CotizacionesProductModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CotizacionesEntityModule {}
