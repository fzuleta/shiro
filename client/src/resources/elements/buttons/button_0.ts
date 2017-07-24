import {bindable, inject, customElement} from 'aurelia-framework'; 
import {trace} from "./../../../_/common/functions";
import * as $ from "jquery"

@customElement('button-0') // Define the name of our custom element
@inject(Element) // Inject the instance of this element
export class Button_0 {
    element;


    btnclass="btn-empty-custom-element";

    @bindable iconsrc="";
    @bindable iconwidth="30";
    @bindable iconheight="30";
    @bindable text="";
    @bindable btntype="";
    constructor(element) {
        this.element = element;
    }

    // this is called by aurelia on the bindeable changed
    btntypeChanged(){ 
        switch (this.btntype) {
            case "blue":
                this.btnclass = "btn-blue-custom-element"
                break;
            
            default:
                this.btnclass = "btn-empty-custom-element"
                break;
        }
    }

    attached(){  
    }
}