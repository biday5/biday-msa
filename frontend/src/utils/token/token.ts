//src/utils/token/token.ts
import type { NextApiRequest, NextApiResponse } from 'next';
import {setItem,removeItem} from "@/utils/storage/storage.api";
import {setCookie} from "undici-types";
import {removeCookie} from "@/utils/cookie/cookie.api";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    if (req.method === 'GET') {
        const { returnUrl } = req.query;
        // Your logic to generate token data
        res.status(200).json({
            enc_data: 'example_enc_data',
            token_version_id: 'example_token_version_id',
            integrity_value: 'example_integrity_value'
        });
    } else {
        res.setHeader('Allow', ['GET']);
        res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}

