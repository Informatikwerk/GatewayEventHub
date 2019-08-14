import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ConfigElement } from './config-element.model';
import { ConfigElementService } from './config-element.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-config-element',
    templateUrl: './config-element.component.html'
})
export class ConfigElementComponent implements OnInit, OnDestroy {
configElements: ConfigElement[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private configElementService: ConfigElementService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.configElementService.query().subscribe(
            (res: HttpResponse<ConfigElement[]>) => {
                this.configElements = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInConfigElements();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ConfigElement) {
        return item.id;
    }
    registerChangeInConfigElements() {
        this.eventSubscriber = this.eventManager.subscribe('configElementListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
