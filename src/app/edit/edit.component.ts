import { Component, OnInit } from '@angular/core';
import { Budjot } from '../budjot';
import { BudjotEntry } from '../budjotentry';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
    budjot: Budjot;
    newEntry: BudjotEntry;

    constructor() {
        this.budjot = new Budjot("Test Budjot", 1000);
        this.budjot.addEntry(new BudjotEntry("Bills", 3.1415, true, () => this.budjot.updateFields()));
        this.newEntry = new BudjotEntry(null, null, null, () => this.budjot.updateFields());
    }

    add() {
        this.budjot.addEntry(this.newEntry);
        this.newEntry = new BudjotEntry(null, null, null, () => this.budjot.updateFields());
    }

    delete(index: number) {
       this.budjot.removeEntry(index); 
    }

    ngOnInit() {}

}
