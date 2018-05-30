import { BaseEntity } from './../../shared';

export class Gateways implements BaseEntity {
    constructor(
        public id?: number,
        public externalIp?: string,
        public internalIp?: string,
        public userId?: string,
    ) {
    }
}
