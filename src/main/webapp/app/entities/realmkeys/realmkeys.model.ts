import { BaseEntity } from './../../shared';

export class Realmkeys implements BaseEntity {
    constructor(
        public id?: number,
        public realmkey?: string,
        public gateways?: BaseEntity,
    ) {
    }
}
