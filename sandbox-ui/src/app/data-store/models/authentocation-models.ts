export interface IUserInfo{
    userName:string;
    isLoggedIn:boolean;
    roles : string[];
}

export class LoginRequestParam{
    userName:string;
    password : string;
}