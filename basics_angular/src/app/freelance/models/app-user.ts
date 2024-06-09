import { AppRole } from "./app-role";

export interface AppUser {
 
    id?: number;
    name?: string;
    password?: string;
     email?: string;
     roles?: AppRole[];

}
