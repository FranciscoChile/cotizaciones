import { BaseEntity } from './../../shared';

export class Location implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public createDate?: any,
        public active?: number,
        public clientId?: number,
        public sales?: BaseEntity[],
    ) {
    }
}
