import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Realmkeys } from './realmkeys.model';
import { RealmkeysPopupService } from './realmkeys-popup.service';
import { RealmkeysService } from './realmkeys.service';
import { Gateways, GatewaysService } from '../gateways';

@Component({
    selector: 'jhi-realmkeys-dialog',
    templateUrl: './realmkeys-dialog.component.html'
})
export class RealmkeysDialogComponent implements OnInit {

    realmkeys: Realmkeys;
    isSaving: boolean;

    gateways: Gateways[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private realmkeysService: RealmkeysService,
        private gatewaysService: GatewaysService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.gatewaysService.query()
            .subscribe((res: HttpResponse<Gateways[]>) => { this.gateways = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.realmkeys.id !== undefined) {
            this.subscribeToSaveResponse(
                this.realmkeysService.update(this.realmkeys));
        } else {
            this.subscribeToSaveResponse(
                this.realmkeysService.create(this.realmkeys));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Realmkeys>>) {
        result.subscribe((res: HttpResponse<Realmkeys>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Realmkeys) {
        this.eventManager.broadcast({ name: 'realmkeysListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackGatewaysById(index: number, item: Gateways) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-realmkeys-popup',
    template: ''
})
export class RealmkeysPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private realmkeysPopupService: RealmkeysPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.realmkeysPopupService
                    .open(RealmkeysDialogComponent as Component, params['id']);
            } else {
                this.realmkeysPopupService
                    .open(RealmkeysDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
