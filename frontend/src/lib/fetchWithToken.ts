import {handleReissueToken} from "@/utils/reissue/reissueToken";

const fetchWithToken = async (url: string, token: string, options: RequestInit = {}):Promise<Response> => {

    const headers = {
        ...options.headers,
        ...(token ? {'Authorization': `Bearer ${token}`} : {}),
    };

    const response = await fetch(url, {
        ...options,
        headers,
    });

    if (response.status === 401) {
        await handleReissueToken();
        return fetchWithToken(url, token, options);
    }

    return response;
};

export default fetchWithToken;