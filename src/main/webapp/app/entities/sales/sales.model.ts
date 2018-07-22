import { BaseEntity } from './../../shared';

export class Sales implements BaseEntity {
    constructor(
        public id?: number,
        public finalPrice?: number,
        public createDate?: any,
        public active?: number,
        public conditions?: string,
        public clientId?: number,
        public clientName?: string,
        public contactId?: number,
        public locationId?: number,
        public products?: BaseEntity[],
        public clientAddress?: string,
        public clientNumDocument?: string,
        public contactName?: string,
        public contactSurname?: string,
        public contactCellPhone?: string
    ) {
    }
}
