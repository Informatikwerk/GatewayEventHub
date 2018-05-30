/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GatewayeventhubTestModule } from '../../../test.module';
import { GatewaysDetailComponent } from '../../../../../../main/webapp/app/entities/gateways/gateways-detail.component';
import { GatewaysService } from '../../../../../../main/webapp/app/entities/gateways/gateways.service';
import { Gateways } from '../../../../../../main/webapp/app/entities/gateways/gateways.model';

describe('Component Tests', () => {

    describe('Gateways Management Detail Component', () => {
        let comp: GatewaysDetailComponent;
        let fixture: ComponentFixture<GatewaysDetailComponent>;
        let service: GatewaysService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GatewayeventhubTestModule],
                declarations: [GatewaysDetailComponent],
                providers: [
                    GatewaysService
                ]
            })
            .overrideTemplate(GatewaysDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GatewaysDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GatewaysService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Gateways(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.gateways).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
