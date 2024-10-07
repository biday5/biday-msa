import {UserModel} from "@/model/user/user.model";

interface ChatClickModel {
    id?: number;
    //button: ChatButtonModel;
    user: UserModel;
    clickTime: Date;
}