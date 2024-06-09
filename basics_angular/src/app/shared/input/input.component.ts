import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import{ FormControl } from '@angular/forms';


@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrl: './input.component.css'
})
export class InputComponent implements OnChanges {
  @Input() placeholderString: string = ''
  @Input() typeInput: string = ''
  @Input() valueData: any = ''
  @Input() nameInput: string = ''
  @Input() classCustom: string =''
  //used if u wish to use the input as file upload with many or other inputs which accepts multiple
  @Input() isMultiple:boolean = false
  //formControl input
  @Input() control :FormControl = new FormControl(); // new FormControl() only to make TypescriptError Disappear will not be used really
  @Input() idInput: any;


  ngOnChanges(changes: SimpleChanges): void {
  }

}
