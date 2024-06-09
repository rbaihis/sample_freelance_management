import { Component, Input, OnInit } from '@angular/core';
import { Milestone } from '../models/milestone';
import { environment } from '../../../environments/environment';
import { ActivatedRoute, Router } from '@angular/router';
import { MilestoneService } from '../../Services/freelance/milestone.service';
import { Subscription } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { TimeOutFunctions } from '../../Services/utility/time-out-functions';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-milestone-display',
  templateUrl: './milestone-display.component.html',
  styleUrl: './milestone-display.component.css'
})


export class MilestoneDisplayComponent implements OnInit {

  constructor(private router:Router , private milestoneService:MilestoneService, private activateRoute:ActivatedRoute ,
      private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  //date-Ok-For-Ask-Refund-User
  dateTodayPlus3DaysGreaterOrEqualsOfPaymentDate : boolean = false; 
  dateTodayGreaterOrEqualsPaymentDate : boolean = false; 
  

  // @Input() showDescription: boolean= false
  // @Input() showDetailMilestoneComponent:boolean=false
  //---http--
  msSubscribtion:Subscription|null=null;
  //----data---
  @Input() milestoneModel: Milestone | null =null;
  //----error-msg-----
  errorMsg:string|null=null

  @Input() gigmodelId?:number|null = null
  //apiUrl for Download files
  pathToDownload:string = environment.apiFileDownload+'/'

  updateMilestone() {
    console.log("updateMilestone() to be implemented");
    // this.router.navigate([])

  } 

  ngOnInit(): void {

      let idMsFromRoute:any;
     this.activateRoute.params.subscribe(param => idMsFromRoute= param['idMs']);
    if(this.milestoneModel == null){
      this.msSubscribtion = this.milestoneService.getMilestone(idMsFromRoute)
        .subscribe( 
          (response:HttpResponse<Milestone>) => {
            this.milestoneModel = response.body
            if(this.milestoneModel != null)
               this.gigmodelId!=this.milestoneModel.gigId
            console.log(this.milestoneModel);
            

            //check if datepayment is after 3 days To display button refund or payment 
            let todayPlus3Days=new Date(Date.now()+ 3 * 24* 60 * 60 *1000 ) ;
            let newDatePayment=new Date(this.milestoneModel?.datePayment!)
            this.dateTodayPlus3DaysGreaterOrEqualsOfPaymentDate = todayPlus3Days >=  newDatePayment
            this.dateTodayGreaterOrEqualsPaymentDate =  new Date(Date.now()) >=  newDatePayment
            console.log("dateTodayGreaterOrEqualsPaymentDate : ",this.dateTodayGreaterOrEqualsPaymentDate);
            console.log("dateTodayPlus3DaysGreaterOrEqualsOfPaymentDate : ",this.dateTodayPlus3DaysGreaterOrEqualsOfPaymentDate);
        } );
    }


    
  }


  //-----------------------------------------
  activateMileStone() {
      if(this.milestoneModel?.deposited){
        TimeOutFunctions.runWithTimeoutAction(3000, ()=>this.errorMsg="already Deposited and activated ",()=>this.errorMsg=null)
        return
      }
        
      let msId:number = this.milestoneModel?.id!
      this.milestoneService.activateMs(msId).subscribe( 
          (response:HttpResponse<any>) => {
              console.log(response.status);
              console.log(response.body);
              if(response.status>=200&&response.status<=202){
              
                TimeOutFunctions.runWithTimeoutAction(2000,()=>this.errorMsg="milestone successfully Activated",
                  ()=> this.router.navigate(['/gigs',this.milestoneModel?.gigId]))
                return;
               }
              TimeOutFunctions.runWithTimeoutAction(1500,()=>this.errorMsg="U need To chose Application first Or milestone Already Activated ", ()=>this.errorMsg=null)
        } );
  }
  //---------------------
  reportMilestone() {
    if(this.milestoneModel?.reclamationByGigCreator || !this.dateTodayGreaterOrEqualsPaymentDate){
        if(this.milestoneModel?.reclamationByGigCreator)
          TimeOutFunctions.runWithTimeoutAction(3000, ()=>this.errorMsg="reclamation already occured" ,()=>this.errorMsg=null)
        if(!this.dateTodayGreaterOrEqualsPaymentDate)
          TimeOutFunctions.runWithTimeoutAction(3000, ()=>this.errorMsg="too early to report ",()=>this.errorMsg=null)
        return
      }
      console.log("clicked report ms");
      let msId:number = this.milestoneModel?.id!
      this.milestoneService.reportMsByCustomer(msId).subscribe( 
          (response:HttpResponse<any>) => {
              console.log(response.status);
              console.log(response.body);
              if(response.status>=200&&response.status<=202){
              
                TimeOutFunctions.runWithTimeoutAction(2000,()=>this.errorMsg="milestone successfully Reported",
                  ()=> this.router.navigate(['/gigs',this.milestoneModel?.gigId]))
                return;
               }
              TimeOutFunctions.runWithTimeoutAction(1500,()=>this.errorMsg="too early to report ", ()=>this.errorMsg=null)
        } );
  }

  //----------------------------
  validateMilestoneByCustomerToPayFreelancer() {
    if(this.milestoneModel?.validatedByGigCreator){
        TimeOutFunctions.runWithTimeoutAction(3000, ()=>this.errorMsg="already Validated by Customer and transfer have been sent ",()=>this.errorMsg=null)
        return
      }
    console.log("clicked validate Payment ms");
      let msId:number = this.milestoneModel?.id!
      this.milestoneService.ValidateWorkAndPayMs(msId).subscribe( 
          (response:HttpResponse<any>) => {
              console.log(response.status);
              console.log(response.body);
              if(response.status>=200&&response.status<=202){
              
                TimeOutFunctions.runWithTimeoutAction(2000,()=>this.errorMsg="milestone successfully payed And On its way to its customer",
                  ()=> this.router.navigate(['/gigs',this.milestoneModel?.gigId]).then(() => {window.location.reload
                  this.errorMsg=null}))
                return;
               }
              TimeOutFunctions.runWithTimeoutAction(1500,()=>this.errorMsg="bad request no Application Assigned to Ms yet", ()=>this.errorMsg=null)
        } );
  }

  //-------------------
  requestPaymentMilestone() {
    if(this.milestoneModel?.validatedByGigCreator || !this.dateTodayPlus3DaysGreaterOrEqualsOfPaymentDate){
        console.log("too early or already validated");
        return
      }
    console.log("clicked validate Payment ms");
      let msId:number = this.milestoneModel?.id!
      this.milestoneService.requestRefundOrPaymentMs(msId).subscribe( 
          (response:HttpResponse<any>) => {
              console.log(response.status);
              console.log(response.body);
              if(response.status>=200&&response.status<=202){
                TimeOutFunctions.runWithTimeoutAction(2000,()=>this.errorMsg=response.body.message+',        '+'refunded: '+response.body.refundable ,
                  ()=> this.router.navigate(['/gigs',this.milestoneModel?.gigId]).then(() => window.location.reload))
                return;
               }
              TimeOutFunctions.runWithTimeoutAction(1500,()=>this.errorMsg="bad request no Application Assigned to Ms yet ", ()=>this.errorMsg=null)
        } );

  }


  // constructor(){
  //   this.milestoneModel= {
  //    id: 0,
  //   idGig: 0,
  //   idApplicationWon: 0,
  //   goalDescription: "Complete initial design mockups",
  //   dateCreation: new Date("2024-04-20"),
  //   dateStartWorking: new Date("2024-04-21"),
  //   datePayment: new Date("2024-05-05"),
  //   amountPercentage: 50,
  //   amount: 1000,
  //   milestoneDeliverable: [],
  //   isValidatedByGigCreator: false,
  //   isValidatedByFreelancer: false,
  //   isReclamationByGigCreator: false,
  //   walletCustomer: "customer_wallet_address",
  //   walletFreelancer: "freelancer_wallet_address",
  //   ratingCustomer: 9,
  //   ratingFreelancer: 9
  // };
  // }
   
  
  

}
