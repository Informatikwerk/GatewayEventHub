/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayeventhubTestModule } from '../../../test.module';
import { RealmkeysDialogComponent } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys-dialog.component';
import { RealmkeysService } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.service';
import { Realmkeys } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.model';
import { GatewaysService } from '../../../../../../main/webapp/app/entities/gateways';

describe('Component Tests', () => {

    describe('Realmkeys Management Dialog Component', () => {
        let comp: RealmkeysDialogComponent;
        let fixture: ComponentFixture<RealmkeysDialogComponent>;
        let service: RealmkeysService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [RealmkeysDialogComponent],
                providers: [
                    GatewaysService,
                    RealmkeysService
                ]
            })
            .overrideTemplate(RealmkeysDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RealmkeysDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RealmkeysService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Realmkeys(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.realmkeys = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'realmkeysListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Realmkeys();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.realmkeys = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'realmkeysListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
