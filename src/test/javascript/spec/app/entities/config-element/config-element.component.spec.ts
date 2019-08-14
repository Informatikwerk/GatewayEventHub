/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayeventhubTestModule } from '../../../test.module';
import { ConfigElementComponent } from '../../../../../../main/webapp/app/entities/config-element/config-element.component';
import { ConfigElementService } from '../../../../../../main/webapp/app/entities/config-element/config-element.service';
import { ConfigElement } from '../../../../../../main/webapp/app/entities/config-element/config-element.model';

describe('Component Tests', () => {

    describe('ConfigElement Management Component', () => {
        let comp: ConfigElementComponent;
        let fixture: ComponentFixture<ConfigElementComponent>;
        let service: ConfigElementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [ConfigElementComponent],
                providers: [
                    ConfigElementService
                ]
            })
            .overrideTemplate(ConfigElementComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConfigElementComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigElementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ConfigElement(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.configElements[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
