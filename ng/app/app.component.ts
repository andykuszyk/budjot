import { Component } from '@angular/core';
import { AuthBase } from './authbase';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '@abacritt/angularx-social-login';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
@Injectable()
export class AppComponent extends AuthBase {
    title = 'Budjot - A simple budget builder!';

    constructor(http: HttpClient, authService: AuthService) {
        super(http, authService);
    }
}
