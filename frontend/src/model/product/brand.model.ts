//src/model/BrandeModel.ts
export interface BrandModel {
    id: number;
    name: string;
    createdAt: Date;
    updatedAt: Date;
}


export const initialBrand: BrandModel = {
    id: 0,
    name: '',
    createdAt: new Date(),
    updatedAt: new Date(),
};