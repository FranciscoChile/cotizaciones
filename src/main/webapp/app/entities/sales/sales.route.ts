import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SalesComponent } from './sales.component';
import { SalesDetailComponent } from './sales-detail.component';
import { SalesPopupComponent } from './sales-dialog.component';
import { SalesDeletePopupComponent } from './sales-delete-dialog.component';

@Injectable()
export class SalesResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const salesRoute: Routes = [
    {
        path: 'sales',
        component: SalesComponent,
        resolve: {
            'pagingParams': SalesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.sales.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sales/:id',
        component: SalesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.sales.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salesPopupRoute: Routes = [
    {
        path: 'sales-new',
        component: SalesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.sales.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales/:id/edit',
        component: SalesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.sales.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales/:id/delete',
        component: SalesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cotizacionesApp.sales.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
