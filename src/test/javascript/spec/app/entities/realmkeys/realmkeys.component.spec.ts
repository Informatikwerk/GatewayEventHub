/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayeventhubTestModule } from '../../../test.module';
import { RealmkeysComponent } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.component';
import { RealmkeysService } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.service';
import { Realmkeys } from '../../../../../../main/webapp/app/entities/realmkeys/realmkeys.model';

describe('Component Tests', () => {

    describe('Realmkeys Management Component', () => {
        let comp: RealmkeysComponent;
        let fixture: ComponentFixture<RealmkeysComponent>;
        let service: RealmkeysService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [RealmkeysComponent],
                providers: [
                    RealmkeysService
                ]
            })
            .overrideTemplate(RealmkeysComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RealmkeysComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RealmkeysService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Realmkeys(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.realmkeys[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
