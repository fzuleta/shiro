import "xss-filters";
import * as $ from "jquery";

export function isNumeric(str) {
    if (str == null){
        return false;
    }
    var regx = /^[-+]?\d*\.?\d+(?:[eE][-+]?\d+)?$/;
    return regx.test(str);
}

export function trimString(str, n) {
    if (str.length>n) { str = str.substr(0,n) + "..."; }
    return str;
}

export function validateEmail(email) {
    let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

export function generateString(char=5) {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < char; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

export function isMobile() {
    const isAndroid = this.isAndroid();
    const isIOS = this.isIOS();
    const isBlackberry = this.isBlackberry();
    return isAndroid || isIOS || isBlackberry;
}
export function isAndroid() { return /android/i.test(navigator.userAgent.toLowerCase()); }
export function isIOS() { return /iphone|ipad|ipod/i.test(navigator.userAgent.toLowerCase()); }
export function isBlackberry() { return /blackberry/i.test(navigator.userAgent.toLowerCase()); }
    


/** Compare function for sorting i.e   dates.sort(dynamicSort("date")) */
export function dynamicSort(property, sortOrder = 1) {
    if(property[0] === "-") {
        sortOrder = -1;
        property = property.substr(1);
    }
    return function (a,b) {
        const ar = Number(a[property]);
        const br = Number(b[property]);
        var result = (ar < br) ? -1 : (ar > br) ? 1 : 0;
        // trace("ar: " + ar);
        return result * sortOrder;
    }
}


export function sanitize(value) { 
    return window["xssFilters"].inHTMLData(value);
}


export function trace(o, bgcolor="#f8eafc", color="#302207") {
    if (typeof console !== "undefined") {
        if (typeof o === "string") {
            console.log("%c"+o, `background: ${bgcolor}; color: ${color}`);
        } else {
            console.log(o);
        }
    }
}

export function range(n) {
    return Array.from(Array(n).keys());
}
