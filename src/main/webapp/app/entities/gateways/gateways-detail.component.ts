import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Gateways } from './gateways.model';
import { GatewaysService } from './gateways.service';

@Component({
    selector: 'jhi-gateways-detail',
    templateUrl: './gateways-detail.component.html'
})
export class GatewaysDetailComponent implements OnInit, OnDestroy {

    gateways: Gateways;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private gatewaysService: GatewaysService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGateways();
    }

    load(id) {
        this.gatewaysService.find(id)
            .subscribe((gatewaysResponse: HttpResponse<Gateways>) => {
                this.gateways = gatewaysResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGateways() {
        this.eventSubscriber = this.eventManager.subscribe(
            'gatewaysListModification',
            (response) => this.load(this.gateways.id)
        );
    }
}
