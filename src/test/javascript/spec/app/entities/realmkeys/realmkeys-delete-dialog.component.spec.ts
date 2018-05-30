/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayeventhubTestModule } from '../../../test.module';
import { RealmkeysDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys-delete-dialog.component';
import { RealmkeysService } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.service';

describe('Component Tests', () => {

    describe('Realmkeys Management Delete Component', () => {
        let comp: RealmkeysDeleteDialogComponent;
        let fixture: ComponentFixture<RealmkeysDeleteDialogComponent>;
        let service: RealmkeysService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [RealmkeysDeleteDialogComponent],
                providers: [
                    RealmkeysService
                ]
            })
            .overrideTemplate(RealmkeysDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RealmkeysDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RealmkeysService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
