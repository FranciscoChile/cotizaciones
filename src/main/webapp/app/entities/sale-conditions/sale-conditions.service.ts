import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { SaleConditions } from './sale-conditions.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<SaleConditions>;

@Injectable()
export class SaleConditionsService {

    private resourceUrl =  SERVER_API_URL + 'api/sale-conditions';

    constructor(private http: HttpClient) { }

    create(saleConditions: SaleConditions): Observable<EntityResponseType> {
        const copy = this.convert(saleConditions);
        return this.http.post<SaleConditions>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(saleConditions: SaleConditions): Observable<EntityResponseType> {
        const copy = this.convert(saleConditions);
        return this.http.put<SaleConditions>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<SaleConditions>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<SaleConditions[]>> {
        const options = createRequestOption(req);
        return this.http.get<SaleConditions[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SaleConditions[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: SaleConditions = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<SaleConditions[]>): HttpResponse<SaleConditions[]> {
        const jsonResponse: SaleConditions[] = res.body;
        const body: SaleConditions[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to SaleConditions.
     */
    private convertItemFromServer(saleConditions: SaleConditions): SaleConditions {
        const copy: SaleConditions = Object.assign({}, saleConditions);
        return copy;
    }

    /**
     * Convert a SaleConditions to a JSON which can be sent to the server.
     */
    private convert(saleConditions: SaleConditions): SaleConditions {
        const copy: SaleConditions = Object.assign({}, saleConditions);
        return copy;
    }
}
