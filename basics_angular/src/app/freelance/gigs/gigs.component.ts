import { Component, OnDestroy, OnInit } from '@angular/core';
import { Gig } from '../models/gig';
import { Skill } from '../models/skill';
import { GigService } from '../../Services/freelance/gig.service';
import {  combineLatest, debounceTime, filter, Subscription } from 'rxjs';
import {  HttpResponse } from '@angular/common/http';
import { PageJPAReleventData } from '../../shared/pagination/entity/page-jparelevent-data';
import { FooConnectedUserService } from '../../Services/foo-connected-user.service';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-gigs',
  templateUrl: './gigs.component.html',
  styleUrl: './gigs.component.css'
})
export class GigsComponent implements OnInit ,OnDestroy {
  constructor(public gigService:GigService,  private userConnected:FooConnectedUserService) {
      console.log("Connected user : ",userConnected.currentUser);
      if(userConnected.currentUser !==null)
        this.currentUserId = userConnected.currentUser?.id ?? null
  }
  //userId current
  currentUserId : number |null = null;
  //spinner
  spinnerOn:boolean=false

  //-------pagination attributes----
  totalPages: number = 3;
  currentPage: number= 0;
  sort:string|null=null;
  //-------SearchAttributes------
  searchUnderMaxPrice:FormControl = new FormControl('');
  searchAboveMinPrice:FormControl = new FormControl('');
  _maxPriceSearch:string|null=''
  _minPriceSearch:string|null=''
  
  onPageChange(event: number) {
    console.log(event);
    this.currentPage=event;
    this.callPagination()
  }


  onSearch() {
    //both must be filled case
    // combineLatest([
    //   this.searchUnderMaxPrice.valueChanges.pipe(debounceTime(500)),
    //   this.searchAboveMinPrice.valueChanges.pipe(debounceTime(500))
    // ]).subscribe(([minPrice, maxPrice]) => {
    //   this._minPriceSearch = minPrice;
    //   this._maxPriceSearch = maxPrice;
    //   //call pagination
    //   this.callPagination()
    // });
    this.spinnerOn=true

    this.searchAboveMinPrice.valueChanges.pipe(
      debounceTime(500),
      filter(value => value !=='')
    ).subscribe( minPrice => {
      this._minPriceSearch = minPrice
      //set index page pagination to 0
      this.currentPage=0

      if(this._minPriceSearch != null){
        this._maxPriceSearch=null
      }
      
      this.callPagination()
    })

    this.searchUnderMaxPrice.valueChanges.pipe(
      debounceTime(500),
      filter(value => value !=='')
    ).subscribe( maxPrice => {
      
      this._maxPriceSearch = maxPrice
      //set index page pagination to 0
      this.currentPage=0
      
      if(this._maxPriceSearch == null)
        this._maxPriceSearch=null

      this.callPagination()

      
     
    })


  }

  callPagination(){
    this.spinnerOn=true
    this.gigsSubscription = this.gigService.getGigsPaginated(this.currentPage,this.sort!,this._minPriceSearch!,this._maxPriceSearch!).subscribe( (response:HttpResponse<PageJPAReleventData<Gig>>) =>{
      this.spinnerOn=false
      this.gigs = response.body?.content ?? null ;
      this.totalPages=response.body?.totalPages?? -1
      this.currentPage=response.body?.pageable.pageNumber?? -1
      
      console.log("sort :" + this.sort);
      // console.log(response);
    })
  }
  //-------end-pagination
  
  showDescription:boolean = false
  showApplications:boolean = false
  showMilestones:boolean=false
  
  // gigs$: Observable<HttpResponse<Gig[]>> | undefined;
  private gigsSubscription: Subscription | undefined;
  gigs:Gig[]|null = null
  
  //-------Default fetch all gigs-------------
  // ngOnInit(): void {
    //     // this.gigs$ = this.gigService.getGigs();
    //     this.gigsSubscription = this.gigService.getGigs().subscribe( (response:HttpResponse<Gig[]>) =>{
      //          this.gigs = response.body ?? null ;
  //          console.log('Response-Status :'+response.status);
  //     })
  // }
    ngOnInit(): void {
      this.callPagination()
  }

  ngOnDestroy(): void {
      this.gigsSubscription?.unsubscribe();
  }
 

  isProjectStartBeforTodayFunction(dateProjectStart:Date):boolean{
    let result = new Date(Date.now()) > new Date(dateProjectStart)
    // console.log("is Project old: ",result);
    return result
  }


}





  // =[  
    // {
      // id:5,
      // title:"Spring App ",
      // description:"\naka lorem blablana fjdkfj dfghjzfj fiehjflieh fkehfleh aeogjqef dmogjmogj logjmogj  \n hhhhhhhh  vnsfkghs fg,fmg vjsmgvj mvsdmj fnqlkfhqslkf \n fnqdhfqmd gnjmfgqdmf ogjogjqdl  ldg,qmldgqdml gj,qdlgj,qdml lgdjqmdlgn lgjqdmgnj dgljqmlgn",
      // priceMin:200,
      // priceMax:500,
      // skills:
        // [Skill.CPP,Skill.JAVA],
      // files:[
        // {
          // name :"description",
          // extension :"txt",
          // path :"description.txt"
        // },
        // {
          // name :"template",
          // extension :"png",
          // path:"userId-project-id-template.png"
        // }
      // ]
    // },
    // {
      // id:2,
      // title:"Angular App ",
      // description:"\naka lorem blablana fjdkfj dfghjzfj fiehjflieh fkehfleh aeogjqef dmogjmogj logjmogj  \n hhhhhhhh  vnsfkghs fg,fmg vjsmgvj mvsdmj fnqlkfhqslkf \n fnqdhfqmd gnjmfgqdmf ogjogjqdl  ldg,qmldgqdml gj,qdlgj,qdml lgdjqmdlgn lgjqdmgnj dgljqmlgn",
      // priceMin:200,
      // priceMax:500,
      // skills:
        // [Skill.CPP,Skill.JAVA],
      // files:[
        // {
          // name :"description",
          // extension :"txt",
          // path :"description.txt"
        // },
        // {
          // name :"template",
          // extension :"png",
          // path :"userId-project-id-template.png"
        // }
      // ],
