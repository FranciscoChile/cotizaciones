import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { SaleCondition } from './sale-condition.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<SaleCondition>;

@Injectable()
export class SaleConditionService {

    private resourceUrl =  SERVER_API_URL + 'api/sale-conditions';

    constructor(private http: HttpClient) { }

    create(saleCondition: SaleCondition): Observable<EntityResponseType> {
        const copy = this.convert(saleCondition);
        return this.http.post<SaleCondition>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(saleCondition: SaleCondition): Observable<EntityResponseType> {
        const copy = this.convert(saleCondition);
        return this.http.put<SaleCondition>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<SaleCondition>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<SaleCondition[]>> {
        const options = createRequestOption(req);
        return this.http.get<SaleCondition[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SaleCondition[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: SaleCondition = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<SaleCondition[]>): HttpResponse<SaleCondition[]> {
        const jsonResponse: SaleCondition[] = res.body;
        const body: SaleCondition[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to SaleCondition.
     */
    private convertItemFromServer(saleCondition: SaleCondition): SaleCondition {
        const copy: SaleCondition = Object.assign({}, saleCondition);
        return copy;
    }

    /**
     * Convert a SaleCondition to a JSON which can be sent to the server.
     */
    private convert(saleCondition: SaleCondition): SaleCondition {
        const copy: SaleCondition = Object.assign({}, saleCondition);
        return copy;
    }
}
