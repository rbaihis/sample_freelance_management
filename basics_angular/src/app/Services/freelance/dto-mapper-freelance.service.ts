import { Injectable } from '@angular/core';
import { Application } from '../../freelance/models/application';

@Injectable({
  providedIn: 'root'
})
export class DtoMapperFreelanceService {

  constructor() { }
  //Applicatio,method for converting csv string to array of strings for display :
  mapApplicationReferenceLinksToRefernceLinksDto(app:Application){
    app.workReferenceLinksDto = app.workReferenceLinks?.split(',').filter(link=>link.trim() !=='')
  }
}
