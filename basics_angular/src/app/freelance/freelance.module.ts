import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import { FreelanceRoutingModule } from './freelance-routing.module';
import { SharedModule } from '../shared/shared.module';
import { GigsComponent } from './gigs/gigs.component';
import { GigCreateComponent } from './gig-create/gig-create.component';
import { ApplicationCreateComponent } from './application-create/application-create.component';
import { ApplicationDisplayComponent } from './application-display/application-display.component';
import { ApplicationsComponent } from './applications/applications.component';
import { MyGigsComponent } from './my-gigs/my-gigs.component';
import { MyApplicationsComponent } from './my-applications/my-applications.component';
import { ApplicationUpdateComponent } from './application-update/application-update.component';
import { GigUpdateComponent } from './gig-update/gig-update.component';
import { MilestoneUpdateComponent } from './milestone-update/milestone-update.component';
import { MilestoneDisplayComponent } from './milestone-display/milestone-display.component';
import { MilestonesComponent } from './milestones/milestones.component';
import { GigDisplayComponent } from './gig-display/gig-display.component';
import { MilestonesAssignedComponent } from './milestones-assigned/milestones-assigned.component';

//import { ReactiveFormsModule } from '@angular/forms'; //already declared in shared Module




@NgModule({
  declarations: [

    GigsComponent,
    GigDisplayComponent,
    GigCreateComponent,
    GigUpdateComponent,
    MyGigsComponent,
    ApplicationsComponent,
    ApplicationDisplayComponent,
    ApplicationCreateComponent,
    ApplicationUpdateComponent,
    MyApplicationsComponent,
    MilestoneDisplayComponent,
    MilestoneUpdateComponent,
    MilestonesComponent,
    MilestonesAssignedComponent,
  ],

  imports: [
    CommonModule,
    SharedModule,
    FreelanceRoutingModule,
    

    
  ]
})

export class FreelanceModule { }
