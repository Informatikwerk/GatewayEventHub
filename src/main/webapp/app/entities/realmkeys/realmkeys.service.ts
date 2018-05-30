import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Realmkeys } from './realmkeys.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Realmkeys>;

@Injectable()
export class RealmkeysService {

    private resourceUrl =  SERVER_API_URL + 'api/realmkeys';

    constructor(private http: HttpClient) { }

    create(realmkeys: Realmkeys): Observable<EntityResponseType> {
        const copy = this.convert(realmkeys);
        return this.http.post<Realmkeys>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(realmkeys: Realmkeys): Observable<EntityResponseType> {
        const copy = this.convert(realmkeys);
        return this.http.put<Realmkeys>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Realmkeys>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Realmkeys[]>> {
        const options = createRequestOption(req);
        return this.http.get<Realmkeys[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Realmkeys[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Realmkeys = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Realmkeys[]>): HttpResponse<Realmkeys[]> {
        const jsonResponse: Realmkeys[] = res.body;
        const body: Realmkeys[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Realmkeys.
     */
    private convertItemFromServer(realmkeys: Realmkeys): Realmkeys {
        const copy: Realmkeys = Object.assign({}, realmkeys);
        return copy;
    }

    /**
     * Convert a Realmkeys to a JSON which can be sent to the server.
     */
    private convert(realmkeys: Realmkeys): Realmkeys {
        const copy: Realmkeys = Object.assign({}, realmkeys);
        return copy;
    }
}
