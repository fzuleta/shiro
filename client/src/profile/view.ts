import {inject} from "aurelia-framework"
import {EventAggregator} from 'aurelia-event-aggregator';
import {I18N} from 'aurelia-i18n';
import { Router } from 'aurelia-router';
import * as functions from "./../_/common/functions";
import {trace} from "./../_/common/functions";
import Api from './../_/connect/api';
import Alerts from "./../_/common/alerts";
import constants from "./../_/data/constants";
import Nav from "./../_/common/navigation";
import User from "./../_/data/user";
import * as $ from "jquery";


@inject(EventAggregator,Alerts, I18N, Nav, User, Api)
export class View {
    eventAggregator:EventAggregator
    alerts:Alerts
    i18n:I18N
    router:Router
    nav:Nav
    user:User
    api:Api

    showFirstTime = false;

    constructor(eventAggregator,Alerts, I18N, Nav, User, Api){
        this.eventAggregator = eventAggregator
        this.alerts = Alerts
        this.i18n = I18N
        this.nav = Nav
        this.user = User
        this.api = Api
    }
    activate(params, routeConfig){
        if (!this.user.isAuthenticatedOrRemembered) {
            this.nav.to("");
            return;
        }
    }
    attached(){
        setTimeout(()=>{
            $('[data-toggle="tooltip"]').tooltip();
            this.showFirstTime = this.user.firstTime;
        }, 400);
    }
}

