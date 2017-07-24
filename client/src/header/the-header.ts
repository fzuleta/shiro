import {bindable, inject, customElement} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import * as $ from "jquery";
import {I18N} from 'aurelia-i18n';
import LocalStorage from "./../_/common/localstorage";
import User from "./../_/data/user";
import API from "./../_/connect/api";

@customElement('the-header')
@inject(EventAggregator, I18N, LocalStorage,User,API)
export class TheHeader {
    eventAggregator;
    i18n;
    localStorage;
    user;
    api;
    languages;
    sel_lang:any;

    constructor(EventAggregator, I18N, LocalStorage, User,API) {
        this.eventAggregator = EventAggregator;
        this.i18n = I18N;
        this.localStorage = LocalStorage;
        this.user = User;
        this.api = API;
        this.languages = [
            {
                label: "English",
                value: "en"
            },
            {
                label: "Español",
                value: "es"
            }
        ];

        const lang = localStorage.getItem("lang") || "en";
        this.languages.forEach(i=>{
            if(i.value == lang){
                this.sel_lang = i;
            }
        })
    }
    attached() {
        $("#header-language-select").change( event=>{
            this.localStorage.setItem("lang", this.sel_lang.value);
            this.eventAggregator.publish("change_lang", this.sel_lang.value);
            this.i18n
                .setLocale(this.sel_lang.value)
                .then( () => {
                    // trace("ioooo changed langue")
                });
        });
    }
    logout(){
        this.api.call("/member/logout/").then((u:any) => {
            if (u.success) {
                location.reload();
            }
        });
    }
}















