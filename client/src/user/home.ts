import {inject} from "aurelia-framework"
import {EventAggregator} from 'aurelia-event-aggregator';
import User from './../_/data/user';
import Nav from './../_/common/navigation';

@inject(EventAggregator, User, Nav)
export class Home {
    eventAggregator:EventAggregator;
    user:User;
    nav:Nav;
    showUI = false;
    constructor(EventAggregator, User, Nav) {
        this.eventAggregator = EventAggregator;
        this.user = User;
        this.nav = Nav;
        this.showUI = false;
    }
    activate () {
        if (this.user.isAuthenticatedOrRemembered) {
            this.eventAggregator.publish('setIsLoading', true);
            this.nav.to("profile");
            return;
        }

        this.showUI = true;
    }
}