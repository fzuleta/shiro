import {trace} from "../functions";

const id = 'UA-70822515-2';
let userSet = false;

export function init(){
    try {
      (<any>window).ga('create', id, 'auto');
      (<any>window).ga('send', 'pageview');
      trace("starting the GA journey");
    } catch (e){
        trace(e);
    }
}
export function setUser(user_id){
    try {
        if (!userSet) (<any>window).ga('set', 'userId', user_id);
        userSet = true;
    } catch (e){
        trace(e);
    }
}
