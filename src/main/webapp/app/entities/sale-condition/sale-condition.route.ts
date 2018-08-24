import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SaleConditionComponent } from './sale-condition.component';
import { SaleConditionDetailComponent } from './sale-condition-detail.component';
import { SaleConditionPopupComponent } from './sale-condition-dialog.component';
import { SaleConditionDeletePopupComponent } from './sale-condition-delete-dialog.component';

export const saleConditionRoute: Routes = [
    {
        path: 'sale-condition',
        component: SaleConditionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleCondition.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sale-condition/:id',
        component: SaleConditionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleCondition.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleConditionPopupRoute: Routes = [
    {
        path: 'sale-condition-new',
        component: SaleConditionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleCondition.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-condition/:id/edit',
        component: SaleConditionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleCondition.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-condition/:id/delete',
        component: SaleConditionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.saleCondition.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
