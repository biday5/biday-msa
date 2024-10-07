//src/model/category.model.ts
export interface CategoryModel {
    id: number;
    name: string;
    createdAt: Date;
    updatedAt: Date;
}


export const initialCategory: CategoryModel = {
    id: 0,
    name: '',
    createdAt: new Date(),
    updatedAt: new Date(),
};