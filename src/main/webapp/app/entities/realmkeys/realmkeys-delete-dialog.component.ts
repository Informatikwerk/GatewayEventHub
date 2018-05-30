import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Realmkeys } from './realmkeys.model';
import { RealmkeysPopupService } from './realmkeys-popup.service';
import { RealmkeysService } from './realmkeys.service';

@Component({
    selector: 'jhi-realmkeys-delete-dialog',
    templateUrl: './realmkeys-delete-dialog.component.html'
})
export class RealmkeysDeleteDialogComponent {

    realmkeys: Realmkeys;

    constructor(
        private realmkeysService: RealmkeysService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.realmkeysService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'realmkeysListModification',
                content: 'Deleted an realmkeys'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-realmkeys-delete-popup',
    template: ''
})
export class RealmkeysDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private realmkeysPopupService: RealmkeysPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.realmkeysPopupService
                .open(RealmkeysDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
