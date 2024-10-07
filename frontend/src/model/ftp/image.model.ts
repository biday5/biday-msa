import {initialAward} from "@/model/auction/award.model";

export interface ImageModel {
    id?: number;
    originalName:string;
    uploadName:string;
    uploadPath:string;
    uploadUrl:string;
    url: string;
    referenceId: number;
    type: string;
    createdAt: Date;
    ratingValue: number;
}

export const initialImageModel : ImageModel = {
    id: 0,
    originalName:"",
    uploadName:"",
    uploadPath:"",
    uploadUrl:"",
    url: "",
    referenceId: 0,
    type: "",
    createdAt: new Date(),
    ratingValue: 0,
}
