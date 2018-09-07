import { BaseEntity } from './../../shared';

export class SaleConditions implements BaseEntity {
    constructor(
        public id?: number,
        public key?: string,
        public value?: string,
        public sales?: BaseEntity[],
    ) {
    }
}
