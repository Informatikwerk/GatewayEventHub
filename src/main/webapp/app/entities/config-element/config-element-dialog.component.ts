import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ConfigElement } from './config-element.model';
import { ConfigElementPopupService } from './config-element-popup.service';
import { ConfigElementService } from './config-element.service';

@Component({
    selector: 'jhi-config-element-dialog',
    templateUrl: './config-element-dialog.component.html'
})
export class ConfigElementDialogComponent implements OnInit {

    configElement: ConfigElement;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private configElementService: ConfigElementService,
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
        if (this.configElement.id !== undefined) {
            this.subscribeToSaveResponse(
                this.configElementService.update(this.configElement));
        } else {
            this.subscribeToSaveResponse(
                this.configElementService.create(this.configElement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ConfigElement>>) {
        result.subscribe((res: HttpResponse<ConfigElement>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ConfigElement) {
        this.eventManager.broadcast({ name: 'configElementListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-config-element-popup',
    template: ''
})
export class ConfigElementPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private configElementPopupService: ConfigElementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.configElementPopupService
                    .open(ConfigElementDialogComponent as Component, params['id']);
            } else {
                this.configElementPopupService
                    .open(ConfigElementDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
