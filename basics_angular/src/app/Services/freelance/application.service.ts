import { inject, Injectable } from '@angular/core';
import { FileDocService } from './file-doc.service';
import { Application } from '../../freelance/models/application';
import { catchError, map, Observable } from 'rxjs';
import { Gig } from '../../freelance/models/gig';
import { HttpClient, HttpRequest, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { ApplicationUpdate } from '../../freelance/models/application-update';
import { environment } from '../../../environments/environment';
import { response } from 'express';
import { DtoMapperFreelanceService } from './dto-mapper-freelance.service';
import { url } from 'inspector';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private route: ActivatedRoute , private http: HttpClient ,private mapperDto:DtoMapperFreelanceService) { }
  baseGigspath=environment.apiUrl+"/gigs"
  appsPath="/applications"
  adminPath="/admin"



   //--------Array-all-applications for admin
  getApplicationsForAdmin():Observable<HttpResponse<Application[]>>{  //http://localhost:9090/gigs/applications/admin
    return this.http.get<Application[]>(this.baseGigspath+this.appsPath+this.adminPath , {observe:'response'})
      .pipe(
        map( response =>{
            response.body?.forEach( app => this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(app));
            return response;
        }),
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }));
   }

   //-----------
    //--------getOneGig's all related application
   getGigIsApplications(idGig:number):Observable<HttpResponse<Application[]>>{//http://localhost:9090/gigs/idGig/applications
    return this.http.get<Application[]>(this.baseGigspath+'/'+idGig+this.appsPath , {observe:'response'})
      .pipe( map( response =>{
            response.body?.forEach( app => this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(app));
            return response;
        }),
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }
   //---------OneApplication---
   getApplication(id:number):Observable<HttpResponse<Application>>{
        return this.http.get<Application>(this.baseGigspath+this.appsPath+'/'+id , {observe:'response'})
        .pipe( 
          map( response =>{
            if(response.body)
              this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(response.body);
            return response;
        }),
          catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }
   //--------
  
    // http://localhost:9090/gigs/4/applications expecting Gig as return Type
    addApplication(idGig:number ,application: Application): Observable<HttpResponse<Gig>> {
      return this.http.post<Gig>(this.baseGigspath+'/'+idGig+this.appsPath,application , {observe : 'response'})
      .pipe(
        map( response =>{
            if(response.body && response.body.applications)
             response.body.applications.forEach( app => this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(app));
            return response;
        }),
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
      }
   //--------http://localhost:9090/gigs/applications/idApp
   updateApplication(id:number , application:ApplicationUpdate):Observable<HttpResponse<Application>>{
      return this.http.put<Application>(this.baseGigspath+this.appsPath+'/'+id,application, {observe:'response'})
      .pipe(
        map( response =>{
          if(response.body)
            this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(response.body);  
          return response;
        }
        ),
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }

   //--------
   deleteApplication(id:number):Observable<HttpResponse<string>>{
      return this.http.delete<any>(this.baseGigspath+this.appsPath+'/'+id , {observe: 'response'})
      .pipe(catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }



   choseApplicationWon(idGig:number , idApp:number):Observable<HttpResponse<Gig>>{
     let apiurl:string=`http://localhost:9090/gigs/${idGig}/milestones/application/${idApp}/chosen`
     console.log(apiurl);
     return this.http.get<any>( apiurl, {observe : 'response'})
      .pipe(
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
    }

   

}
 