import { animate, state, style, transition, trigger } from "@angular/animations";
import { AngularFirestore } from "@angular/fire/firestore";
import { Vaccinato } from "../model/vaccinato.model";
import { CookieService } from "ngx-cookie-service";
import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    templateUrl: "./auth.component.html",
    styleUrls: ["./auth.component.css"],
    animations: [
        trigger('borders', [
            state('open', style({
                "background-color": "rgba(0, 0, 0, 0.4)",
                "border-radius": "5px 5px 0 0"
            })),
            state('closed', style({
                "border-radius": "5px"
            })),
            transition('open <=> closed', animate('0s ease-in-out')),
            transition('open <=> closed', animate('0.3s ease-in-out'))
        ]),
        trigger('arrow', [
            state('open', style({
                "transform": "rotate(180deg)"
            })),
            state('closed', style({
                "transform": "rotate(0deg)"
            })),
            transition('open <=> closed', animate('0s ease-in-out'))
        ]),
        trigger('menu', [
            state('open', style({
                "padding-bottom": "10px",
                height: "100%",
                "transform": "scaleY(1)"
            })),
            state('closed', style({
                "padding-bottom": "0",
                height: "0",
                "transform": "scaleY(0)"
            })),
            transition('open <=> closed', animate('0.4s ease-in-out'))
        ]),
        trigger('rect', [
            state('open', style({
                "color": "rgb(100, 100, 100)",
                fill: "rgb(100, 100, 100)"
            })),
            state('closed', style({
            })),
            transition('open <=> closed', animate('0.3s ease-in-out'))
        ])
    ]
})
export class AuthComponent
{
    open = [] as boolean[];
    vaccinati = [] as Vaccinato[];
    auth = false;
    constructor(firestore:AngularFirestore, router:Router, cs:CookieService)
    {
        if(cs.get("email") == "")
            router.navigateByUrl("/login");
        this.auth = cs.get("auth") == "true";

        firestore.collection("vaccinati").get().subscribe(data => {
            data.forEach(value => {
                if(value.data()["vaccino"] != "")
                {
                    var data = value.data();
                    this.vaccinati.push(new Vaccinato(data["cittaNascita"], data["codiceFiscale"], data["cognome"], data["dataNascita"], data["dataVaccino1"],
                    data["dataVaccino2"], data["genere"], data["indirizzo"], data["nazioneNascita"], data["nome"], data["provincia"], data["residenza"], data["vaccino"]));
                    this.open.push(false);
                }
            });
        });
    }

    private closeAll():void
    {
        for(let i = 0; i < this.open.length; i++)
            this.open[i] = false;
    }

    public onClick(i:number):void
    {
        if(this.open[i])
        {
            this.open[i] = false;
            return;
        }
        this.closeAll();
        this.open[i] = true;
    }

    public getVerb(data:string):string
    {
        let vars = data.split("/") as string[];
        return (new Date(vars[2] + "-" + vars[1] + "-" + vars[0]).getTime() > Date.now()) ? "Farà" : "Ha fatto";
    }
}