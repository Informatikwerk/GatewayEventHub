import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Realmkeys } from './realmkeys.model';
import { RealmkeysService } from './realmkeys.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-realmkeys',
    templateUrl: './realmkeys.component.html'
})
export class RealmkeysComponent implements OnInit, OnDestroy {
realmkeys: Realmkeys[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private realmkeysService: RealmkeysService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.realmkeysService.query().subscribe(
            (res: HttpResponse<Realmkeys[]>) => {
                this.realmkeys = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRealmkeys();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Realmkeys) {
        return item.id;
    }
    registerChangeInRealmkeys() {
        this.eventSubscriber = this.eventManager.subscribe('realmkeysListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
