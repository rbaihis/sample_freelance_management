
import { Application } from "./application"
import { FileDoc } from "./file-doc"
import { Milestone } from "./milestone"
import { Skill } from "./skill"


export interface Gig {
  ownerId?:number
  ownerName?:string

  id? : number
  title?: string
  description?: string 
  questions?:string
  minPrice?: number  
  maxPrice?: number
  submitDate?:Date 
   
  projectStart?:Date
  projectDeadline?:Date
  gigAssigned?:boolean
  files?: FileDoc[]
  skills?: Skill[]
  milestones?:Milestone[]
  customerProfile?:string
  walletId?:string,
  applications?:Application[]





}
