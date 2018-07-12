import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Sales } from './sales.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Sales>;

@Injectable()
export class SalesService {

    private resourceUrl =  SERVER_API_URL + 'api/sales';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(sales: Sales): Observable<EntityResponseType> {
        const copy = this.convert(sales);
        return this.http.post<Sales>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(sales: Sales): Observable<EntityResponseType> {
        const copy = this.convert(sales);
        return this.http.put<Sales>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Sales>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Sales[]>> {
        const options = createRequestOption(req);
        return this.http.get<Sales[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Sales[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Sales = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Sales[]>): HttpResponse<Sales[]> {
        const jsonResponse: Sales[] = res.body;
        const body: Sales[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Sales.
     */
    private convertItemFromServer(sales: Sales): Sales {
        const copy: Sales = Object.assign({}, sales);
        copy.createDate = this.dateUtils
            .convertDateTimeFromServer(sales.createDate);
        return copy;
    }

    /**
     * Convert a Sales to a JSON which can be sent to the server.
     */
    private convert(sales: Sales): Sales {
        const copy: Sales = Object.assign({}, sales);

        copy.createDate = this.dateUtils.toDate(sales.createDate);
        return copy;
    }
}
