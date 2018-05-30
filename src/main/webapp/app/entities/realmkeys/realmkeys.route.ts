import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { RealmkeysComponent } from './realmkeys.component';
import { RealmkeysDetailComponent } from './realmkeys-detail.component';
import { RealmkeysPopupComponent } from './realmkeys-dialog.component';
import { RealmkeysDeletePopupComponent } from './realmkeys-delete-dialog.component';

export const realmkeysRoute: Routes = [
    {
        path: 'realmkeys',
        component: RealmkeysComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Realmkeys'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'realmkeys/:id',
        component: RealmkeysDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Realmkeys'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const realmkeysPopupRoute: Routes = [
    {
        path: 'realmkeys-new',
        component: RealmkeysPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Realmkeys'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'realmkeys/:id/edit',
        component: RealmkeysPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Realmkeys'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'realmkeys/:id/delete',
        component: RealmkeysDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Realmkeys'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
