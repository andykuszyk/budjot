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

    constructor() {
        this.budjot = new Budjot(
            "Test Budjot",
            [
                new BudjotEntry("Bills", 3.1415, false)
            ]
        );
    }

    ngOnInit() {}

}
