/*
//src/service/user/delete.api.ts
export async function deleteUser(id: number): Promise<void | { status: number }> {

    console.log("delete.api : ",id);
    try {
        const response = await fetch(`http://211.188.54.218:8080/users/${id}/cancel`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error('Failed to delete user');
        }

    } catch (e) {
        console.error('Error deleting user:', e);
        return { status: 500 };
    }
}
*/
