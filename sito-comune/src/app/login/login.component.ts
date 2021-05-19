import { animate, state, style, transition, trigger } from "@angular/animations";
import { FormControl, FormGroup } from "@angular/forms";
import { Component } from "@angular/core";
import { AngularFirestore } from "@angular/fire/firestore";
import { Router } from "@angular/router";
import { CookieService } from "ngx-cookie-service";

@Component({
    templateUrl: './login.component.html',
    animations: [
        trigger('tent', [
            state('open', style({
                transform: 'scaleY(1)',
                height: '28px'
            })),
            state('closed', style({
                transform: 'scaleY(0)',
                height: '0px'
            })),
            transition('open <=> closed', animate('0.4s ease-in-out'))
        ])
    ]
})
export class LoginComponent
{
    error = false;
    formGroup = new FormGroup({
        email: new FormControl(''),
        pwd: new FormControl(''),
    });

    constructor(private firestore:AngularFirestore, private router:Router, private cs:CookieService) {}

    public onSubmit():void
    {
        let email = this.formGroup.value["email"];
        let pwd = this.formGroup.value["pwd"];

        this.error = false;
        document.getElementById("email").classList.remove("error");
        document.getElementById("pwd").classList.remove("error");

        if(email == "")
        {
            document.getElementById("email").focus();
            document.getElementById("email").classList.add("error");
        }
        else if(pwd == "")
        {
            document.getElementById("pwd").focus();
            document.getElementById("pwd").classList.add("error");
        }
        else
        {
            var logged = false;
            var auth:string;
            this.firestore.collection("users").snapshotChanges().subscribe(data => {
                data.forEach(value => {
                    if(value.payload.doc.data()["email"] == email && value.payload.doc.data()["pwd"] == pwd)
                    {
                        logged = true;
                        auth = value.payload.doc.data()["auth"];
                        return;
                    }
                });

                if(logged)
                {
                    this.cs.set("auth", auth, 1);
                    this.cs.set("email", email, 2);
                    this.router.navigateByUrl("/");
                }
                else
                    this.error = true;
            });
        }
    }
}