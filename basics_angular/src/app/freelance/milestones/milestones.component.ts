import { Component, Input, OnInit } from '@angular/core';
import { Milestone } from '../models/milestone';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';
import { MilestoneService } from '../../Services/freelance/milestone.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-milestones',
  templateUrl: './milestones.component.html',
  styleUrl: './milestones.component.css'
})
export class MilestonesComponent implements OnInit {
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
    this.milestoneService.getMyMilestones().subscribe( (response:HttpResponse<Milestone[]>) =>{
      this.spinnerOn=false
      if(response.body == null)
        this.milestones=[]
      else
        this.milestones = response.body
    })

  }
}
