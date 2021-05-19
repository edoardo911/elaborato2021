import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { CookieService } from 'ngx-cookie-service';
import { firebaseConfig } from '../assets/private'
import { ChartsModule } from 'ng2-charts';
import { NgModule } from '@angular/core';

import { RegisterComponent } from './register/register.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { GraphComponent } from './graph/graph.component';
import { AuthComponent } from './auth/auth.component';
import { AppComponent } from './app.component';

import { AngularFireDatabaseModule } from '@angular/fire/database'
import { AngularFireModule } from '@angular/fire'

@NgModule({
  declarations: [
    RegisterComponent,
    HeaderComponent,
    LoginComponent,
    GraphComponent,
    AuthComponent,
    AppComponent,
  ],
  imports: [
    AngularFireModule.initializeApp(firebaseConfig),
    AngularFireDatabaseModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserModule,
    ChartsModule,
    FormsModule,
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
