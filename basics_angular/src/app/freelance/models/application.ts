import { FileDoc } from "./file-doc"
import { Skill } from "./skill"



export interface Application {

    id?: number
    ownerId?:number
    ownerName?:string
    customerId?:number

    freelancer?:string
    freelancerProfile?:string
    description?:string
    questions?:string
    workReferenceLinks?:string
    bid? :number
    extraCost?:number
    walletId?:string
    submitDate?:Date
    applicationWon?:boolean
    files?:FileDoc[]
    gigId?:number
    skills?:Skill[]
    //dto's proprety
    workReferenceLinksDto?:string[]
    // idFreelancer?:number
    idGig?:number
    filePaths?:string[]

}
  