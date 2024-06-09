import { Component, Input } from '@angular/core';
import{ FormControl } from '@angular/forms';

@Component({
  selector: 'app-text-area',
  templateUrl: './text-area.component.html',
  styleUrl: './text-area.component.css'
})
export class TextAreaComponent {
 @Input()   placeholderString: string =''
 @Input()   rowsCount: number = 4 ;
 @Input()   maxLengthCount: number = 800 ;
 @Input()   idInput:any ;
  
  //formControl input
  @Input() control:FormControl = new FormControl(); // new FormControl() only to make TypescriptError Disappear will not be used really

}
