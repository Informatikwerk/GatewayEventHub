import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ConfigElementComponent } from './config-element.component';
import { ConfigElementDetailComponent } from './config-element-detail.component';
import { ConfigElementPopupComponent } from './config-element-dialog.component';
import { ConfigElementDeletePopupComponent } from './config-element-delete-dialog.component';

export const configElementRoute: Routes = [
    {
        path: 'config-element',
        component: ConfigElementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfigElements'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'config-element/:id',
        component: ConfigElementDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfigElements'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const configElementPopupRoute: Routes = [
    {
        path: 'config-element-new',
        component: ConfigElementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfigElements'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'config-element/:id/edit',
        component: ConfigElementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfigElements'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'config-element/:id/delete',
        component: ConfigElementDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ConfigElements'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
