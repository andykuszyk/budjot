export class BudjotEntry {
    name: string;
    amount: number;
    paid: boolean;

    constructor(name: string, amount: number, paid: boolean) {
        this.name = name;
        this.amount = amount;
        this.paid = paid;
    }
}
