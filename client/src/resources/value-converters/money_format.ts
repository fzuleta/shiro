import * as numeral from 'numeral';
import constants from "./../../_/data/constants";
import {inject} from 'aurelia-framework';

export class MoneyFormatValueConverter {
    toView(value, currency) {
        if (typeof value === "undefined" || ! value) { value = 0; }
        return "USD " + numeral(value).format('$0,0.00');
    } 
}