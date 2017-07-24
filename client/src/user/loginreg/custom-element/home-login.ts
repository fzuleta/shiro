import {bindable, inject, customElement} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {Router} from 'aurelia-router';
import {I18N} from 'aurelia-i18n';
import {DialogService} from 'aurelia-dialog';

import User from './../../../_/data/user';
import Nav from './../../../_/common/navigation';
import Api from './../../../_/connect/api';
import ltSocket from './../../../_/connect/ltsocket';
import constants from './../../../_/data/constants';
import LocalStorage from "./../../../_/common/localstorage";
import {trace} from "./../../../_/common/functions"; 

import * as $ from 'jquery';

@customElement('home-login')
@inject(Router,EventAggregator,I18N,DialogService,User,Nav,Api,ltSocket,LocalStorage)
export class HomeRegistration {
    router:Router;
    eventAggregator:EventAggregator;
    i18n:I18N;
    dialogService:DialogService;
    user:User;
    nav:Nav;
    api:Api;
    ltSocket:ltSocket; 
    ls:LocalStorage; 

    rememberMe = true;
    username = "";
    password = "";
    errorMessage:string;
    txtPass;

    constructor(router,eventAggregator,I18N,DialogService,User,Nav,Api,ltSocket,LocalStorage ){
        this.router = router;
        this.eventAggregator = eventAggregator;
        this.i18n = I18N;
        this.dialogService = DialogService;

        this.user = User;
        this.nav = Nav;
        this.api = Api;
        this.ltSocket = ltSocket;
        this.ls = LocalStorage;

        this.rememberMe = true;
        this.username = "";
        this.password = "";
        
    }

    attached(){
        this.errorMessage = this.i18n.tr("regpages:login.title");
        this.username = this.ls.getItem("email") || "";
        if(this.username != "") {
            $(this.txtPass).focus();
        }
    }

    forgotPassword(){
        this.nav.to("forgot-password") 
    }

    login(){
        if (this.username.indexOf("@") == -1 || this.username.indexOf(".") == -1 || this.username.length < 6) {
            this.errorMessage = this.i18n.tr("regpages:login.error0");
            return false;
        }
        if (this.password.length < 6) {
            this.errorMessage = this.i18n.tr("regpages:login.error1");
            return false;
        }
        this.errorMessage = "";
        this.eventAggregator.publish('setIsLoading', true);

        const o = {
            username:      this.username,
            password:      this.password,
            rememberMe:    this.rememberMe
        };

            //Form submit
        this.api.call("/member/login/", o).then((u:any) => {
            if (u.success) {
                this.ltSocket.restart();
                
                this.user.isAuthenticated = true;

                this.api.refresh().then( () => {
                    this.eventAggregator.publish("justloggedIn");
                    this.nav.to("profile", this.user.reference);
                }); 

                if (this.rememberMe) {
                    this.ls.setItem("email", this.username);
                } else { 
                    this.ls.removeItem("email");
                }
            } else {
                this.errorMessage = "Wrong username or password";
                this.eventAggregator.publish('setIsLoading', false);
            }
        });
        return false;
    }
}
