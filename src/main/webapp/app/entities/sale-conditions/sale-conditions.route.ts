import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SaleConditionsComponent } from './sale-conditions.component';
import { SaleConditionsDetailComponent } from './sale-conditions-detail.component';
import { SaleConditionsPopupComponent } from './sale-conditions-dialog.component';
import { SaleConditionsDeletePopupComponent } from './sale-conditions-delete-dialog.component';

export const saleConditionsRoute: Routes = [
    {
        path: 'sale-conditions',
        component: SaleConditionsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleConditions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sale-conditions/:id',
        component: SaleConditionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleConditions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleConditionsPopupRoute: Routes = [
    {
        path: 'sale-conditions-new',
        component: SaleConditionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-conditions/:id/edit',
        component: SaleConditionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-conditions/:id/delete',
        component: SaleConditionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
