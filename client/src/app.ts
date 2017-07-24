import {inject} from "aurelia-framework";
import {EventAggregator} from 'aurelia-event-aggregator';
import constants from "./_/data/constants";
import {trace} from "./_/common/functions";
import Alerts from "./_/common/alerts";
import localStorage from "./_/common/localstorage";
import User from "./_/data/user";
import Api from "./_/connect/api";
import ltSocket from "./_/connect/ltsocket";
import {Router, RedirectToRoute} from 'aurelia-router';
import {I18N} from 'aurelia-i18n';
import * as $ from "jquery";

@inject(I18N, EventAggregator, Alerts,User,ltSocket,Api)
export class App {
    i18n:I18N;
    alerts:Alerts; 
    eventAggregator:EventAggregator;  
    router:Router;
    user:User;
    socket:ltSocket;
    api:Api;

    evt_navigateTo;
    loader;
    subscriber;
    evt_errorshow;
    _showLoader:Boolean = false;

    constructor(i18n, eventAggregator, Alerts,User,ltSocket,Api) {
        this.i18n = i18n;
        this.alerts = Alerts;
        this.user = User;
        this.socket = ltSocket;
        this.api = Api;

        this.eventAggregator = eventAggregator;
    }

    activate(){
        this.evt_navigateTo = this.eventAggregator.subscribe('navigateTo', payload => { 
            this.router.navigate(payload.route, payload.params || undefined); 
        });
        return this.api.refresh();
    }

    attached() {
        this.loader = $('#loadingScreen');
        //Subscribes to the eventAggregator to show the loader or not
        this.subscriber = this.eventAggregator.subscribe('setIsLoading', showLoader => {
            if (showLoader) {
                this.loader.show(); 
            } else {
                this.loader.show();
                this.loader.fadeOut(500, () =>{ this._showLoader = showLoader; } ); 
            }
        });
        
        this.evt_errorshow = this.eventAggregator.subscribe('showGenericError', payload => { 
            this.alerts.showGenericError(payload);
        });
        
        setTimeout( ()=> {
            this.eventAggregator.publish('setIsLoading', false);    
        },250);

      }

    configureRouter(config, router) {
        this.router = router;
        config.title = "Apache shiro example";

        // Remove the /#/address/
        // config.options.pushState = true;
        config.options.root = '/'; // this matches the base href tag 

        config.addPipelineStep('authorize',     AuthorizeStep);
        config.addPipelineStep('preRender',     PreRenderStep);
        config.addPipelineStep('postRender',    PostRenderStep);

        config.map([
            {   route:"",
                name: "home", 
                viewPorts:{ mainView:{moduleId: "./user/home"} }, 
                settings: { roles: []} },

            // LOGIN - REGISTRATION
            {   route:["/user/forgotpassword"], 
                viewPorts:{ mainView:{moduleId: "./user/loginreg/forgot-password"} },         
                name: "forgotPassword", 
                settings: {roles: ['notloggedin']} },
            {   route:["/user/forgotpasswordrecover/:code"],                     
                viewPorts:{ mainView:{moduleId: "./user/loginreg/forgot-password-recover"} }, 
                name: "forgotPasswordRecover", 
                settings: {roles: []} },
            {   route:["/user/confirmpin/:code", "/user/confirmpin"],       
                viewPorts:{ mainView:{moduleId: "./user/loginreg/confirm-pin"} },             
                name: "confirmpin", 
                settings: {roles: []} },

            // USER
            {   route:["/u"],
                viewPorts:{ mainView:{moduleId: "./profile/profile"} },      
                name: "profile", 
                settings: {roles:["loggedIn"]} },
        ]);

        const handleUnknownRoutes = (instruction) => {
            return { 
                viewPorts:{ "mainView":{ moduleId: './notfound/notfound' } },
                settings: { roles:[] }
            };
        }
        config.mapUnknownRoutes(handleUnknownRoutes);
    }

}

@inject(User)
class AuthorizeStep {
    User;
    constructor(User){
        this.User = User;
    };

    run(navigationInstruction, next) {
    for(let i=0; i<navigationInstruction.getAllInstructions().length;i++) {
        const ni = navigationInstruction.getAllInstructions()[i];
        if(ni.config.settings.roles.indexOf('loggedIn') !== -1) {
            if (!this.User.isAuthenticatedOrRemembered) { return next.cancel(new RedirectToRoute('home')); } else {break;}
        }
        if(ni.config.settings.roles.indexOf('isAuthenticated') !== -1) {
            if (!this.User.isAuthenticated) { return next.cancel(new RedirectToRoute('home')); } else {break;}
        }
        if(ni.config.settings.roles.indexOf('notloggedin') !== -1) {
            if (this.User.isAuthenticated) { return next.cancel(new RedirectToRoute('home')); } else {break;}
        }
    }

    return next();
    }
}

@inject(EventAggregator)
class PreRenderStep {
    eventAggregator;
    constructor(EventAggregator){
        this.eventAggregator = EventAggregator;
    };

    run(navigationInstruction, next) {
        this.eventAggregator.publish('setIsLoading', true);
        trace("---- Page preRenderStep");
    return next();
    }
}
@inject(EventAggregator)
class PostRenderStep {
    eventAggregator;
    constructor(EventAggregator){
        this.eventAggregator = EventAggregator;
    };

    run(navigationInstruction, next) {
        this.eventAggregator.publish('setIsLoading', false);
        this.eventAggregator.publish('viewHasFinishedRendering');
        return next();
    }
}
