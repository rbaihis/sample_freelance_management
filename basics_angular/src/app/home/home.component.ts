import { Component, OnInit } from '@angular/core';
import { FooConnectedUserService } from '../Services/foo-connected-user.service';
import { AppUser } from '../freelance/models/app-user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
    constructor(  private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;

  ngOnInit():void{
      
  }
}
