import {Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Gig } from '../models/gig';
import { ActivatedRoute,  Router } from '@angular/router';
import { GigService } from '../../Services/freelance/gig.service';
import { Subscription } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { FileDocService } from '../../Services/freelance/file-doc.service';
import { FileDoc } from '../models/file-doc';
import { GigUpdateDto } from '../models/gig-update-dto';
import { environment } from '../../../environments/environment';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';

@Component({
  selector: 'app-gig-update',
  templateUrl: './gig-update.component.html',
  styleUrl: './gig-update.component.css'
})
export class GigUpdateComponent implements OnInit, OnDestroy{

  constructor(private activatedRoute:ActivatedRoute ,private route :Router, private gigService:GigService ,
     private fileService:FileDocService ,  private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  //spinner
  spinnerOn:boolean=false
  
  //---enviremnt--Url--File-Download-----
  urlApiDownload:string=environment.apiFileDownload+'/'
  //-----msg-Display----------------
  //----entity-to-fetch--
  gigModel:Gig | null =null;
  //---array-container-of-files-uploaed
  files:FileDoc[]=[]
  //-------Controls--------   
  walletId: FormControl= new FormControl();
  filesControl: FormControl= new FormControl();
  gigForm: FormGroup= new FormGroup({ walletId: this.walletId, files: this.filesControl});
  //http related
  subscribe:Subscription|null=null
 
  //fileRelated progress-Upload isDisabled for disable something 
  progressBar = {progress: -1 , isDisabled:false};

  deleteFile(fileId:number , fileArray:FileDoc[]) {
    console.log("clicked");
    this.fileService.deleteSingleFilFromDatabase(fileId , 'gig').subscribe(
      response => {
        console.log("deleteResponse : ",response.status);
        if(response.status >=200 && response.status <=202)
          this.fileService.deleteFileFromCurrentArray(fileId,fileArray)
      }
    );
  }
 
  selectFiles(event:any){
    this.spinnerOn=true
    this.files=[];
    if(this.gigModel && this.gigModel.files)
      this.fileService.selectFilesReturnArrayFileDoc(event , this.files, this.progressBar )
    this.spinnerOn=false
  }
  
  submitForm($event: Event) {
    if(this.gigModel){
      this.spinnerOn=true


      let gigUpdate:GigUpdateDto={
        id:this.gigModel.id,
        files:this.files,
        walletId: this.walletId.value
      };

      
      if(this.gigModel.id)
        this.gigService.updateGig(this.gigModel.id, gigUpdate).subscribe(response => {
          this.spinnerOn=false
          if(response.status == 200 && response.body)
            this.route.navigate(['/gigs', response.body.id])  
      })
    }
      
    // this.route.navigate(['/gigs',this.gigModel?.id])
  }



  
  
  ngOnInit(): void {
    if(this.gigModel != null)
      return

    //getting Route Param :idGig
    let idGigFromRoute:number = 0 ;
    this.activatedRoute.params.subscribe(params => idGigFromRoute = params['idGig'] as number)
    //fetching data from API
     this.subscribe = this.gigService.getGig(idGigFromRoute).subscribe( (response : HttpResponse<Gig>) =>{
        this.gigModel=response.body ?? null;
        console.log(this.gigModel);
        this.walletId.setValue(this.gigModel?.walletId);
        //disable inputs 
        if(response.body?.gigAssigned ===true){
          this.gigForm.get('walletId')?.disable()
          this.gigForm.get('files')?.disable()
        }

        console.log("ResponseStatus : "+response.status);
    }); 

  }

  ngOnDestroy(): void {
      if(this.subscribe)
        this.subscribe.unsubscribe();
  }
  
  
 
}
