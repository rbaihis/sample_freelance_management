import { Component, Input } from '@angular/core';
import{ FormControl } from '@angular/forms';


@Component({
  selector: 'app-select-input',
  templateUrl: './select-input.component.html',
  styleUrl: './select-input.component.css'
})
export class SelectInputComponent {
    @Input() isMultiple:boolean = false
    @Input() optionsSelect:string[]=[]
    @Input() idInput:any;


  //formControl input
  @Input() control:FormControl = new FormControl(); // new FormControl() only to make TypescriptError Disappear will not be used really

}
