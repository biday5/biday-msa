import {ProductDictionary, ProductModel} from "@/model/ProductModel";
import {fetchAuctionList} from "@/service/auction/auction.api";

const baseUrl = `${process.env.NEXT_PUBLIC_API_SERVER_URL}/api/products`

export async function insertProduct(product: ProductModel): Promise<any | { status: number }> {
    try {

        const response = await fetch(baseUrl, {
                method: 'POST',
                headers: {
                    'Content-Type' : 'application/json'
                    // 토큰 필요
                },
                body: JSON.stringify(product)
            }
        );

        return await response.json();

    } catch (error) {
        console.error("상품 등록 중 오류 발생: ", error);
        return {status: 500}
    }
}
export async function fetchProduct(id?:number) {
    try {

        let url = baseUrl;

        id ? url += `?id=${id}` : url += "/findAll";

        const response = await fetch(url, {cache: "no-store"});

        console.log("확인용",response.ok);


        if (response.ok) {
            const data = await response.json();
            console.log("지금 만든 최신 확인", data);
            return data;

        } else {
            console.log("실패")
            const data = await response.json();
            console.log("실패인 경우", data);
            return {...data, id: 0}

        }


    } catch (error) {
        console.error("상품 데이터 로드 중 오류 발생", error);
        throw new Error("상품 데이터 로드 실패");

    }
}

export async function getProductList() {
    try {
        const response = await fetch(baseUrl, {
            method: 'GET'
        });

        const data = await response.json();

        console.log("+++++>" + JSON.stringify(data));

        return data;
    } catch (error) {
        console.error("상품 리스트 로드 중 오류 발생", error);
        return { status: 500 };
    }
}

export async function fetchProductDetails(id: number): Promise<any> {

    // 옥션 api 호출
    // 이미지 api 호출
    // 컬러 id들 (누르면 이동),

    try {
        const products: ProductDictionary[] = await fetchProduct(id);
        const productDict = products.find((item) => Object.keys(item)[0] === String(id));
        if (!productDict) {
            throw new Error(`id: ${id} 에 해당하는 상품 탐색 불가`)
        }

        // 프로덕트 색상에 따른 키값만 뽑기
        const colorIds = products.map((item) => Object.keys(item)[0]);

        // 해당 상품 키값 뽑아서 키는 productId, value는 color인 배열로 만들기


        console.log("추출 키값 배열 확인", colorIds);

        // product 정보
        const product = productDict[String(id)];

        console.log("product 객체 확인", productDict);
        console.log("sizes 객체 확인", product.sizes);

        // sizes
        const sizes = product.sizes.map((size) => size.id);

        console.log("sizes 배열 확인", sizes);

        const auctionArray = await Promise.all(sizes.map((size) => {
            console.log("사이즈 넘어오는지", size);
            return fetchAuctionList(size);
        }));

        const auctions = auctionArray.flat(Infinity);

        console.log("평탄화", auctions);

        const size = product.sizes.map((size) => size.size);


        console.log("auctions 배열 확인", auctions);

        // const image = await fetchImage(String(id), ImageType.PRODUCT);

        // console.log("image 객체 확인", image);

        /**
         * 해당 상품, 컬러 다른 상품 라우트용 id, 컬러 다른 상품 color,
         */
        return {colorIds, product, size, auctions};

    } catch (error) {
        console.error("상품 상세 정보 로드 중 오류 발생: product.api.ts fetchProductDetails", error);
        throw new Error("상품 상세 정보 로드 실패");
    }
}

// 상품 삭제
export async function deleteProduct(id:number) {
    try {
        const url = baseUrl + `/${id}`;

        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type' : 'application/json'
            },

        });

        const data: any = await response;

        return data;

    } catch (error) {
        console.error("상품 개별 삭제 중 오류 발생", error);

        return {status: 500};
    }

}

// 전체 상품 목록
export async function fetchAllProducts(): Promise<ProductModel[]> {
    const url = baseUrl + "/findAll";

    try {
        const response = await fetch(url, {
            cache: "no-store",
            method: "GET",
        });

        console.log("응답 객체 확인: ", response); // 응답 로그 확인

        if (response.ok) {
            const data = response.json();
            console.log("프로덕트 데이터 확인", data);
            return data;
        } else {
            throw new Error(`상품 전체 정보 로드 실패 status: ${response.status}`);

        }

    } catch (error) {
        console.error("상품 전체 로드 중 오류 발생: product.api.ts fetchAllProducts", error);
        throw new Error("상품 전체 정보 로드 실패");
    }
}

// 하나 불러오기
export async function fetchProductOne(id:number) {

    try {

        const url = baseUrl+`/findOne?id=${id}`;

        const response = await fetch(url, {cache: "no-store"});

        console.log("확인용",response.ok);

        return (await response.json())[String(id)];

    } catch (error) {
        console.error("상품 데이터 로드 중 오류 발생", error);
        throw new Error("상품 데이터 로드 실패");

    }
}