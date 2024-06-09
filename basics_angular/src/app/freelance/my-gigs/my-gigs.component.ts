import { Component } from '@angular/core';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-my-gigs',
  templateUrl: './my-gigs.component.html',
  styleUrl: './my-gigs.component.css'
})
export class MyGigsComponent {
  constructor( private userConnected:FooConnectedUserService) {

      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  

}
