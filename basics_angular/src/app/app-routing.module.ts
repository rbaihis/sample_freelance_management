import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { GigsComponent } from './freelance/gigs/gigs.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import { FakeLoginComponent } from './fake-login/fake-login.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'error',
    component: ErrorPageComponent
  },
  {
    path: 'gigs',
    pathMatch:'full',
    component: GigsComponent
  },
  { path: 'login', component: FakeLoginComponent },
  

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }

