/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GatewayeventhubTestModule } from '../../../test.module';
import { RealmkeysDetailComponent } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys-detail.component';
import { RealmkeysService } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.service';
import { Realmkeys } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.model';

describe('Component Tests', () => {

    describe('Realmkeys Management Detail Component', () => {
        let comp: RealmkeysDetailComponent;
        let fixture: ComponentFixture<RealmkeysDetailComponent>;
        let service: RealmkeysService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [RealmkeysDetailComponent],
                providers: [
                    RealmkeysService
                ]
            })
            .overrideTemplate(RealmkeysDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RealmkeysDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RealmkeysService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Realmkeys(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.realmkeys).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
