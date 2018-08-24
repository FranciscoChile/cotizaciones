import { BaseEntity } from './../../shared';

export class SaleCondition implements BaseEntity {
    constructor(
        public id?: number,
        public key?: string,
        public value?: string,
        public salesId?: number,
    ) {
    }
}
