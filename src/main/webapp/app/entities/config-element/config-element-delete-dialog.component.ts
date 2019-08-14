import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ConfigElement } from './config-element.model';
import { ConfigElementPopupService } from './config-element-popup.service';
import { ConfigElementService } from './config-element.service';

@Component({
    selector: 'jhi-config-element-delete-dialog',
    templateUrl: './config-element-delete-dialog.component.html'
})
export class ConfigElementDeleteDialogComponent {

    configElement: ConfigElement;

    constructor(
        private configElementService: ConfigElementService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.configElementService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'configElementListModification',
                content: 'Deleted an configElement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-config-element-delete-popup',
    template: ''
})
export class ConfigElementDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private configElementPopupService: ConfigElementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.configElementPopupService
                .open(ConfigElementDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
