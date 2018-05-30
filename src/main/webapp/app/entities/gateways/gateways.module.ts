import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewayeventhubSharedModule } from '../../shared';
import {
    GatewaysService,
    GatewaysPopupService,
    GatewaysComponent,
    GatewaysDetailComponent,
    GatewaysDialogComponent,
    GatewaysPopupComponent,
    GatewaysDeletePopupComponent,
    GatewaysDeleteDialogComponent,
    gatewaysRoute,
    gatewaysPopupRoute,
} from './';

const ENTITY_STATES = [
    ...gatewaysRoute,
    ...gatewaysPopupRoute,
];

@NgModule({
    imports: [
        GatewayeventhubSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        GatewaysComponent,
        GatewaysDetailComponent,
        GatewaysDialogComponent,
        GatewaysDeleteDialogComponent,
        GatewaysPopupComponent,
        GatewaysDeletePopupComponent,
    ],
    entryComponents: [
        GatewaysComponent,
        GatewaysDialogComponent,
        GatewaysPopupComponent,
        GatewaysDeleteDialogComponent,
        GatewaysDeletePopupComponent,
    ],
    providers: [
        GatewaysService,
        GatewaysPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayeventhubGatewaysModule {}
