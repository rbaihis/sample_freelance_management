import { Component, Input } from '@angular/core';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';
import { MilestoneService } from '../../Services/freelance/milestone.service';
import { Milestone } from '../models/milestone';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-milestones-assigned',
  templateUrl: './milestones-assigned.component.html',
  styleUrl: './milestones-assigned.component.css'
})
export class MilestonesAssignedComponent {
   constructor( private userConnected:FooConnectedUserService , private milestoneService:MilestoneService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  //spinner
  spinnerOn:boolean=false

  
  @Input() milestones : Milestone[] = [];

  ngOnInit(): void {
    this.milestoneService.getMyAssignedMilestones().subscribe( (response:HttpResponse<Milestone[]>) =>{
      this.spinnerOn=false
      console.log("body response: ",response.body);
      if(response.body == null)
        this.milestones=[]
      else
        this.milestones = response.body
    })

  }
}
