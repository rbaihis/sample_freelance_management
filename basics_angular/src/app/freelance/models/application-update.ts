import { FileDoc } from "./file-doc"



export interface ApplicationUpdate {
    
    id?: number
    gigId?:number
    ownerId?:number
    ownerName?:string
    
    bid? :number
    extraCost?:number
    walletId?:string
    files?:FileDoc[]
}
