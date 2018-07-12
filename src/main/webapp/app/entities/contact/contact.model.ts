import { BaseEntity } from './../../shared';

export class Contact implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public surname?: string,
        public position?: string,
        public area?: string,
        public address?: string,
        public cellphone?: string,
        public linePhone?: string,
        public email?: string,
        public comments?: string,
        public createDate?: any,
        public active?: number,
        public clientId?: number,
        public sales?: BaseEntity[],
    ) {
    }
}
