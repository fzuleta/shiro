import {inject} from "aurelia-framework"
import {EventAggregator} from 'aurelia-event-aggregator';
import User from './../../_/data/user';
import Nav from './../../_/common/navigation';
import Api from './../../_/connect/api';
import {Router} from 'aurelia-router';
import {I18N} from 'aurelia-i18n';


@inject(Router,EventAggregator,User,Nav,Api,I18N)
export class ForgotPasswordRecover {
    router:Router;
    eventAggregator:EventAggregator;
    user:User;
    nav:Nav;
    api:Api;
    i18n:I18N;

    code = "";
    password = "";
    password2 = "";
    errorMessage = "";
    successMessage = ""; 
   constructor(router,eventAggregator,user,nav,api,I18N){
        this.router = router;
        this.eventAggregator = eventAggregator;
        this.user = user;
        this.nav = nav;
        this.api = api;
        this.i18n = I18N;

        this.code = "";
        this.password = "";
        this.password2 = "";
        this.errorMessage = "";
        this.successMessage = ""; 
    }
    
    activate(params, routeConfig){
        this.code = params.code;
    }

    doForgotPassword(){
        if (this.code == "") {
            this.errorMessage = this.i18n.tr('regpages:forgotpasswordrecover.errors.e0');
            return false;
        }
        if (this.password.length < 6) {
            this.errorMessage = this.i18n.tr('regpages:forgotpasswordrecover.errors.e1');
            return false;
        }
        if (this.password != this.password2) {
            this.errorMessage = this.i18n.tr('regpages:forgotpasswordrecover.errors.e2');
            return false;
        }

        this.errorMessage = "";
        this.eventAggregator.publish('setIsLoading', true);

        const o = {
            code:  this.code,
            password: this.password,
            password2: this.password2
        };

        //Form submit
        this.api.call("/member/forgot-password-recover/", o)
        .then( (data:any) => {
            this.eventAggregator.publish('setIsLoading', false);

            if (data.success) {
                this.successMessage = this.i18n.tr('regpages:forgotpasswordrecover.success');
                this.router.navigateToRoute("home");
            } else {
                this.errorMessage = "Error";
            }
        });
        return false;
    }
}