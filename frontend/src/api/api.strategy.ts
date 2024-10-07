//src/api/api.strategy.ts
import { fetchAPI } from './fetch';

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | "PATCH" ;

interface RequestOptions {
    method: HttpMethod;
    params?: Record<string, string>;
    data?: any;
}

const createQueryString = (params?: Record<string, string>) =>
    params ? `?${new URLSearchParams(params)}` : '';

const apiRequest = async (url: string, { method, params, data }: RequestOptions) => {
    const queryString = createQueryString(params);
    const options: RequestInit = {
        method,
        headers: {
            'Content-Type': 'application/json',
        },
        ...(data && { body: JSON.stringify(data) }),
    };

    const response = await fetchAPI(`${url}${queryString}`, options);
    return response.json();
};

export const strategy = {
    GET: (url: string, params?: Record<string, string>) =>
        apiRequest(url, { method: 'GET', params }),
    POST: (url: string, data: any, params?: Record<string, string>) =>
        apiRequest(url, { method: 'POST', data, params }),
    PUT: (url: string, data: any) =>
        apiRequest(url, { method: 'PUT', data }),
    DELETE: (url: string) =>
        apiRequest(url, { method: 'DELETE' }),
    PATCH: (url: string, data: any) =>
        apiRequest(url, { method: 'PATCH', data })
};