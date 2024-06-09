import { Component, Input } from '@angular/core';
import { Application } from '../models/application';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-my-applications',
  templateUrl: './my-applications.component.html',
  styleUrl: './my-applications.component.css'
})
export class MyApplicationsComponent {
  constructor( private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  
  showDetail:boolean = false 
  @Input() applications:Application[]=[
        {
          id:1,
          freelancer: "myself",
          freelancerProfile:"github.com/tony",
          description:"blabla abala anhaln anal anla alnal ajam ajal amjam aja hioglgouggbgougogo gogoigoi gogigb gohlknmkn vbbonlkbi obb",
          files:[
              {
                name :"description",
                extension :".txt",
                path :"description.txt"
              },
              {
                name :"template",
                extension :".png",
                path :"userId-project-id-template.png"
              }
          ],
          questions:"please mor detail about ?",
          bid : 300,
          extraCost: undefined,
          workReferenceLinksDto:[
            "facebook.com",
            "twitter.com"
          ]
        },
        {
          id:2,
          freelancer: "myself",
          freelancerProfile:"github.com/salah",
          description:"blabla abala anhaln anal anla alnal ajam ajal amjam aja",
          files:[
            
          ],
          questions:"please mor detail about ?",
          bid : 300,
          extraCost: undefined,
          workReferenceLinksDto:[
            "facebook.com",
            "twitter.com"
          ]
        },
        {
          id:3,
          freelancer: "myself",
          freelancerProfile:"github.com/asma",
          description:"blabla abala anhaln anal anla alnal ajam ajal amjam aja",
          files:[
            
          ],
          questions: undefined,
          bid : 300,
          extraCost: undefined,
          workReferenceLinksDto:[
            "facebook.com",
            "twitter.com"
          ]

        }
      ]
    
  
}
