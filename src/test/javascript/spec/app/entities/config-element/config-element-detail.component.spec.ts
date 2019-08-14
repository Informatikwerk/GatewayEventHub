/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GatewayeventhubTestModule } from '../../../test.module';
import { ConfigElementDetailComponent } from '../../../../../../main/webapp/app/entities/config-element/config-element-detail.component';
import { ConfigElementService } from '../../../../../../main/webapp/app/entities/config-element/config-element.service';
import { ConfigElement } from '../../../../../../main/webapp/app/entities/config-element/config-element.model';

describe('Component Tests', () => {

    describe('ConfigElement Management Detail Component', () => {
        let comp: ConfigElementDetailComponent;
        let fixture: ComponentFixture<ConfigElementDetailComponent>;
        let service: ConfigElementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [ConfigElementDetailComponent],
                providers: [
                    ConfigElementService
                ]
            })
            .overrideTemplate(ConfigElementDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConfigElementDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfigElementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ConfigElement(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.configElement).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
