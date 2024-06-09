import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GigCreateComponent } from './gig-create/gig-create.component';
import { ApplicationUpdateComponent } from './application-update/application-update.component';
import { GigUpdateComponent } from './gig-update/gig-update.component';
import { MilestoneUpdateComponent } from './milestone-update/milestone-update.component';
import { MilestoneDisplayComponent } from './milestone-display/milestone-display.component';
import { ApplicationDisplayComponent } from './application-display/application-display.component';
import { ApplicationsComponent } from './applications/applications.component';
import { MilestonesComponent } from './milestones/milestones.component';
import { GigDisplayComponent } from './gig-display/gig-display.component';
import { MilestonesAssignedComponent } from './milestones-assigned/milestones-assigned.component';

const routes: Routes = [
//------gigs-----------
  // { // defined on app-route.ts
  //   path: 'gigs',
  //   component: GigsComponent
  // },
  {
    path: 'gigs/create',
    pathMatch:'full',
    component: GigCreateComponent
  },
  {
    path: 'gigs/:idGig/update',
    pathMatch:'full',
    component: GigUpdateComponent
  },
  {//get one with its applications
    path: 'gigs/:idGig',
    pathMatch:'full',
    component: GigDisplayComponent
  },
//------applications-----
//  {defined on gig using modal no need for path
//     path: 'gigs/:idGig/applications/create',
//     component: ApplicationUpdateComponent
//   },
  {
    path: 'gigs/applications/admin',
    pathMatch:'full',
    component: ApplicationsComponent,
  },
  {
    path: 'gigs/applications/:idApp/update',
    pathMatch:'full',
    component: ApplicationUpdateComponent,
  },
   {
    path: 'gigs/applications/:idApp',
    pathMatch:'full',
    component: ApplicationDisplayComponent,
  },
//milestones
  {//all milestones
    path: 'gigs/milestones/admin',
    component: MilestonesComponent,
    pathMatch:'full'
  },
  {//Gig's-Milestones
    path: 'gigs/milestones/my',
    component: MilestonesComponent,
    pathMatch:'full'
  },
  {//Gig's-Milestones
    path: 'gigs/milestones/myAssigned',
    component: MilestonesAssignedComponent,
    pathMatch:'full'
  },
  {//update Milestone
    path: 'gigs/milestones/:idMs/update',
    component: MilestoneUpdateComponent,
    pathMatch:'full'
  },
  {//display one milestone
    path: 'gigs/milestones/:idMs',
    component: MilestoneDisplayComponent,
    pathMatch:'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FreelanceRoutingModule { }
