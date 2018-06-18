import { Injectable } from '@angular/core';
import { AuthBase } from '../authbase';
import { AuthService } from 'angularx-social-login';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Budjot } from '../budjot';
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

    constructor(http: HttpClient, authService: AuthService, private router: Router) {
        super(http, authService);
    }
    
    onSignedIn() {
        if(!this.isLoggedIn) return;

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
        if(this.budjots == null) {
            return false;
        }
        for(var budjot of this.budjots) {
            if(budjot.isSelected) {
                return true;
            }
        }
        return false;
    }

    openBudjot(budjot: any) {
        this.router.navigate(['edit', budjot.id ]);
    }
}
