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

    user: any;
    
    signInWithGoogle(): void {
        this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
    }

    signOut(): void {
        this.authService.signOut();
    }

    onSignIn() {
        if (this.user == null) {
            return;
        }
        var idToken = this.user.idToken;
        console.log(idToken);
        
        var httpOptions = {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Authorization': idToken
            })
        };

        var response = this.http.post('http://localhost:8080/users', { 'foo': 'bar' }, httpOptions);
        console.log(response);
    }

    ngOnInit() {
        this.authService.authState.subscribe((user) => { 
            console.log(user);
            //this.user = user;
            //this.onSignIn();
        });
    }

}
