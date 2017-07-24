import {inject} from "aurelia-framework"
import {EventAggregator} from 'aurelia-event-aggregator';
import User from './../../_/data/user';
import Nav from './../../_/common/navigation';
import Api from './../../_/connect/api';
import {Router} from 'aurelia-router';
import {I18N} from 'aurelia-i18n';


@inject(Router,EventAggregator,User,Nav,Api,I18N)
export class ConfirmPin {
    router:Router;
    eventAggregator:EventAggregator;
    user:User;
    api:Api;
    nav:Nav;
    i18n:I18N;

    code = "";
    errorMessage = "";
   constructor(router,eventAggregator,user,nav,api,i18n){
        this.router = router;
        this.eventAggregator = eventAggregator;
        this.user = user;
        this.nav = nav;
        this.api = api;
        this.i18n = i18n;

        this.code = "";
        this.errorMessage = "";
    }
    
    activate(params, routeConfig){
        if(params && params.code){
            this.code = params.code;
            this.doConfirmPin();
        }
        return true;
    } 

    doConfirmPin(){
        if (this.code == "") {
            this.errorMessage = this.i18n.tr('regpages:confirmpin.errorMessage0');
            return false;
        }

        this.errorMessage = "";
        this.eventAggregator.publish('setIsLoading', true);

        const o = {
            code:  this.code
        };

        //Form submit
        this.api.call("/member/confirm-pin/", o)
        .then( (data:any) => {
            this.eventAggregator.publish('setIsLoading', false);

            if (data.success) {
                this.router.navigateToRoute("home");
            } else {
                this.errorMessage = this.i18n.tr('regpages:confirmpin.errorMessage1');
            }
        });
        return false;
    }
}