import { animate, state, style, transition, trigger } from "@angular/animations";
import { CookieService } from "ngx-cookie-service";
import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    selector: "auth-header",
    templateUrl: "./header.component.html",
    animations: [
        trigger('menu', [
            state('open', style({
                'margin-left': '0px'
            })),
            state('closed', style({
                'margin-left': '-100vw'
            })),
            transition('open => closed', animate('0.4s ease-in-out')),
            transition('closed => open', animate('0.4s ease-in-out'))
        ]),
        trigger('center', [
            state('open', style({
                width: '80%'
            })),
            state('shrink', style({
                width: '0%'
            })),
            transition('open => shrink', animate('0.4s ease-in-out')),
            transition('shrink => open', animate('0.4s ease-in-out'))
        ]),
        trigger('top', [
            state('hamburger', style({
                transform: 'rotate(0deg)',
                width: '80%'
            })),
            state('cross', style({
                transform: 'rotate(37deg)',
                width: '95%'
            })),
            transition('cross => hamburger', animate('0.5s ease-in-out')),
            transition('hamburger => cross', animate('0.5s ease-in-out'))
        ]),
        trigger('bottom', [
            state('hamburger', style({
                transform: 'rotate(0deg)',
                width: '80%'
            })),
            state('cross', style({
                transform: 'rotate(-37deg)',
                width: '95%'
            })),
            transition('cross => hamburger', animate('0.5s ease-in-out')),
            transition('hamburger => cross', animate('0.5s ease-in-out'))
        ])
    ]
})
export class HeaderComponent
{
    menu_open = false;

    constructor(private router:Router, private cs:CookieService) {}

    public logout():void
    {
        this.cs.delete("email");
        this.cs.delete("auth");
        this.router.navigateByUrl("/login");
    }

    public toggleMenu():void { this.menu_open = !this.menu_open; }
}