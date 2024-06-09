import { AbstractControl, FormGroup } from "@angular/forms";
import { DateComparison } from "../validator/date-comparison";

export class ApplicationValidator {

    public static mustBeLink(someFormControl:AbstractControl): { [key: string]: any } | null{
        const urlPattern = /^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})([/\w .-]*)*\/?$/;
        let Value = someFormControl.value
        if(! urlPattern.test(Value)){
            return  { invalidWebsiteLink: true };
        }
        return null;
    }


    public static dateAfterOrEqual(control:AbstractControl  ,date:Date , comparison : DateComparison ):{ [key: string]: any } | null{
        switch(comparison){
            case (DateComparison.AFTER):
                break;
            case (DateComparison.AFTER):
                break;
            case (DateComparison.AFTER):
                break;
            case (DateComparison.AFTER):
                break;
            
        }

        return null
    }



}
