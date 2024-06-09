import { Application } from "./application"
import { FileDoc } from "./file-doc"

export interface MilestoneUpdate {
    freelancerId?:number
    ownerId?:number
    // 
    id?:number
    gigId?:number
    idApplicationWon?:number

    goalDescription?:String
    dateCreation?:Date // ok
    dateStartWorking?:Date // ok
    datePayment?:Date // ok
    amountPercentage?:number // ok
    amount?:number
    milestoneDeliverable?:FileDoc[]
    applicationWon?:Application  // ok
    updatable?:boolean
    deposited?:boolean // ok remove is
    validatedByGigCreator?:boolean
    validatedByFreelancer?:boolean
    veclamationByGigCreator?:boolean
    walletCustomer?:string
    walletFreelancer?:string
    ratingCustomer?:number
    ratingFreelancer?:number
}
