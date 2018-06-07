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
    accountCreated: boolean;

    signInWithGoogle(): void {
        this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
    }

    signOut(): void {
        this.authService.signOut();
    }

    onSignIn(user: any) {
        if(user == null) { return; }
        var idToken = user.idToken;
        
        var url = window.location.protocol + '//' + window.location.host + '/users';

        this.http.post(url, null, { headers: new HttpHeaders({'Content-Type': 'application/json','Authorization': idToken}), observe: 'response'}).subscribe(res => {
            if(res.status == 201) {
                this.accountCreated = true;
                this.isLoggedIn = true;
            } else if(res.status == 204) {
                this.isLoggedIn = true;
            } else {
                console.log('Unknown response from /users: ' + res.status);
            }
        });
    }

    ngOnInit() {
        this.authService.authState.subscribe((user) => { 
            console.log(user);
            this.onSignIn(user);
        });
    }

}
