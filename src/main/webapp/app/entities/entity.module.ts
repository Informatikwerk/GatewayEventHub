import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GatewayeventhubGatewaysModule } from './gateways/gateways.module';
import { GatewayeventhubRealmkeysModule } from './realmkeys/realmkeys.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GatewayeventhubGatewaysModule,
        GatewayeventhubRealmkeysModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayeventhubEntityModule {}
