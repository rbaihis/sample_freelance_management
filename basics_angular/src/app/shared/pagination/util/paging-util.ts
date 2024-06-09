import { HttpClient, HttpResponse } from "@angular/common/http";
import { catchError, map, Observable } from "rxjs";
import { PageJPAReleventData } from "../entity/page-jparelevent-data";
import { Inject, Injectable, Injector } from "@angular/core";
import { response } from "express";

@Injectable({
  providedIn: 'root'
})
export class PagingUtil {
    
    static  {
        if(!PagingUtil.http){
            const injector = Injector.create({ providers: [HttpClient] }); // Create injector with HttpClient
            PagingUtil.http = injector.get(HttpClient); 
        }
            
    }

    static http:HttpClient;


    static pageNumber: any;
    static sort: any;
    static basePath:string = "basepath"


    (data: PageJPAReleventData<T>) => Observable<PageJPAReleventData<T>>
): Observable<HttpResponse<PageJPAReleventData<T>>> 


    static getPagesHttpResponse<T>(  mapMethod: (data: PageJPAReleventData<T>) => Observable<PageJPAReleventData<T>> ): Observable<HttpResponse<PageJPAReleventData<T>>>{

      let url:string=`${this.basePath}?page=${this.pageNumber}${this.sort ? '&sort='+this.sort:'' }`
      
      return this.http.get<PageJPAReleventData<T>>(url,{ observe: 'response' })
      .pipe( 
        //------to-manipulate data on return one time implementation instead of doing it in the callee function on subscription
        map( 
             response => mapMethod
        )//endForEach  
         ,
        catchError(error => {
          console.error('Error:', error);
          throw error; 
        })
        
    }

    

}
