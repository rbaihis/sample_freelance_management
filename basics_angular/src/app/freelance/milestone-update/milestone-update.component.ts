import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MilestoneService } from '../../Services/freelance/milestone.service';
import { environment } from '../../../environments/environment';
import { Milestone } from '../models/milestone';
import { Subscription } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { FileDoc } from '../models/file-doc';
import { FileDocService } from '../../Services/freelance/file-doc.service';
import { TimeOutFunctions } from '../../Services/utility/time-out-functions';
import { response } from 'express';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-milestone-update',
  templateUrl: './milestone-update.component.html',
  styleUrl: './milestone-update.component.css'
})
export class MilestoneUpdateComponent implements OnInit {

  constructor(private router:Router , private milestoneService:MilestoneService, private activateRoute:ActivatedRoute,
    private fileService:FileDocService ,private fb:FormBuilder ,  private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  } 
  //userId current
  currentUserId : number |null = null;
  //spinner
  spinnerOn:boolean=false

  // @Input() showDescription: boolean= false
  // @Input() showDetailMilestoneComponent:boolean=false
  //---http--
  msSubscribtion:Subscription|null=null;
  //----data---
  @Input() milestoneModel: Milestone | null =null;
  //----error-msg-----
   errorMsg:string|null=null
  //----files-upload
  files:FileDoc[]=[]
  //---- ProgressBar---------
  progressBar= {progress: -1 , isDisabled: false}
  //------gigId-- if passed
  @Input() gigmodelId?:number|null = null
  //apiUrl for Download files
  pathToDownload:string = environment.apiFileDownload+'/'

  milestoneDeliverable: FormControl = new FormControl('');
  milestoneForm: FormGroup = this.fb.group({
    milestoneDeliverable:this.milestoneDeliverable
  })

  
  ngOnInit(): void {
      let idMsFromRoute:any;
     this.activateRoute.params.subscribe(param => idMsFromRoute= param['idMs']);
    if(this.milestoneModel == null){
      this.msSubscribtion = this.milestoneService.getMilestone(idMsFromRoute)
        .subscribe( 
          (response:HttpResponse<Milestone>) => {
            this.milestoneModel = response.body
            console.log(response.body);
            if(this.milestoneModel != null)
               this.gigmodelId!=this.milestoneModel.gigId
        } );
    }
  }


  selectFile(myEvent:Event) {
    console.log("selecting files");
    this.files=[] // to get rid of old files uploaded
    this.fileService.selectFilesReturnArrayFileDoc( myEvent, this.files,this.progressBar)
  }

  //-------------------------
  submitForm() {
    if(this.selectFile.length ==0 )
      return
    console.log("submittingForm");
    console.log(this.milestoneModel?.milestoneDeliverable);
    let msId:number= this.milestoneModel?.id!
    this.milestoneModel?.milestoneDeliverable!=this.files
    this.milestoneService.updateFileMilestoneByFreelancer(msId,this.milestoneModel!).subscribe(
      response=>{
        console.log(response.status);
        console.log(response.body);
        if(response.status>=200 && response.status<=202){
           this.milestoneModel?.milestoneDeliverable!=response.body?.milestoneDeliverable
           TimeOutFunctions.runWithTimeoutAction(3000,()=>this.errorMsg="files deliverable successfuly Added",
                  ()=> {
                    this.errorMsg=null
                    this.milestoneModel!.milestoneDeliverable=[]
                  } )
                return;
        }
           TimeOutFunctions.runWithTimeoutAction(3000,()=>this.errorMsg="MileStone is Disactivate , or You Passed Deadline",
                  ()=> {
                    this.errorMsg=null 
                      this.milestoneModel!.milestoneDeliverable=[]
                  })


    })

  }
  //---------------
  validateWorkDone(event:Event) {
    event.preventDefault
    let msId= this.milestoneModel!.id
    this.spinnerOn=true
    this.milestoneService.validateWorkDoneByFreelancer(msId!).subscribe(response=>{
      this.spinnerOn=false
        console.log(response.status);
         if(response.status>=200 && response.status<=202){
           TimeOutFunctions.runWithTimeoutAction(2000,()=>this.errorMsg="Thx for Validating ",
                  ()=>  this.milestoneModel!.milestoneDeliverable=[])
                return;
          }
           TimeOutFunctions.runWithTimeoutAction(3000,()=>this.errorMsg="MileStone Can't be activated",
                  ()=> {this.errorMsg=null 
                      this.milestoneModel!.milestoneDeliverable=[]
                  })
        }
    )

  }

}
