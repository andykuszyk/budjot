import { Component, OnInit } from '@angular/core';
import { Budjot } from '../budjot';
import { BudjotEntry } from '../budjotentry';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthBase } from '../authbase';
import { AuthService } from 'angularx-social-login';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
@Injectable()
export class EditComponent extends AuthBase {
    budjot: Budjot;
    newEntry: BudjotEntry;
    public id: string;

    constructor(http: HttpClient, authService: AuthService, private route: ActivatedRoute) {
        super(http, authService);
        this.newEntry = new BudjotEntry(null, null, null, () => this.budjot.updateFields());
    }

    protected onInit() {
        this.id = this.route.snapshot.paramMap.get('id');
        if(this.id == 'new') {
            this.budjot = new Budjot('New Budjot', 0);
        } else {
            var url  = window.location.protocol + '//' + window.location.host + '/jots/' + this.id;
            this.http.get(url, { headers: new HttpHeaders({'Content-Type': 'application/json', 'Authorization': this.idToken}), observe: 'response' }).subscribe(res => {
                console.log(res);
                this.budjot = new Budjot(res.body['name'], res.body['income']);
                for(let entry of res.body['entries']) {
                    this.budjot.addEntry(new BudjotEntry(entry['name'], entry['amount'], entry['paid'], () => this.budjot.updateFields()));
                }
            });
        }
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
            if(res.status == 405) {

            } else if (res.status == 201) {

            } else {

            }
        });
    }
}
