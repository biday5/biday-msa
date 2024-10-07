import {fetchProduct} from "@/service/product/product.api";
import {useEffect, useState} from "react";
import {ProductModel} from "@/model/ProductModel";

export default async function useProduct(id: number) {
    const [product, setProduct] = useState<any>(null);

    useEffect(() => {
        const fetchProductData = async () => {
            try {
                const data = await fetchProduct(id);
                setProduct(data);
            } catch (error) {
                console.error(error)
            }
        };

        fetchProductData();
    }, []);

    const {brand, category, name, subName, price, color, description, auctions} = product || {};

    return {brand, category, name, subName, price, color, description, auctions}

}
