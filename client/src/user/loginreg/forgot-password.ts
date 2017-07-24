import {inject} from "aurelia-framework"
import {EventAggregator} from 'aurelia-event-aggregator';
import User from './../../_/data/user';
import Nav from './../../_/common/navigation';
import Api from './../../_/connect/api';
import {Router} from 'aurelia-router';
import {I18N} from 'aurelia-i18n';


@inject(Router,EventAggregator,User,Nav,Api,I18N)
export class ForgotPassword {
    router:Router;
    eventAggregator:EventAggregator;
    i18N:I18N;
    user:User;
    nav:Nav;
    api:Api;

    username = "";
    errorMessage = "";
    successMessage = ""; 
   constructor(router,eventAggregator,user,nav,api,I18N){
        this.router = router;
        this.eventAggregator = eventAggregator;
        this.user = user;
        this.nav = nav;
        this.api = api;
        this.i18N = I18N;

        this.username = "";
        this.errorMessage = "";
        this.successMessage = ""; 
    }
    forgotPassword(){
        if (this.username.indexOf("@") == -1 || this.username.indexOf(".") == -1 || this.username.length < 6) {
            this.errorMessage = this.i18N.tr('regpages:forgotpassword.errorMessage');
            return false;
        }

        this.errorMessage = "";
        this.eventAggregator.publish('setIsLoading', true);

        const o = {
            username:  this.username
        };

            //Form submit
        this.api.call("/member/forgot-password/", o)
        .then((data:any) => {
            this.eventAggregator.publish('setIsLoading', false);

            if (data.success) {
                this.successMessage = this.i18N.tr('regpages:forgotpassword.successMessage');
            } else {
                this.errorMessage = this.i18N.tr('regpages:forgotpassword.errorMessage1');
            }
        });
        return false;
    }
}