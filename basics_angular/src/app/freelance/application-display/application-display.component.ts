import { Component, Input, OnInit } from '@angular/core';
import { Application } from '../models/application';
import { ApplicationService } from '../../Services/freelance/application.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { response } from 'express';
import { TimeOutFunctions } from '../../Services/utility/time-out-functions';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';
import { moveMessagePortToContext } from 'worker_threads';

@Component({
  selector: 'app-application-display',
  templateUrl: './application-display.component.html',
  styleUrl: './application-display.component.css'
})
export class ApplicationDisplayComponent implements  OnInit {

  constructor(private serviceApp:ApplicationService , private activatedRoute:ActivatedRoute , private route:Router , 
    private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  //spinner
  spinnerOn:boolean=false
  //-----Error---Msg----
  errorMsg:string|null=null
  //------- data--To---fill---orPassedByParent---
  @Input() applicationModel:Application | null = null
  //------http---related
  private applicationSubscription: Subscription | undefined;
  //---------path-----file-----download
  pathToDownload:string = environment.apiFileDownload+'/'
  //----- Data passed for routing --------
  @Input() gigmodelId:number|null = null
  //--------Show-not-SHow------------
  @Input() showDetailComponent=true
  @Input() showElementsInListView:boolean=true
  @Input() itsDateNotTooLateForAction:boolean = true // (not used yet)
 
 
  
  ngOnInit():void{
    //load data only if this component is called Directly
    let idAppFromRoute:any;
     this.activatedRoute.params.subscribe(param => idAppFromRoute= param['idApp']);
    if(!this.applicationModel){
      this.applicationSubscription = this.serviceApp.getApplication(idAppFromRoute)
        .subscribe( 
          (response:HttpResponse<Application>) => {
            this.applicationModel = response.body
            console.log(response.body);
            if(this.applicationModel != null)
               this.gigmodelId!=this.applicationModel.gigId
        } );
    }
    
    //replaced in service
    // //this to display links in an array (stored in Db as CSV-String)
    // if(this.applicationModel){
    //   this.applicationModel.workReferenceLinksDto = this.applicationModel.workReferenceLinks?.split(',').filter(link=>link.trim() !=='')
    // }
  }


  deleteApplication(){
    console.log("delete called");
    if(this.applicationModel == null)
      return
    this.spinnerOn=true
    this.serviceApp.deleteApplication(this.applicationModel.id!).subscribe(
      response =>{

        console.log(response.status);
        if(!(response.status >= 200 && response.status<=202)){
          TimeOutFunctions.runWithTimeoutAction(3000,()=>this.errorMsg="can't delete application too Late",()=> {this.errorMsg=null ; this.spinnerOn=false})
          return
        }
        this.spinnerOn=false
        if(this.applicationModel?.gigId != null){

          this.route.navigate(['/gigs',this.gigmodelId]);
          return;
        }
        this.route.navigate(['/gigs'])
          
      } 
    );

  }


  choseApplicationWon() {
    console.log("chose application called");
    if(this.applicationModel?.gigId && this.applicationModel !== null ){
      //animation
    this.spinnerOn=true

      console.log(this.applicationModel.gigId , this.applicationModel.id );
      //gigModelId may be null if applicationDisplay is called directly not from parent
      this.serviceApp.choseApplicationWon(this.applicationModel.gigId!, this.applicationModel.id!).subscribe( response => {
          let msg ="";
          console.log("bodyResponse:"+response.body);
          console.log("bodyStatus:"+response.status);
          if( response.status >=200 && response.status <=202){
            msg = response.body?.gigAssigned ? "Successfully Chose This Application " : " Successfull disAssigne the Application from your Gig"
            TimeOutFunctions.runWithTimeoutAction(4000,
              ()=>{ this.errorMsg=msg  ; this.spinnerOn=false},
              ()=> this.route.navigate(['/gigs',this.applicationModel?.gigId])
            )
            return;
          }
          //off spinner
          this.spinnerOn=false
          
          if(response.body == null || response.body == undefined)
            msg=" Illegal Action By You Have Been Commited"

          if(new Date(Date.now()) > new Date(response.body?.projectStart!) )
            msg="U can't Assign Application Because The Start Date To Work On your Gig Have Already Passed"
          if(response.body?.milestones!.some(milestone => milestone.deposited))
            msg="U have MileStone Already Deposited and a freelance should be working on it can't cancel at this moment"
          TimeOutFunctions.runWithTimeoutAction(5000,
            ()=> this.errorMsg=msg ,
            ()=> this.errorMsg=null
          )
      })
    }

  }




  //-------------display----methods--------
  toggleDetails(){
    this.showDetailComponent = !this.showDetailComponent
    console.log("from application-display Component => ",this.showDetailComponent)
  }


}
