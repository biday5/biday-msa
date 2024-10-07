//src/api/fetch.ts

export const fetchAPI = (url: string, options?: RequestInit) => {
    const baseURL = process.env.NEXT_PUBLIC_API_SERVER_URL || 'http://localhost:8080';
    return fetch(`${baseURL}${url}`, options);
};


