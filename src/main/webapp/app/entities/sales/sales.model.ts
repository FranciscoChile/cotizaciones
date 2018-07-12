import { BaseEntity } from './../../shared';

export class Sales implements BaseEntity {
    constructor(
        public id?: number,
        public finalPrice?: number,
        public createDate?: any,
        public active?: number,
        public clientId?: number,
        public contactId?: number,
        public locationId?: number,
        public productId?: number,
    ) {
    }
}
