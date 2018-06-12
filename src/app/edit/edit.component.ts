import { Component, OnInit } from '@angular/core';
import { Budjot } from '../budjot';
import { BudjotEntry } from '../budjotentry';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthBase } from '../authbase';
import { AuthService } from 'angularx-social-login';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
@Injectable()
export class EditComponent extends AuthBase {
    budjot: Budjot;
    newEntry: BudjotEntry;

    constructor(http: HttpClient, authService: AuthService) {
        super(http, authService);
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

    save() {
        var url = window.location.protocol + '//' + window.location.host + '/jots';
        this.http.post(url, this.budjot, { headers: new HttpHeaders({'Content-Type': 'application/json','Authorization': this.idToken}), observe: 'response'}).subscribe(res => {
            console.log(res.status);
        });
    }
}
