import { animate, state, style, transition, trigger } from "@angular/animations";
import { AngularFirestore } from "@angular/fire/firestore";
import { FormControl, FormGroup } from "@angular/forms";
import { CookieService } from "ngx-cookie-service";
import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    templateUrl: './register.component.html',
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
            transition('open => closed', animate('0.4s ease-in-out')),
            transition('closed => open', animate('0.4s ease-in-out'))
        ])
    ]
})
export class RegisterComponent
{
    message = "";
    error = false;
    formGroup = new FormGroup({
        email: new FormControl(''),
        pwd: new FormControl(''),
        pwd_confirm: new FormControl('')
    });

    constructor(private firestore:AngularFirestore, private router:Router, private cs:CookieService) {}

    public onSubmit():void
    {
        let email = this.formGroup.value['email'] as string;
        let pwd = this.formGroup.value['pwd'] as string;
        let confirm = this.formGroup.value['pwd_confirm'] as string;

        this.error = false;
        document.getElementById("email").classList.remove("error");
        document.getElementById("pwd").classList.remove("error");
        document.getElementById("pwd_confirm").classList.remove("error");

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
        else if(confirm == "")
        {
            document.getElementById("pwd_confirm").focus();
            document.getElementById("pwd_confirm").classList.add("error");
        }
        else if(!email.match(/(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/))
        {
            this.message = "Email non valida"
            this.error = true;
        }
        else if(pwd.length < 8)
        {
            this.message = "La password deve avere minimo 8 caratteri"
            this.error = true;
        }
        else if(pwd != confirm)
        {
            this.message = "Le password non coincidono"
            this.error = true;
        }
        else
        {
            var emailTaken = false;
            this.firestore.collection("users").snapshotChanges().subscribe(data => {
                data.forEach(value => {
                    if(value.payload.doc.data()["email"] == email)
                    {
                        emailTaken = true;
                        return;
                    }
                });

                if(!emailTaken)
                {
                    this.firestore.collection("users").add({ email: email, pwd: pwd, auth: false });
                    this.cs.set("email", email, 2);
                    this.router.navigateByUrl("/");
                }
                else
                {
                    this.message = "Email già in uso"
                    this.error = true;
                }
            });
        }
    }
}