import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputComponent } from './input/input.component';
import { RouterModule } from '@angular/router';
import { TextAreaComponent } from './text-area/text-area.component';
import { ModalComponent } from './modal/modal.component';
import { SelectInputComponent } from './select-input/select-input.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ProgressBarComponent } from './progress-bar/progress-bar.component';
import { PaginationComponent } from './pagination/pagination.component';
import { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';


@NgModule({
  declarations: [
    InputComponent,
    TextAreaComponent,
    ModalComponent,
    SelectInputComponent,
    ProgressBarComponent,
    PaginationComponent,
    LoadingSpinnerComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule
  ],
  exports: [
    InputComponent,
    TextAreaComponent,
    ModalComponent,
    SelectInputComponent,
    ProgressBarComponent,
    ReactiveFormsModule,
    PaginationComponent,
    LoadingSpinnerComponent
  ]
})
export class SharedModule { }

