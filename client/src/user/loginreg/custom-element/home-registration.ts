import {bindable, inject, customElement} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {Router} from 'aurelia-router';
import {I18N} from 'aurelia-i18n';

import User from './../../../_/data/user';
import Nav from './../../../_/common/navigation';
import Api from './../../../_/connect/api';
import ltSocket from './../../../_/connect/ltsocket';
import constants from './../../../_/data/constants';
import LocalStorage from "./../../../_/common/localstorage";
import {trace} from "./../../../_/common/functions"; 
import Alerts from "./../../../_/common/alerts";

import * as $ from 'jquery';


@customElement('home-registration')
@inject(Router,EventAggregator,I18N,User,Nav,Api,ltSocket,LocalStorage,Alerts)
export class HomeRegistration {
    router:Router;
    eventAggregator:EventAggregator;
    i18n:I18N;

    user:User;
    nav:Nav;
    api:Api;
    ltSocket:ltSocket; 
    ls:LocalStorage; 
    alerts:Alerts;

    errorMessage = "";
    captchaVerified = false;

    tosCheck = false;
    username = "";
    password = "";

    savedUsername;

    constructor(router,eventAggregator,I18N,User,Nav,Api,ltSocket,LocalStorage,Alerts){
        this.router = router;
        this.eventAggregator = eventAggregator;
        this.i18n = I18N;

        this.user = User;
        this.nav = Nav;
        this.api = Api;
        this.ltSocket = ltSocket;
        this.ls = LocalStorage;
        this.alerts = Alerts;

        this.errorMessage = "";
        this.captchaVerified = false;

        this.tosCheck = false;
        this.username = "";
        this.password = "";
     }
     attached(){
        this.errorMessage = this.i18n.tr("regpages:register.title");
        this.savedUsername = this.ls.getItem("email") || "";
     }
     onCaptchaVerified() {
        this.captchaVerified = true;
     }

     signup(){
        if (this.username.indexOf("@") == -1 || this.username.indexOf(".") == -1 || this.username.length < 6) {
            this.errorMessage = "Invalid Email";
            return false;
        }
        if (this.password.length < 6) {
            this.errorMessage = "Password must be at least 6 digits";
            return false;
        }
        if (!this.tosCheck) {
            this.errorMessage = "Please accept the terms of use and privacy policy";
            return false;
        }
        
        let captchaerror = false;
        let captcha = "";
        if (constants.captchaEnabled) {
            if (!this.captchaVerified) {
                this.errorMessage = "Verify captcha";
                return false;
            }
            try {
                captcha = window["grecaptcha"].getResponse();
            } catch (e) {
                captchaerror=true;
                window["grecaptcha"].reset();
                
                this.alerts.showGenericError({
                    error: true,
                    message: this.i18n.tr("translation:error.captcha_0")
                });
            }
            if (captchaerror) return;
        }

        this.errorMessage = "";
        this.eventAggregator.publish('setIsLoading', true);

        const o = {
            username:  this.username,
            password:  this.password,
            password2: this.password,
            approve:   this.tosCheck,
            captcha:   captcha
        };

        //Form submit
        this.api.call("/member/register/", o)
            .then((u:any) => {
                const data = u.data
                this.eventAggregator.publish('setIsLoading', false);

                if (u.success) {
                    this.user.isAuthenticated = true;
                    this.user.firstTime = data.firstTime;
                    this.api.refresh().then( () => {
                        this.eventAggregator.publish("justloggedIn");
                        this.nav.to("profile", this.user.reference);
                    });
                } else {
                    window["grecaptcha"].reset();
                    this.errorMessage = "We're sorry, username is taken";
                }
                // todo Navigate somewhere
            });
        return false;
     }
     fbLogin() {
        
     }
     googleLogin() {
         
     }
}
