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
        this.budjot = new Budjot(
            "Test Budjot",
            [
                new BudjotEntry("Bills", 3.1415, true)
            ]
        );
        this.newEntry = new BudjotEntry(null, null, null);
    }

    add() {
        this.budjot.entries.push(this.newEntry);
        this.newEntry = new BudjotEntry(null, null, null);
    }

    delete(index: number) {
       this.budjot.entries.splice(index, 1); 
    }

    ngOnInit() {}

}
