import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SocialLoginModule, SocialAuthServiceConfig, GoogleLoginProvider } from '@abacritt/angularx-social-login';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './/app-routing.module';
import { HomeComponent } from './home/home.component';
import { EditComponent } from './edit/edit.component';
import { ListComponent } from './list/list.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    EditComponent,
    ListComponent
  ],
  imports: [
      BrowserModule,
      HttpClientModule,
      FormsModule,
      AppRoutingModule,
      SocialLoginModule
  ],
    providers: [
        {
            provide: 'SocialAuthServiceConfig',
	    useValue: {
		providers: [
		    {
			id: GoogleLoginProvider.PROVIDER_ID,
			provider: new GoogleLoginProvider("763708471782-uc93cjsj2c03rm4j3jt8v91tbqu3namq.apps.googleusercontent.com")
		    }
		],
	    } as SocialAuthServiceConfig,
        }
    ],  
  bootstrap: [AppComponent]
})
export class AppModule { }
