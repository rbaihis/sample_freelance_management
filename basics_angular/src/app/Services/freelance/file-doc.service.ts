import { Injectable } from '@angular/core';
import { FileDoc } from '../../freelance/models/file-doc';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { error } from 'console';
import { subscribe } from 'diagnostics_channel';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
export class FileDocService {

  constructor(private http:HttpClient) { }


  counterUploded:number = 0;
  
  selectFilesReturnArrayFileDoc(event: any , fileArrayToFill:FileDoc[] , progressBar:{progress:number , isDisabled:boolean}): void{
    let selectedFiles: FileList = event.target.files; ;
    console.log("selectedFiles form event.target.files : "+selectedFiles);
    if (selectedFiles) {
      //check if mistakenly forget to initialize or fileArray when passing it
      if(fileArrayToFill === null || fileArrayToFill === undefined)
        fileArrayToFill = []

      for( let i=0; i<selectedFiles.length; i++){
        const file: File | null = selectedFiles.item(i);
      
        if (file) {
          //declare arrray of FileDocModel
          const fileDoc:FileDoc={};   

          const reader = new FileReader();
          reader.readAsDataURL(file); // Read the file as a data URL (base64)

          //process Action (while reading -in progress)
          reader.onprogress = (e: ProgressEvent) => {
            if (e.lengthComputable) {
              progressBar.progress = Math.round((e.loaded / e.total) * 100);
              progressBar.isDisabled = true
              console.log("Progress:",progressBar);
            }
          };
          
          //Process Action (completely read)
          reader.onload = (e: any) => {

            // Extract fileName Withoutextension and extention
            const lastDotIndex: number = file.name.lastIndexOf('.');
            if (lastDotIndex !== -1) {
              fileDoc.name = file.name.substring(0, lastDotIndex); // Filename without extension
              fileDoc.extension = file.name.substring(lastDotIndex + 1); // File extension
              fileDoc.isSaved=false//
            }
            fileDoc.content=e.target.result.split(',')[1]; // Extract base64 string from data URL
            fileArrayToFill.push(fileDoc)
            progressBar.isDisabled=false
            
          };
          
        }
      }
    }

  }
  


  public deleteSingleFilFromDatabase(idFile:number , entityName:string ):Observable<HttpResponse<any>>{

    const url = `${environment.apiFileDelete}/${idFile}?entity=${entityName}`
    console.log(url);
    return this.http.delete<any>(url , { observe: 'response' }).pipe(
        catchError(error => {
          console.log(error);
          throw error;
        })
    )
  }  
  
 
  public deleteFileFromCurrentArray(fileId:number , arrayFiles:FileDoc[]){
     if(arrayFiles){
        const index = arrayFiles.findIndex(file => file.id == fileId);
        if (index !== -1) {
          arrayFiles.splice(index, 1);
        } else {
          console.error('File not found:', fileId);
        }
    } 


  }


}
 