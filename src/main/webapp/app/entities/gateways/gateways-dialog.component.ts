import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Gateways } from './gateways.model';
import { GatewaysPopupService } from './gateways-popup.service';
import { GatewaysService } from './gateways.service';

@Component({
    selector: 'jhi-gateways-dialog',
    templateUrl: './gateways-dialog.component.html'
})
export class GatewaysDialogComponent implements OnInit {

    gateways: Gateways;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private gatewaysService: GatewaysService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.gateways.id !== undefined) {
            this.subscribeToSaveResponse(
                this.gatewaysService.update(this.gateways));
        } else {
            this.subscribeToSaveResponse(
                this.gatewaysService.create(this.gateways));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Gateways>>) {
        result.subscribe((res: HttpResponse<Gateways>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Gateways) {
        this.eventManager.broadcast({ name: 'gatewaysListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-gateways-popup',
    template: ''
})
export class GatewaysPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gatewaysPopupService: GatewaysPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.gatewaysPopupService
                    .open(GatewaysDialogComponent as Component, params['id']);
            } else {
                this.gatewaysPopupService
                    .open(GatewaysDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
