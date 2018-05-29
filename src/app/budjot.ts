import { BudjotEntry } from './budjotentry';

export class Budjot {
    id: number;
    name: string;
    entries: BudjotEntry[];

    constructor(name: string, entries: BudjotEntry[]) {
        this.name = name;
        this.entries = entries;
    }
}
