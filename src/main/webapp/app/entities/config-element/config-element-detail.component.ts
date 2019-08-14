import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ConfigElement } from './config-element.model';
import { ConfigElementService } from './config-element.service';

@Component({
    selector: 'jhi-config-element-detail',
    templateUrl: './config-element-detail.component.html'
})
export class ConfigElementDetailComponent implements OnInit, OnDestroy {

    configElement: ConfigElement;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private configElementService: ConfigElementService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInConfigElements();
    }

    load(id) {
        this.configElementService.find(id)
            .subscribe((configElementResponse: HttpResponse<ConfigElement>) => {
                this.configElement = configElementResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInConfigElements() {
        this.eventSubscriber = this.eventManager.subscribe(
            'configElementListModification',
            (response) => this.load(this.configElement.id)
        );
    }
}
