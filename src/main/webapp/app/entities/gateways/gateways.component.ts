import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Gateways } from './gateways.model';
import { GatewaysService } from './gateways.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-gateways',
    templateUrl: './gateways.component.html'
})
export class GatewaysComponent implements OnInit, OnDestroy {
gateways: Gateways[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private gatewaysService: GatewaysService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.gatewaysService.query().subscribe(
            (res: HttpResponse<Gateways[]>) => {
                this.gateways = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInGateways();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Gateways) {
        return item.id;
    }
    registerChangeInGateways() {
        this.eventSubscriber = this.eventManager.subscribe('gatewaysListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
