import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { GatewaysComponent } from './gateways.component';
import { GatewaysDetailComponent } from './gateways-detail.component';
import { GatewaysPopupComponent } from './gateways-dialog.component';
import { GatewaysDeletePopupComponent } from './gateways-delete-dialog.component';

export const gatewaysRoute: Routes = [
    {
        path: 'gateways',
        component: GatewaysComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Gateways'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'gateways/:id',
        component: GatewaysDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Gateways'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gatewaysPopupRoute: Routes = [
    {
        path: 'gateways-new',
        component: GatewaysPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Gateways'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gateways/:id/edit',
        component: GatewaysPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Gateways'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gateways/:id/delete',
        component: GatewaysDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Gateways'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
