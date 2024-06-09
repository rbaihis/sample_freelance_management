export const environment = {
    production: true,
    apiUrl: 'http://localhost:9090',
    apiImageDisplay: 'http://localhost:9090/files/images',
    apiFileDownload: 'http://localhost:9090/files',
    apiFileDelete: 'http://localhost:9090/files/delete',
//Gig-App-milestones-Relationship
    //  apiChoseApplication:'http://localhost:9090/gigs/{idGig}/milestones/application/{idApp}/chosen',
    //  apiActivateMileStone: 'http://localhost:9090/gigs/milestones/{id}/activate'
    //  apiReportMileStoneByGigCreator='http://localhost:9090/gigs/milestones/{id}/report'
    //  apiRefundOrRequestPaymentMileStoneByBothSides='http://localhost:9090/gigs/milestones/{id}/refund'
//milestoneUpdatebyFreelancer:
    //  apiUpdateFiles = 'http://localhost:9090/gigs/milestones/{id}/application/files'
    //  apiValidateWorkIsDone = 'http://localhost:9090/gigs/milestones/{id}/application/validate'
};
/*

validateMilestoneByCustomerToPayFreelancer() {
    console.log("clicked validate Payment ms");
      let msId:number = this.milestoneModel?.id!
      this.milestoneService.ValidateWorkAndPayMs(msId).subscribe( 
          (response:HttpResponse<any>) => {
              console.log(response.status);
              console.log(response.body);
              if(response.status>=200&&response.status<=202){
              
                TimeOutFunctions.runWithTimeoutAction(2000,()=>this.errorMsg="milestone successfully payed And On its way to its customer",
                  ()=> this.router.navigate(['/gigs',this.milestoneModel?.gigId]))
                return;
               }
              TimeOutFunctions.runWithTimeoutAction(1500,()=>this.errorMsg="bad request no Application Assigned to Ms yet", ()=>this.errorMsg=null)
        } );
  }
  //---------------------------
  requestPaymentMilestone() {
    console.log("clicked validate Payment ms");
      let msId:number = this.milestoneModel?.id!
      this.milestoneService.requestRefundOrPaymentMs(msId).subscribe( 
          (response:HttpResponse<any>) => {
              console.log(response.status);
              console.log(response.body);
              if(response.status>=200&&response.status<=202){
                TimeOutFunctions.runWithTimeoutAction(2000,()=>this.errorMsg=response.body.message+',        '+'refunded: '+response.body.refundable ,
                  ()=> this.router.navigate(['/gigs',this.milestoneModel?.gigId]))
                return;
               }
              TimeOutFunctions.runWithTimeoutAction(1500,()=>this.errorMsg="bad request no Application Assigned to Ms yet ", ()=>this.errorMsg=null)
        } );

*/