import {useEffect, useState} from "react";

export default function useTranId(): string  {
    const [randomId, setRandomId] = useState('');

    const generateRandomId = () => {
        const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        let result = "";
        const length = 16;

        for (let i = 0; i < length; i++) {
            const randomId = Math.floor(Math.random() * characters.length);
            result += characters[randomId];
        }

        return result;
    }

    useEffect(() => {
        setRandomId(generateRandomId());
    }, []);

    return randomId;
};