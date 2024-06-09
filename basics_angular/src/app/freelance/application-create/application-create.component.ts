import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { FormGroup, FormControl,Validator, FormArray, AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { FileDocService } from '../../Services/freelance/file-doc.service';
import { FileDoc } from '../models/file-doc';
import { Application } from '../models/application';
import { ApplicationService } from '../../Services/freelance/application.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { application } from 'express';
import { ApplicationValidator } from './application-validator';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';


@Component({
  selector: 'app-application-create',
  templateUrl: './application-create.component.html',
  styleUrl: './application-create.component.css',
})
export class ApplicationCreateComponent implements OnInit{

  constructor(public fileService:FileDocService , public fb:FormBuilder , public appService:ApplicationService , private route:Router ,
    private userConnected:FooConnectedUserService , private activatedRoute:ActivatedRoute) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  

  @Input() idGig?:number= 0 ;

  //form-Group declaration
  addApplicationForm:FormGroup=this.fb.group({});
  //form-control declaration
  description = new FormControl('',[Validators.required]);
  bid = new FormControl('',[Validators.required]);
  extraCost = new FormControl('',[Validators.required]);
  workRefernces:FormArray = new FormArray<FormControl>([]);
  questions = new FormControl('');
  policy = new FormControl("",[Validators.required]);
  freelancerProfile: FormControl<any> = new FormControl('',[Validators.required,ApplicationValidator.mustBeLink]);
  walletId: FormControl<any>= new FormControl('',[Validators.required]);
  //fileUploads 
  files:FileDoc[] = [];
  //fileRelated progress-Upload isDisabled for disable something 
  progressBar = {progress: -1 , isDisabled:false};
  
  selectFiles(event:any){
    this.files=[] // to get rid of old files uploaded
    this.fileService.selectFilesReturnArrayFileDoc(event, this.files,this.progressBar)
  }


  ngOnInit(): void {
  //   this.activatedRoute.paramMap.subscribe(params => {
  //   this.idGig = +params.get('idGig')!; // Convert to number using unary plus
  //   console.log('idGig as a number:', this.idGig);
  // });

    //requires Login
      if(!!!this.currentUserId)
        this.route.navigate(['login'])
      //--end-requires-login

    this.addApplicationForm= this.fb.group({
      // this is a form array
       workRefernces: this.workRefernces
    })
    // Manually add existing FormControl instances to the FormGroup
    this.addApplicationForm.addControl('description', this.description);
    this.addApplicationForm.addControl('bid', this.bid);
    this.addApplicationForm.addControl('extraCost', this.extraCost);
    this.addApplicationForm.addControl('questions', this.questions);
    this.addApplicationForm.addControl('policy', this.policy);
    this.addApplicationForm.addControl('walletId', this.walletId);
    this.addApplicationForm.addControl('freelancerProfile', this.freelancerProfile);
  }

  
  submitForm(){ 
    console.log(this.addApplicationForm.value);
    console.log(this.files);

    //convert array of string into 1-csv-string
    let wr:string=''
    this.workRefernces.controls.forEach(control => { wr += (control.value+",") });
    const application:Application= {
      description: this.addApplicationForm.get('description')?.value ,
      bid: this.addApplicationForm.get('bid')?.value,
      extraCost: this.addApplicationForm.get('extraCost')?.value,
      workReferenceLinks: wr,
      questions: this.addApplicationForm.get('questions')?.value,
      files: this.files,
      walletId: this.addApplicationForm.get('walletId')?.value,
      freelancerProfile: this.addApplicationForm.get('freelancerProfile')?.value
    }
    console.log(application);
    
    this.appService.addApplication( this.idGig as number , application)
    .subscribe( 
      response => {
        console.log("response status create gig: ",response.status);
        console.log("data received create gig :\n"+response.body)
        if(response.status>=200 && response.status <= 202)
          this.route.navigate(['gigs',response.body?.id])
            .then(() => window.location.reload());
       }
    )
  }
  

  


  
  //add-remove-Workreference 
  addWorkRefernce(){
    if(this.workRefernces.length<10)
      {
        this.workRefernces.push(new FormControl("",[Validators.required,ApplicationValidator.mustBeLink]))
        console.log("adding......");
      }
    console.log("lenght-Array:"+this.workRefernces.length);
  }
  //----
  removeWorkRefernce() {
    const lastIndex= this.workRefernces.length-1;
    if (lastIndex>=0) {
      this.workRefernces.removeAt(lastIndex);
      console.log("removing");
    }
    console.log("lenght-Array:"+this.workRefernces.length);
  }

 
  //used to cast AbstractControl To FormControl if needed
  getFormControl(control: AbstractControl): FormControl {
    return control as FormControl;
  }

  
}


  
  
  


