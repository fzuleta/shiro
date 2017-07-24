import {inject, noView, bindable} from 'aurelia-framework';
import constants from "../../_/data/constants"
import localStorage from "../../_/common/localstorage"

const recaptchaCallbackName = 'setRecaptchaReady';
const ready = new Promise(resolve => window[recaptchaCallbackName] = resolve);

const lang = new localStorage().getItem("lang") || "en";

// https://developers.google.com/recaptcha/docs/display#explicit_render
let script = document.createElement('script');
script.src = `https://www.google.com/recaptcha/api.js?onload=${recaptchaCallbackName}&render=explicit&hl=${lang}`;
script.async = true;
script.defer = true;
document.head.appendChild(script);


@noView()
@inject(Element)
export class Recaptcha {
  element;
  widgetId;

  @bindable verified;
  @bindable theme = 'light';

  constructor(element) {
    this.element = element; 
    this.widgetId = null;
  }

  attached() {
    ready.then( () => {
      this.widgetId = window["grecaptcha"].render(this.element, { 
        sitekey: constants.reCaptchaKey, 
        theme: this.theme,
        callback: this.verified
      });
    });
  }
}