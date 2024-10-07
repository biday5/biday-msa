interface DecodedToken{
    id:string;   //userId
    name:string; // username
    role:string;
}

export const extractUserInfoFromToken = (token: string): { id: string, name: string, role: string } => {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split('')
            .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
            .join('')
    );

    const payload = JSON.parse(jsonPayload);
    return {
        id: payload.id,
        name: payload.name,
        role: payload.role
    };
};