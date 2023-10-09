export class BudjotEntry {
    private _name: string;
    private _amount: number;
    private _paid: boolean;
    private _updateCallBack: any;

    get name():string {
        return this._name;
    }

    set name(value:string) {
        this._name = value;
        this._updateCallBack();
    }

    get amount():number {
        return this._amount;
    }

    set amount(value:number) {
        this._amount = value;
        this._updateCallBack();
    }

    get paid():boolean {
        return this._paid;
    }

    set paid(value:boolean) {
        this._paid = value;
        this._updateCallBack();
    }

    constructor(name: string, amount: number, paid: boolean, updateCallBack: any) {
        this._updateCallBack = updateCallBack;
        this._name = name;
        this._amount = amount;
        this._paid = paid;
    }
}
