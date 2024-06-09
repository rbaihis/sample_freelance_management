import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Application } from '../models/application';
import { environment } from '../../../environments/environment';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from '../../Services/freelance/application.service';
import { FileDoc } from '../models/file-doc';
import { Subscription } from 'rxjs';
import { TimeOutFunctions } from '../../Services/utility/time-out-functions';
import { FileDocService } from '../../Services/freelance/file-doc.service';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-application-update',
  templateUrl: './application-update.component.html',
  styleUrl: './application-update.component.css'
})
export class ApplicationUpdateComponent implements OnInit {



  constructor(private activatedRoute:ActivatedRoute ,private serviceApp:ApplicationService  , private route:Router, private fileService:FileDocService ,
     private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  //spinner
  spinnerOn:boolean=false
  //-----errorMsg------------
  msgFailDisplay:string|null=null
  //-------http-related--------
  applicationSubscription:Subscription|null=null;
  //-----data------------
  applicationModel:Application|null=null
  //---- ProgressBar---------
  progressBar= {progress: -1 , isDisabled: false}
  //-----url---download-----Delete--------------
  urlApiDownload:string=environment.apiFileDownload+'/'
  //------fileUpload---------------
  files:FileDoc[]=[]
  //-------forms related----------
  bid: FormControl = new FormControl('');
  walletId: FormControl = new FormControl('');
  extraCost: FormControl = new FormControl('');
  filesControl: FormControl = new FormControl('');
  updateApplicationForm: FormGroup =new FormGroup({});
  
  
  
  
  selectFile(event: Event) {
    this.spinnerOn=true
    console.log("not yet implemented");
    this.files=[] // to get rid of old files uploaded
    this.fileService.selectFilesReturnArrayFileDoc(event, this.files,this.progressBar)
    this.spinnerOn=false
  }
  
  ngOnInit(): void {
    //load data only if this component is called Directly
    let idAppFromRoute:any;
    this.activatedRoute.params.subscribe(param => idAppFromRoute= param['idApp']);
    if(!this.applicationModel){
      this.applicationSubscription = this.serviceApp.getApplication(idAppFromRoute)
      .subscribe( 
        (response:HttpResponse<Application>) => {
          this.applicationModel = response.body
          console.log(response.body);
          this.walletId.setValue(this.applicationModel?.walletId)
          this.extraCost.setValue(this.applicationModel?.extraCost)
          this.bid.setValue(this.applicationModel?.bid)
        } );
        
      }
    }
    
    
    submitForm(event: Event) {
      event.preventDefault();
      console.log("not yet implemented");
      
      if(this.applicationModel==null)
        return;

      this.spinnerOn=true
      
      this.applicationModel.bid=this.bid.value
      this.applicationModel.extraCost=this.extraCost.value
      this.applicationModel.walletId=this.walletId.value
      this.applicationModel.files=this.files
      
      this.applicationSubscription = this.serviceApp.updateApplication(this.applicationModel.id!,this.applicationModel).subscribe(
        response => {
          if(response.status>=200 && response.status<=202){
            this.spinnerOn=false
            this.route.navigate(['/gigs/applications',this.applicationModel?.id])
            return;
          }

          this.spinnerOn=false
          TimeOutFunctions.runWithTimeoutAction(4000,
            ()=> {this.msgFailDisplay='Not allowed to Delete this Gig' ; } ,
            ()=> this.msgFailDisplay=null
          ) 
          
        }
      );
      
    }
    
    
    deleteFile(fileId: number,fileArray: FileDoc[]) {
    console.log("clicked");

        this.fileService.deleteSingleFilFromDatabase(fileId , 'application').subscribe(
          response => {
            console.log("deleteResponse : ",response.status);
            if(response.status >=200 && response.status <=202)
              this.fileService.deleteFileFromCurrentArray(fileId,fileArray)
          }
        );
    }
    
  }
  
  