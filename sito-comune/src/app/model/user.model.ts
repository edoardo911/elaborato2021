export class User
{
    private email:string;
    private auth:boolean;

    constructor(email:string, auth:boolean)
    {
        this.email = email;
        this.auth = auth;
    }
}