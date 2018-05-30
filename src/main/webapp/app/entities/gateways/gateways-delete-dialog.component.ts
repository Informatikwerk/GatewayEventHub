import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Gateways } from './gateways.model';
import { GatewaysPopupService } from './gateways-popup.service';
import { GatewaysService } from './gateways.service';

@Component({
    selector: 'jhi-gateways-delete-dialog',
    templateUrl: './gateways-delete-dialog.component.html'
})
export class GatewaysDeleteDialogComponent {

    gateways: Gateways;

    constructor(
        private gatewaysService: GatewaysService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.gatewaysService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'gatewaysListModification',
                content: 'Deleted an gateways'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-gateways-delete-popup',
    template: ''
})
export class GatewaysDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gatewaysPopupService: GatewaysPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.gatewaysPopupService
                .open(GatewaysDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
