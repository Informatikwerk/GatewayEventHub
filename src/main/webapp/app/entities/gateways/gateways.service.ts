import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Gateways } from './gateways.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Gateways>;

@Injectable()
export class GatewaysService {

    private resourceUrl =  SERVER_API_URL + 'api/gateways';

    constructor(private http: HttpClient) { }

    create(gateways: Gateways): Observable<EntityResponseType> {
        const copy = this.convert(gateways);
        return this.http.post<Gateways>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(gateways: Gateways): Observable<EntityResponseType> {
        const copy = this.convert(gateways);
        return this.http.put<Gateways>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Gateways>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Gateways[]>> {
        const options = createRequestOption(req);
        return this.http.get<Gateways[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Gateways[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Gateways = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Gateways[]>): HttpResponse<Gateways[]> {
        const jsonResponse: Gateways[] = res.body;
        const body: Gateways[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Gateways.
     */
    private convertItemFromServer(gateways: Gateways): Gateways {
        const copy: Gateways = Object.assign({}, gateways);
        return copy;
    }

    /**
     * Convert a Gateways to a JSON which can be sent to the server.
     */
    private convert(gateways: Gateways): Gateways {
        const copy: Gateways = Object.assign({}, gateways);
        return copy;
    }
}
