import { BaseEntity } from './../../shared';

export class Client implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public numDocument?: string,
        public address?: string,
        public comments?: string,
        public createDate?: any,
        public active?: number,
        public contacts?: BaseEntity[],
        public locations?: BaseEntity[],
        public sales?: BaseEntity[],
    ) {
    }
}
