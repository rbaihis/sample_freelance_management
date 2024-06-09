import { Injectable } from '@angular/core';
import { FileDocService } from './file-doc.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Gig } from '../../freelance/models/gig';
import { catchError, map, Observable } from 'rxjs';
import { response } from 'express';
import { environment } from '../../../environments/environment';
import { DtoMapperFreelanceService } from './dto-mapper-freelance.service';
import { GigUpdateDto } from '../../freelance/models/gig-update-dto';
import { url } from 'inspector';
import { PageJPAReleventData } from '../../shared/pagination/entity/page-jparelevent-data';

@Injectable({
  providedIn: 'root'
})
export class GigService {

  constructor( private serviceFile:FileDocService , private http: HttpClient , private mapperDto:DtoMapperFreelanceService) {
   }
  baseGigspath=environment.apiUrl+"/gigs"

   //--------No-Pagination--------------
   getGigs():Observable<HttpResponse<Gig[]>>{
      return this.http.get<Gig[]>(this.baseGigspath,{ observe: 'response' })
      .pipe( map( response =>{
            if(response.body)
              response.body.forEach( gig => {
                if(gig.applications)
                  gig.applications.forEach( app => this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(app))
            })
            return response;
        }) ,
        catchError(error => {
          console.error('Error:', error);
          //redirect to error html page and print Error instead of throw Error 
          throw error; 
        }))
   }

   //--------
   getGigsPaginated(pageNumber:number,sort:string , minPrice:string , maxPrice:string):Observable<HttpResponse<PageJPAReleventData<Gig>>>{
      let searchVars = `${ minPrice !== '' &&  minPrice !== null ? '&minPrice='+minPrice : '' }${ maxPrice != '' &&  maxPrice !== null ? '&maxPrice='+maxPrice : ''}`
      let url:string=`${this.baseGigspath}/paginated?page=${pageNumber}${sort? '&sort='+sort:'' }${searchVars}`
      console.log(url);
      return this.http.get<PageJPAReleventData<Gig>>(url,{ observe: 'response' })
      .pipe( 
        //------to-manipulate data on return one time implementation instead of doing it in the callee function on subscription
        map( response =>{
            if(response.body)
              response.body.content.forEach( gig => {
                if(gig.applications)
                  // this to map a string of csv type to array of string for display reasons :
                  gig.applications.forEach( app => this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(app))
            })//endForEach
            return response;
        }) ,
        catchError(error => {
          console.error('Error:', error);
          //redirect to error html page and print Error instead of throw Error 
          throw error; 
        }))
   }

   //--------
   getGig(id:number):Observable<HttpResponse<Gig>>{
    return this.http.get<Gig>(this.baseGigspath+'/'+id , { observe : 'response'})
      .pipe( map( response =>{
            if(response.body && response.body.applications)
              response.body.applications.forEach( app => this.mapperDto.mapApplicationReferenceLinksToRefernceLinksDto(app));
            return response;
        }),
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))

   }
   //--------
   addGig(gig:GigUpdateDto):Observable<HttpResponse<Gig>>{
      return this.http.post<Gig>(this.baseGigspath, gig, {observe:'response'})
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
   //--------
   updateGig(id:number,gigUpdateDto:GigUpdateDto):Observable<HttpResponse<Gig>>{
      return this.http.put<Gig>(this.baseGigspath+'/'+id,gigUpdateDto , {observe:'response'})
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

   //--------
   deleteGig(id:number):Observable<HttpResponse<any>>{
      return this.http.delete<any>(this.baseGigspath+'/'+id , {observe:'response'} )
      .pipe(catchError(error => {
          console.error('Error:', error);
          throw error; 
        }))
   }
}
 