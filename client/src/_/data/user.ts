export default class User {
    expectedToken:string = ""
    isRemembered:boolean = false
    isAuthenticated:boolean = false
    isAuthenticatedOrRemembered:boolean = false
    firstTime:boolean = false

    reference:string=""
    
    constructor(){
    }

    setMe(o){
        const me = o.me
        this.reference = me.reference;
    }
}