import { Component, Injectable, OnInit } from '@angular/core';
import { AuthBase } from '../authbase';
import { AuthService } from 'angularx-social-login';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
@Injectable()
export class LoginComponent extends AuthBase {
    constructor(http: HttpClient, authService: AuthService) {
        super(http, authService);
    }
}
