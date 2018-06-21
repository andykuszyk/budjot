import { Injectable } from '@angular/core';
import { AuthBase } from '../authbase';
import { AuthService } from 'angularx-social-login';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Budjot } from '../budjot';
import { BudjotEntry } from '../budjotentry';
import { Router } from '@angular/router';
import * as $ from 'jquery';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
@Injectable()
export class ListComponent extends AuthBase {

    budjots: any;
    copyBudjotName: string;

    constructor(http: HttpClient, authService: AuthService, private router: Router) {
        super(http, authService);
    }

    copyBudjot() {
        if(this.copyBudjotName == null) {
            return;
        }
        var budjot = this.getSelectedBudjot();
        if(budjot == null) {
            return;
        }
        
        var url  = window.location.protocol + '//' + window.location.host + '/jots/' + budjot.id;
        this.http.get(url, { headers: new HttpHeaders({'Content-Type': 'application/json', 'Authorization': this.idToken}), observe: 'response' }).subscribe(res => {
            var budjotResponse = new Budjot(this.copyBudjotName, res.body['income']);
            for(let entry of res.body['entries']) {
                budjotResponse.addEntry(new BudjotEntry(entry['name'], entry['amount'], entry['paid'], () => budjotResponse.updateFields()));
            }
            
            url  = window.location.protocol + '//' + window.location.host + '/jots';
            this.http.post(url, budjotResponse, { headers: new HttpHeaders({'Content-Type': 'application/json','Authorization': this.idToken}), observe: 'response'}).subscribe(res => {
                this.getBudjots();
            });
        });

    }
    
    onSignedIn() {
        if(!this.isLoggedIn) return;
        this.getBudjots();
    }

    getBudjots() {
        var url = window.location.protocol + '//' + window.location.host + '/jots';
        this.http.get(url, { headers: new HttpHeaders({'Content-Type': 'application/json', 'Authorization': this.idToken}), observe: 'response' }).subscribe(res => {
            console.log(res);
            this.budjots = res.body;
            for(var budjot of this.budjots) {
                budjot.isSelected = false;
            }
        });
    }

    deleteSelectedBudjot() {
        var budjot = null;
        for(var b of this.budjots) {
            if(b.isSelected) {
                budjot = b;
                break;
            }
        }
        if(budjot == null) {
            return;
        }
        console.log('Deleting budjot: ' + budjot.name);
        var url = window.location.protocol + '//' + window.location.host + '/jots/' + budjot.id;
        this.http.delete(url, { headers: new HttpHeaders({'Content-Type': 'application/json', 'Authorization': this.idToken}), observe: 'response' }).subscribe(res => {
            if(res.status == 200) {
                this.getBudjots();
            }
        });
    }

    onBudjotSelected(budjot: any) {
        for(var b of this.budjots) {
            if(b.name != budjot.name) {
                b.isSelected = false;
            }
        }
    }

    isBudjotSelected() {
        return this.getSelectedBudjot() != null;
    }

    getSelectedBudjot() {
        if(this.budjots == null) {
            return null;
        }
        for(var budjot of this.budjots) {
            if(budjot.isSelected) {
                return budjot;
            }
        }
        return null;
    }

    openBudjot(budjot: any) {
        this.router.navigate(['edit', budjot.id ]);
    }
}
