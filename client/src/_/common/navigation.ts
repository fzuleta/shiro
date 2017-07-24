import { inject } from 'aurelia-framework';
import { EventAggregator } from 'aurelia-event-aggregator';

@inject(EventAggregator)
export default class Navigation {
    eventAggregator:EventAggregator
    constructor(EventAggregator){
        this.eventAggregator = EventAggregator;
    }

    to(where:string, param:string=""){ 
        const encParam = encodeURIComponent(param);
        let o:any = {}; 
        switch (where){
            case "forgot-password":
                o.route = "/user/forgotpassword";
                break;
            case "profile":
                o.route = "/u/" + encParam;
                break;
            default:
                o.route = "";
        }
        this.eventAggregator.publish("navigateTo", o);
    }
}
