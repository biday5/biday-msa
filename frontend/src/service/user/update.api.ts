/*
import { UserModel } from "@/model/UserModel";

export const updateUser = async (id: number, user: UserModel): Promise<Response> => {
    const response = await fetch(`http://your-api-url/users/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    });
    if (!response.ok) {
        throw new Error('Failed to update user');
    }
    return response;
};1*/
