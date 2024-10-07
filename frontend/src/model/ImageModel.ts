export interface ImageModel {
    id?: string;
    originalName: string;
    uploadName: string;
    uploadPath: string;
    uploadUrl: string;
    type: string;
    referencedId: string;
    createdAt: Date;

}

export enum ImageType {
    AUCTION = "경매",
    PRODUCT = "상품",
    RATING = "평점",
}

export interface UploadImageParams {
    filePath: string;
    type: ImageType;
    referenceId: number;
    files: File[];
}