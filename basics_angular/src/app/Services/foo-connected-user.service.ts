import { Injectable } from '@angular/core';
import { AppUser } from '../freelance/models/app-user';

@Injectable({
  providedIn: 'root'
})
export class FooConnectedUserService {

  public currentUser : AppUser | null =null;
  constructor() {
    // this.currentUser=null;

    this.currentUser={
        id:6,
        name:"okBB",
        email:"rabbehs@gmail.com",
        password:"i don't care",
        roles:[{
          id:1,
          name:"USER"
        }]} 

    // this.currentUser={
    //     id:6,
    //     name:"HelloBB",
    //     email:"rabbehseif@gmail.com",
    //     password:"idon'tcare",
    //     roles:[{
    //       id:1,
    //       name:"USER"
    //     }] }
   }
   
}