// 
      ////applications-Here
      // applications: [
        // {
          // id:1,
          // freelancer: "tony",
          // freelancerProfile:"github.com/tony",
          // description:"blabla abala anhaln anal anla alnal ajam ajal amjam aja hioglgouggbgougogo gogoigoi gogigb gohlknmkn vbbonlkbi obb",
          // files:[
              // {
                // name :"description",
                // extension :".txt",
                // path :"description.txt"
              // },
              // {
                // name :"template",
                // extension :".png",
                // path :"userId-project-id-template.png"
              // }
          // ],
          // questions:"please mor detail about ?",
          // bid : 300,
          // extraCost: undefined,
          // workReferenceLinksDto:[
            // "facebook.com",
            // "twitter.com"
          // ]
        // },
        // {
          // id:2,
          // freelancer: "salah",
          // freelancerProfile:"github.com/salah",
          // description:"blabla abala anhaln anal anla alnal ajam ajal amjam aja",
          // files:[
            // 
          // ],
          // questions:"please mor detail about ?",
          // bid : 300,
          // extraCost: undefined,
          // workReferenceLinksDto:[
            // "facebook.com",
            // "twitter.com"
          // ]
        // },
        // {
          // id:3,
          // freelancer: "asma",
          // freelancerProfile:"github.com/asma",
          // description:"blabla abala anhaln anal anla alnal ajam ajal amjam aja",
          // files:[
            // 
          // ],
          // questions: undefined,
          // bid : 300,
          // extraCost: undefined,
          // workReferenceLinksDto:[
            // "facebook.com",
            // "twitter.com"
          // ]
// 
        // }
      // ]
      ////end-Applications
      // ,
      ////start-Milestones
      // milestones : [
        // {
          // id: 1,
          // idGig: 123,
          // idApplicationWon: 456,
          // goalDescription: "Complete initial design mockups",
          // dateCreation: new Date("2024-04-20"),
          // dateStartWorking: new Date("2024-04-21"),
          // datePayment: new Date("2024-05-05"),
          // amountPercentage: 50,
          // amount: 1000,
          // milestoneDeliverable: [{
              // name :"description",
              // extension :"txt",
              // path :"description.txt"
            // },
            // {
              // name :"template",
              // extension :"png",
              // path:"userId-project-id-template.png"
            // }
          // ],
          // isValidatedByGigCreator: true,
          // isValidatedByFreelancer: false,
          // isReclamationByGigCreator: false,
          // walletCustomer: "customer_wallet_address",
          // walletFreelancer: "freelancer_wallet_address",
          // ratingCustomer: 9,
          // ratingFreelancer: 9
        // },
        // {
          // id: 2,
          // idGig: 123,
          // idApplicationWon: 456,
          // goalDescription: "Implement backend functionality",
          // dateCreation: new Date("2024-04-22"),
          // dateStartWorking: new Date("2024-04-23"),
          // datePayment: new Date("2024-05-10"),
          // amountPercentage: 30,
          // amount: 2000,
          // milestoneDeliverable: [{
              // name :"description",
              // extension :"txt",
              // path :"description.txt"
            // },
            // {
              // name :"template",
              // extension :"png",
              // path:"userId-project-id-template.png"
            // }
          // ],
          // isValidatedByGigCreator: false,
          // isValidatedByFreelancer: false,
          // isReclamationByGigCreator: false,
          // walletCustomer: "customer_wallet_address",
          // walletFreelancer: "freelancer_wallet_address",
          // ratingCustomer: 4,
          // ratingFreelancer: 7
        // },
        // {
          // id: 3,
          // idGig: 123,
          // idApplicationWon: 456,
          // goalDescription: "Finalize testing and deploy Lorem ipsum, dolor sit amet consectetur adipisicing elit. Vitae consequuntur quisquam molestias facere, tenetur laborum fugit rem tempora laudantium libero similique blanditiis assumenda corrupti quas provident architecto necessitatibus sunt aspernatur",
          // dateCreation: new Date("2024-04-25"),
          // dateStartWorking: new Date("2024-04-26"),
          // datePayment: new Date("2024-05-15"),
          // amountPercentage: 20,
          // amount: 3000,
          // milestoneDeliverable: [{
              // name :"description",
              // extension :"txt",
              // path :"description.txt"
            // },
            // {
              // name :"template",
              // extension :"png",
              // path:"userId-project-id-template.png"
            // }
          // ],
          // isValidatedByGigCreator: false,
          // isValidatedByFreelancer: false,
          // isReclamationByGigCreator: false,
          // walletCustomer: "customer_wallet_address",
          // walletFreelancer: "freelancer_wallet_address",
          // ratingCustomer: 6,
          // ratingFreelancer: 10
        // }
      // ]
      ///end-milestones
// 
    // }
  // ];
