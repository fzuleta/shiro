import {inject} from "aurelia-framework"
import {EventAggregator} from 'aurelia-event-aggregator';
import { Router } from 'aurelia-router';

import User from './../_/data/user';
import Nav from './../_/common/navigation';

@inject(EventAggregator, User, Nav, Router)
export class Profile {
    eventAggregator:EventAggregator
    user:User
    nav:Nav
    router:Router
    constructor(EventAggregator, User, Nav, Router) {
        this.eventAggregator = EventAggregator
        this.user = User
        this.nav = Nav
        this.router = Router
    }
    activate () {

    }
    configureRouter(config, router) {
        this.router = router;
        
        config.title = "Profile";
        config.options.root = '/u/';

        config.map([
            {   route: ['',':reference'],
                name: 'profileview', nav:false,
                viewPorts: {rProfile: {moduleId: 'profile/view'} },                
                title: '', 
                settings: { roles: [] } 
            },
        ]);

        let handleUnknownRoutes = (instruction) => {
          return { 
            viewPorts:{ rProfile:{ moduleId: './../notfound/notfound' } },
            settings: {roles:[]}
          };
        }
        config.mapUnknownRoutes(handleUnknownRoutes);

    }
}