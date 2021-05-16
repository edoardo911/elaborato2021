import { CookieService } from "ngx-cookie-service";
import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({ templateUrl: "./auth.component.html" })
export class AuthComponent
{
    constructor(cs:CookieService, router:Router)
    {
        if(cs.get("email") == "")
            router.navigateByUrl("/login");
        if(cs.get("auth") == "true")
            alert("Utente autorizzato");
    }
}