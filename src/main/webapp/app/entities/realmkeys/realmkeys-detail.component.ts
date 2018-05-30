import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Realmkeys } from './realmkeys.model';
import { RealmkeysService } from './realmkeys.service';

@Component({
    selector: 'jhi-realmkeys-detail',
    templateUrl: './realmkeys-detail.component.html'
})
export class RealmkeysDetailComponent implements OnInit, OnDestroy {

    realmkeys: Realmkeys;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private realmkeysService: RealmkeysService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRealmkeys();
    }

    load(id) {
        this.realmkeysService.find(id)
            .subscribe((realmkeysResponse: HttpResponse<Realmkeys>) => {
                this.realmkeys = realmkeysResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRealmkeys() {
        this.eventSubscriber = this.eventManager.subscribe(
            'realmkeysListModification',
            (response) => this.load(this.realmkeys.id)
        );
    }
}
