import {useEffect, useState} from "react";

export const useColor = (productName: string) => {
    const [color, setColor] = useState<string>("");

    useEffect(() => {
        const getColor = (productName: string) => {
            const parts = productName.split("(");
            if (parts.length > 1) {
                return parts[1].replace(")", "").trim();
            }
            return "";
        };

        setColor(getColor(productName));
    }, [productName]);

    return color;
};