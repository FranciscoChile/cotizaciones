import { BaseEntity } from './../../shared';

export class Sales implements BaseEntity {
    constructor(
        public id?: number,
        public finalPrice?: number,
        public currency?: string,
        public createDate?: any,
        public userId?: number,
        public clientId?: number,
        public clientName?: string,
        public contactId?: number,
        public products?: BaseEntity[],
        public saleConditions?: BaseEntity[],
        public clientAddress?: string,
        public clientNumDocument?: string,
        public contactName?: string,
        public contactSurname?: string,
        public contactCellPhone?: string,
        public contactEmail?: string
    ) {
    }
}
