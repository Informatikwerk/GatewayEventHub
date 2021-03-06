import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Realmkeys } from './realmkeys.model';
import { RealmkeysService } from './realmkeys.service';

@Injectable()
export class RealmkeysPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private realmkeysService: RealmkeysService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.realmkeysService.find(id)
                    .subscribe((realmkeysResponse: HttpResponse<Realmkeys>) => {
                        const realmkeys: Realmkeys = realmkeysResponse.body;
                        this.ngbModalRef = this.realmkeysModalRef(component, realmkeys);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.realmkeysModalRef(component, new Realmkeys());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    realmkeysModalRef(component: Component, realmkeys: Realmkeys): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.realmkeys = realmkeys;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
