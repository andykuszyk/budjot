import { BudjotEntry } from './budjotentry';

export class Budjot {
    id: number;
    name: string;
    income: number;
    expenditure: number;
    paid: number;
    remaining: number;
    credit: number;
    entries: BudjotEntry[];

    constructor(name: string, income: number) {
        this.name = name;
        this.income = income;
        this.entries = [];
        this.updateFields();
    }

    addEntry(entry: BudjotEntry) {
        this.entries.push(entry);
        this.updateFields();
    }

    removeEntry(index: number) {
        this.entries.splice(index, 1);
        this.updateFields();
    }

    updateFields() {
        let expenditure = 0;
        let paid = 0;
        if(this.entries == null){
            console.log("Warning! entries is null!");
        }
        for (let entry of this.entries) {
            expenditure += entry.amount;
            if(entry.paid) {
                paid += entry.amount;
            }
        }
        this.expenditure = expenditure;
        this.paid = paid;
        this.remaining = this.income - this.paid;
        this.credit = this.income - this.expenditure;
    }
}
