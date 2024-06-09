import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';
import { HomeComponent } from './home/home.component';
import { FreelanceModule } from './freelance/freelance.module';
import { SharedModule } from './shared/shared.module';
import { FooterComponent } from './footer/footer.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HttpClientModule } from '@angular/common/http';
import { ErrorPageComponent } from './error-page/error-page.component';
import { FakeLoginComponent } from './fake-login/fake-login.component';


@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    HomeComponent,
    FooterComponent,
    ErrorPageComponent,
    FakeLoginComponent    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SharedModule,
    FreelanceModule,
    FontAwesomeModule,
    HttpClientModule
  ],
  providers: [
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
