import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Gig } from '../models/gig';
import { ActivatedRoute, Router } from '@angular/router';
import { GigService } from '../../Services/freelance/gig.service';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TimeOutFunctions } from '../../Services/utility/time-out-functions';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-gig-display',
  templateUrl: './gig-display.component.html',
  styleUrl: './gig-display.component.css'
})
export class GigDisplayComponent implements OnInit,OnDestroy {
  constructor(private activatedRoute:ActivatedRoute, private gigService:GigService , private route:Router , private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  //check if too late to update  
  @Input() iSprojectStartDateBeforeToday:boolean=false

  //---msgTo display on fail of actions----
  msgFailDisplay:string|null=null;
  //---Element-To-Dispaly-Only-In-1-Self-Component-Not-As-Child
  @Input() onlyShowInComponent:boolean=true
  //------ 
  @Input() showMilestones: boolean = false;
  @Input() showApplications:boolean = false
  // 
  @Input() gigmodel: Gig | null = null
  @Input() showDescription:boolean =true
  //httpRequestRelated
  subscribe:Subscription | null = null ;
  //apiUrl for Download files
  pathToDownload:string = environment.apiFileDownload+'/'
  // [href]="file.path ? pathToDownload + file.path : '#'"
  

  //--------------------------------------Methods------------------------------------
  public ngOnInit(): void {
    if(this.gigmodel != null)
      return
    //getting Route Param :idGig
    let idGigFromRoute:number = 0 ;
    this.activatedRoute.params.subscribe(params => idGigFromRoute = params['idGig'] as number)
    
    //fetching data from API
    this.subscribe = this.gigService.getGig(idGigFromRoute).subscribe( (response : HttpResponse<Gig>) =>{
      this.gigmodel=response.body ?? null;
      console.log(response.body);
      console.log("ResponseStatus : "+response.status);

      //boolean for display purpose for actions
      this.iSprojectStartDateBeforeToday = new Date(Date.now()) > new Date(this.gigmodel?.projectStart!)
      console.log("this.iSprojectStartDateBeforeToday = new Date(Date.now()) > new Date(this.gigmodel?.projectStart!) : " , this.iSprojectStartDateBeforeToday);

    });  
  }
  //--------
  public ngOnDestroy(): void {
    if(this.subscribe)
      this.subscribe.unsubscribe;
  }
  //--------
  public deleteGig() {
    if( this.gigmodel && this.gigmodel.id)
      this.gigService.deleteGig(this.gigmodel.id).subscribe( response =>{ 
          console.log(response.status);
          console.log(response.body);
        if(response.status >= 200 && response.status <= 202)
          this.route.navigate(['/gigs'])
        else
           TimeOutFunctions.runWithTimeoutAction(3000,
          ()=> this.msgFailDisplay='Not allowed to Delete this Gig' ,
          ()=> this.msgFailDisplay=null
            ) 
       })
  }



  
  //--------
  public toggleDescription(){
    this.showDescription = !this.showDescription
    console.log("from Gig Component => ",this.showDescription);
  }
  //--------
  public toggleShowApplications(){
    this.showApplications = ! this.showApplications
    console.log("from Gig Component => ",this.showApplications);
  }
  //--------
  public toggleShowMilestones() {
    this.showMilestones = ! this.showMilestones
    console.log("from Gig Component => ",this.showMilestones);
  }
  
  
}
