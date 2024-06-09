import { Injectable } from '@angular/core';
import { FileDocService } from './file-doc.service';
import { Milestone } from '../../freelance/models/milestone';
import { catchError, Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { DtoMapperFreelanceService } from './dto-mapper-freelance.service';
import { ActivatedRoute } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class MilestoneService {
  constructor(private route: ActivatedRoute , private http: HttpClient  , private serviceFile:FileDocService) { }

   

   //--------admin--------
   getMilestones():Observable<HttpResponse<Milestone[]>>{  //http://localhost:9090/gigs/milestones
    let url=`http://localhost:9090/gigs/milestones/admin`
    return this.http.get<Milestone[]>(url , {observe:'response'})
      .pipe(
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }));
   }
   //--------My Milestones--------
   getMyMilestones():Observable<HttpResponse<Milestone[]>>{  //http://localhost:9090/gigs/milestones/my
    let url=`http://localhost:9090/gigs/milestones/my`
    return this.http.get<Milestone[]>(url , {observe:'response'})
      .pipe(
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }));
   }
   //--------My Assigned Milestones--------
   getMyAssignedMilestones():Observable<HttpResponse<Milestone[]>>{  //http://localhost:9090/gigs/milestones/my
    let url=`http://localhost:9090/gigs/milestones/myAssigned`
    return this.http.get<Milestone[]>(url , {observe:'response'})
      .pipe(
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }));
   }
   //--------
   getMilestone(idMs:number):Observable<HttpResponse<Milestone>>{
        let url:string =`http://localhost:9090/gigs/milestones/${idMs}`

        return this.http.get<Milestone>(url , {observe:'response'})
        .pipe( 
          catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }

   //--------Update--files--Ms-----
   updateFileMilestoneByFreelancer(idMs:number, Milestone:Milestone):Observable<HttpResponse<Milestone>>{
      let url:string =`http://localhost:9090/gigs/milestones/${idMs}/application/files`
      return this.http.put<Milestone>(url, Milestone, {observe:'response'})
      .pipe(
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }



   //---------------------------------------Non-Crud-------------------------------
   //------Validate---Work-Done--Ms----
    validateWorkDoneByFreelancer(idMs:number):Observable<HttpResponse<any>>{
        let url:string =`http://localhost:9090/gigs/milestones/${idMs}/application/validate`

        return this.http.get<any>(url , {observe:'response'})
        .pipe( 
          catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }

   //-----Activate-Ms------
    activateMs(idMs:number):Observable<HttpResponse<any>>{
        let url:string =`http://localhost:9090/gigs/milestones/${idMs}/activate`

        return this.http.get<any>(url , {observe:'response'})
        .pipe( 
          catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }
   //----Report--ms-------
   reportMsByCustomer(idMs:number):Observable<HttpResponse<any>>{
        let url:string =`http://localhost:9090/gigs/milestones/${idMs}/report`

        return this.http.get<any>(url , {observe:'response'})
        .pipe( 
          catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }

   //-----requestPaymentOrRefund----------
   requestRefundOrPaymentMs(idMs:number):Observable<HttpResponse<any>>{
        let url:string =`http://localhost:9090/gigs/milestones/${idMs}/refund`

        return this.http.get<any>(url , {observe:'response'})
        .pipe( 
          catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }

   //-----validateByCustomer_Ms----
   ValidateWorkAndPayMs (idMs:number):Observable<HttpResponse<any>>{
        let url:string =`http://localhost:9090/gigs/milestones/${idMs}/validate`

        return this.http.get<any>(url , {observe:'response'})
        .pipe( 
          catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }






  

}
 