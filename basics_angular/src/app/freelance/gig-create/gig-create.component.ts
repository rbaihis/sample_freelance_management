import { Component, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { Skill } from '../models/skill';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Gig } from '../models/gig';
import { GigService } from '../../Services/freelance/gig.service';
import { FileDocService } from '../../Services/freelance/file-doc.service';
import { FileDoc } from '../models/file-doc';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-gig-create',
  templateUrl: './gig-create.component.html',
  styleUrl: './gig-create.component.css'
})



export class GigCreateComponent implements OnInit , OnDestroy{
  public constructor(private fb: FormBuilder , private gigService:GigService ,private fileService:FileDocService ,
    private route:Router  ,  private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  
  
  //data & http-Subscriptio
  gig:Gig={ skills:[] , files:[] , milestones:[]}
  skills: string | undefined;
  mySubscription : Subscription | undefined;
  //data for file uploads Upload Ok 
  progressBar = {progress: -1 , isDisabled:false};
  //ReactiveForm Related
  stringGigStartsDate = new Date(Date.now() + 1 * 24 * 60 * 60 * 1000).toISOString().slice(0,10)
  stringGigEndDate = new Date(Date.now() + 10 * 24 * 60 * 60 * 1000).toISOString().slice(0,10)
  title:          FormControl = new FormControl('',[Validators.required]);
  description:    FormControl = new FormControl('',[Validators.required]);
  minPrice:       FormControl = new FormControl('',[Validators.required]);
  maxPrice:       FormControl = new FormControl('',[Validators.required]);
  projectStart:   FormControl = new FormControl(this.stringGigStartsDate,[Validators.required]);
  projectDeadline: FormControl = new FormControl(this.stringGigEndDate,[Validators.required]);
  files: FormControl = new FormControl('');
  walletId: FormControl = new FormControl('',[Validators.required]);
  policy: FormControl = new FormControl('');
  milestoneindex:number = -1
  milestonesFormArray:FormArray<FormGroup> = new FormArray<FormGroup>([ 
    this.fb.group(
      {
        goalDescription : ['' , [Validators.required]],
        dateStartWorking : [this.stringGigStartsDate,[Validators.required]] ,
        datePayment : [this.stringGigEndDate,[Validators.required]],
        amountPercentage : ['100',[Validators.required]],
      }) 
  ],Validators.required)
  gigForm: FormGroup = new FormGroup({
    title:  this.title,        
    description: this.description,
    minPrice: this.minPrice,
    maxPrice:   this.maxPrice,
    projectStart: this.projectStart,
    projectDeadline: this.projectDeadline,
    walletId: this.walletId,
    files: this.files, //only interested in validator hee
    policy: this.policy,
    milestones: this.milestonesFormArray
  }) ;
  
  // upload file and fill gigModel.Files[]
  fileUpload(event:Event){
      //initialize files to empty array:
      this.gig.files=[]
      this.fileService.selectFilesReturnArrayFileDoc(event , this.gig.files! , this.progressBar)
      console.log('isDisabled if true Indicates u shout prevent the user from submitting form untill upload is done : ', this.progressBar.isDisabled);
  }

  submitForm(){
    if(!!!this.currentUserId)
        this.route.navigate(['login'])
    
    console.log(this.gigForm.value);
    console.log((this.gigForm.get('milestones') as FormGroup).value);

    //to be implemented Later
    // for( let skillName of this.skills.values() )
    //     this.gig.skills?.push( {skill:skillName}) 

    //initializeMilestones:
    this.gig.title = this.title.value  
    this.gig.description = this.description.value 
    this.gig.minPrice = this.minPrice.value   
    this.gig.maxPrice = this.maxPrice.value
    this.gig.projectStart = this.projectStart.value 
    this.gig.projectDeadline = this.projectDeadline.value 
    this.gig.walletId=  this.walletId.value 
    // gig.files = this.files.value    
    //initialize milestone in case of error
    this.gig.milestones=[]
    for(let ms of this.milestonesFormArray.controls) 
      this.gig.milestones?.push({
        goalDescription :     ms.get('goalDescription')?.value ,
        dateStartWorking :    ms.get('dateStartWorking')?.value   ,
        datePayment :         ms.get('datePayment')?.value  ,
        amountPercentage :    ms.get('amountPercentage')?.value ,
        milestoneDeliverable: []
      })

      console.log("Gig :" , this.gig);
      this.mySubscription = this.gigService.addGig(this.gig).subscribe(
        response => {
          console.log(response.body as Gig);
          if(response.status == 200 || response.status==201  && response.body !== null )
             this.route.navigate(['/gigs',response.body?.id])


        }
      )
  }
  

  ngOnInit(): void {
      //requires Login
      //--end-requires-login

  }

  ngOnDestroy():void{
    if(this.mySubscription != undefined)
      this.mySubscription.unsubscribe()
  }


  //-----private-internal-usage
     // Getter to access the milestones form array
   getFormControl(control : AbstractControl): FormControl {
    return control as FormControl;
  }

  
  removeMilestone() {
    if(this.milestonesFormArray.length>1)
      { 
        let lastIndex = this.milestonesFormArray.length -1
        this.milestonesFormArray.removeAt(lastIndex)
        this.milestoneindex--
      }

    //  projectStart: this.projectStart,
    // projectDeadline: this.projectDeadline,
    //-------
    //  dateStartWorking :    ms.get('dateStartWorking')?.value   ,
    //     datePayment :         ms.get('datePayment')?.value  ,
    //  amountPercentage:
      if(this.milestonesFormArray.length===1){
        let stringStart= this.gigForm.get('projectStart')?.value
        let stringEnd= this.gigForm.get('projectDeadline')?.value
        //set milestone amount percentage to 100
        this.milestonesFormArray.controls[0].get('dateStartWorking')?.setValue(stringStart,[Validators.required]);
        this.milestonesFormArray.controls[0].get('datePayment')?.setValue(stringEnd,[Validators.required]);
        this.milestonesFormArray.controls[0].get('amountPercentage')?.setValue("100",[Validators.required]);
        //set Milestone StartProject to start Date
        //set mileSton end project to end Date
        console.log(125);
      }
 
  }
  //----------
  addMilestone() {

    let percentage=100;
    this.milestonesFormArray.push( this.fb.group(
      {
        goalDescription : ['' , [Validators.required]],
        dateStartWorking : [null,[Validators.required]] ,
        datePayment : [null,[Validators.required]],
        amountPercentage : [percentage,[Validators.required]],
      })
    );

    
    if(this.milestonesFormArray.length>1){
      let startDateString=this.gigForm.get('projectStart')?.value
      let paymentDate=this.gigForm.get('projectDeadline')?.value
      percentage =  Math.round(100/this.milestonesFormArray.length) 
        for( let i =0 ; i<this.milestonesFormArray.length ; i++)
          { 
            this.milestonesFormArray.controls[i].get('amountPercentage')?.setValue(percentage,[Validators.required])
            this.milestonesFormArray.controls[i].get('dateStartWorking')?.setValue(null,[Validators.required])
            this.milestonesFormArray.controls[i].get('datePayment')?.setValue(null,[Validators.required])

            if(i==0){
              this.milestonesFormArray.controls[i].get('dateStartWorking')?.setValue(startDateString,[Validators.required])
              this.milestonesFormArray.controls[i].get('datePayment')?.setValue(null,[Validators.required])
              continue
            }
            if(i==this.milestonesFormArray.length-1){
              this.milestonesFormArray.controls[i].get('dateStartWorking')?.setValue(null,[Validators.required])
              this.milestonesFormArray.controls[i].get('datePayment')?.setValue(paymentDate,[Validators.required])
              continue
            }
          }
    }

    this.milestoneindex++
  }

  //-----------------
  changeDateStartGigs(){
    // let inputFromDate = value;
    let stringStart= this.gigForm.get('projectStart')?.value
    let stringEnd= this.gigForm.get('projectDeadline')?.value

    if(this.milestonesFormArray.length==1){
      this.milestonesFormArray.controls[0].get('dateStartWorking')?.setValue(stringStart,[Validators.required]);
      this.milestonesFormArray.controls[0].get('datePayment')?.setValue(stringEnd,[Validators.required]);
      this.milestonesFormArray.controls[0].get('amountPercentage')?.setValue("100",[Validators.required]);
    }

    if(this.milestonesFormArray.length>1){
        let lastIndex = this.milestonesFormArray.length-1
        this.milestonesFormArray.controls[lastIndex].get('datePayment')?.setValue(stringEnd,[Validators.required]);
        this.milestonesFormArray.controls[0].get('dateStartWorking')?.setValue(stringStart,[Validators.required]);
    }

  }


 onInputTouched() {
  // Do something with the changed value
  console.log("hello"); 
}

}
