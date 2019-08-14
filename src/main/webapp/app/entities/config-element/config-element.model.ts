import { BaseEntity } from './../../shared';

export class ConfigElement implements BaseEntity {
    constructor(
        public id?: number,
        public key?: string,
        public value?: string,
    ) {
    }
}
