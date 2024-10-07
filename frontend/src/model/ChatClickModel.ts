import {UserModel} from "@/model/UserModel";
import {ChatButtonModel} from "@/model/ChatButtonModel";

export interface ChatClickModel {
    id?: number;
    button: ChatButtonModel;
    user: UserModel;
    clickTime: Date;
}