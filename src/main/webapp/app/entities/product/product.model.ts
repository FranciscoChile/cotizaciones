import { BaseEntity } from './../../shared';

export class Product implements BaseEntity {
    constructor(
        public id?: number,
        public model?: string,
        public priceList?: number,
        public stock?: number,
        public description?: string,
        public imageRefContentType?: string,
        public imageRef?: any,
        public loadDiagramContentType?: string,
        public loadDiagram?: any,
        public createDate?: any,
        public active?: number,
        public sales?: BaseEntity[],
    ) {
    }
}
