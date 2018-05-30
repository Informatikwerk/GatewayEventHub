import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewayeventhubSharedModule } from '../../shared';
import {
    RealmkeysService,
    RealmkeysPopupService,
    RealmkeysComponent,
    RealmkeysDetailComponent,
    RealmkeysDialogComponent,
    RealmkeysPopupComponent,
    RealmkeysDeletePopupComponent,
    RealmkeysDeleteDialogComponent,
    realmkeysRoute,
    realmkeysPopupRoute,
} from './';

const ENTITY_STATES = [
    ...realmkeysRoute,
    ...realmkeysPopupRoute,
];

@NgModule({
    imports: [
        GatewayeventhubSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RealmkeysComponent,
        RealmkeysDetailComponent,
        RealmkeysDialogComponent,
        RealmkeysDeleteDialogComponent,
        RealmkeysPopupComponent,
        RealmkeysDeletePopupComponent,
    ],
    entryComponents: [
        RealmkeysComponent,
        RealmkeysDialogComponent,
        RealmkeysPopupComponent,
        RealmkeysDeleteDialogComponent,
        RealmkeysDeletePopupComponent,
    ],
    providers: [
        RealmkeysService,
        RealmkeysPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayeventhubRealmkeysModule {}
