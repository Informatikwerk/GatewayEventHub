/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayeventhubTestModule } from '../../../test.module';
import { GatewaysComponent } from '../../../../../../main/webapp/app/entities/gateways/gateways.component';
import { GatewaysService } from '../../../../../../main/webapp/app/entities/gateways/gateways.service';
import { Gateways } from '../../../../../../main/webapp/app/entities/gateways/gateways.model';

describe('Component Tests', () => {

    describe('Gateways Management Component', () => {
        let comp: GatewaysComponent;
        let fixture: ComponentFixture<GatewaysComponent>;
        let service: GatewaysService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [GatewaysComponent],
                providers: [
                    GatewaysService
                ]
            })
            .overrideTemplate(GatewaysComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GatewaysComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GatewaysService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Gateways(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.gateways[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
