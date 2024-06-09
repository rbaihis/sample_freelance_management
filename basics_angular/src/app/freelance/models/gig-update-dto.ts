import { FileDoc } from "./file-doc"

export interface GigUpdateDto {
    id?:number,
    ownerId?:number
    ownerName?:string

    walletId?:string
    files?:FileDoc[]
}
