import { Component, OnInit } from '@angular/core';
import { AuthService, GoogleLoginProvider } from 'angularx-social-login';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
@Injectable()
export class LoginComponent implements OnInit {
    constructor(private authService: AuthService, private http: HttpClient) { }

    isLoggedIn: boolean;

    signInWithGoogle(): void {
        this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
    }

    signOut(): void {
        this.authService.signOut();
    }

    onSignIn(user: any) {
        if (user == null) {
            this.isLoggedIn = false;
            return;
        } else {
            this.isLoggedIn = true;
        }
        var idToken = user.idToken;
        console.log(idToken);
        
        var httpOptions = {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Authorization': idToken
            })
        };
        this.http.post(window.location.protocol + '//' + window.location.host + '/users', { 'foo': 'bar' }, httpOptions).subscribe(res => {
            console.log(res);
        });
    }

    ngOnInit() {
        this.authService.authState.subscribe((user) => { 
            console.log(user);
            this.onSignIn(user);
        });
    }

}
