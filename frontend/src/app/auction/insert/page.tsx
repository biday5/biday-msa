'use client';

import Label from "@/components/Label/Label";
import React, {ChangeEvent, FC, FormEvent, useEffect, useState} from "react";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import Select from "@/shared/Select/Select";
import Textarea from "@/shared/Textarea/Textarea";
import Image from "next/image";
import {ProductModel} from "@/model/ProductModel";
import {useQuery} from "@tanstack/react-query";
import {fetchAllProducts} from "@/service/product/product.api";
import {ImageType} from "@/model/ImageModel";
import {fetchImageFromClient} from "@/service/image/image.api";

const productCard = () => {

};

type ModalProps = {
    isOpen: boolean,
    onClose: any,
    onSelectProduct: ProductModel,
    products: ProductModel[];
}

const Modal = ({isOpen, onClose, onSelectProduct, products}: ModalProps) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div className="bg-white p-6 rounded-md w-1/3">
                <h3 className="text-lg font-semibold mb-4"> 상품 선택 </h3>
                <ul>
                    {products.map((product: ProductModel) => (
                        <li
                            key={product.id}
                            className="py-2 cursor-pointer hover:bg-grey-100"
                            // onClick={onSelectProduct(product)}
                            value={product.id}
                        >
                            {product.name}
                        </li>
                    ))}
                </ul>
                <button
                    className="mt-4 bg-red-500 text-white py-2 px-4 rounded">
                    닫기
                </button>
            </div>
        </div>
    );
};

export default function InsertAuction(productId?: number) {
    const [selectedProduct, setSelectedProduct] = useState<ProductModel>();
    const [description, setDescription] = useState("");
    const [endDate, setEndDate] = useState();
    const [isOpen, setIsOpen] = useState(false);
    const [duration, setDuration] = useState<number>(3);
    const [files, setFiles] = useState<File[]>([])

    const productList= useQuery({queryKey: ["products"], queryFn: fetchAllProducts});
    const productImages = useQuery({queryKey: ["products"], queryFn: () => fetchImageFromClient(ImageType.PRODUCT) });


    useEffect(() => {
        if (!productList.isLoading && productId) {
            const foundProduct = productList.data!.find((item) => item.id === productId);
            foundProduct ? setSelectedProduct(foundProduct) : null;
        }
    }, [productList, productId]);

    if (productList.error instanceof Error) return <div>Error: {productList.error.message}</div>;
    // 에러 페이지로 변경

    if (!productList.isLoading) {
        console.log(productList);
    }

    const openModal = () => {
        setIsOpen(true);
    };

    const closeModal = () => {
        setIsOpen(false);
    };

    const handleProductSelect = () => {
        closeModal();
    };

    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            const selectedFiles = Array.from(e.target.files).slice(0, 3);
            setFiles(selectedFiles);
        }
    };


    const durationSelectButton = () => {
        const durations = [3, 5, 7];

        const handleClick = (days: number) => {
            setDuration(days);
        };

        return ((
            <div className="flex">
                {durations.map((days, index) => (
                    <button
                        className={`rounded-md border border-slate-300 py-2 px-4 text-center text-sm transition-all shadow-sm
                        hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800
                        active:border-slate-800 active:text-white active:bg-slate-800
                        disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none
                        ${duration === days ? `border-state-800 text-white bg-slate-800` : `border-slate-300`}
                        ${index !== durations.length - 1 ? "mr-4" : ""}`}
                        key={days}
                        onClick={() => handleClick(days)}>
                        {days}일
                    </button>
                ))}
            </div>
        ))
    };

    const getDuration = (days: number) => {
        const currentDate = new Date();
        currentDate.setMinutes(Math.round(currentDate.getMinutes() / 5) * 5);
        const endDate = currentDate.getDate() + days;

        return (
                `${currentDate.toLocaleDateString()} ${String(currentDate.getHours()).padStart(2, '0')} : ${String(currentDate.getMinutes()).padStart(2, '0')} -
                ${endDate.toLocaleString()}`

        );
    };

    const renderUploadBox = () => {
        return (
            <div className="flex items-center justify-center">
                <label htmlFor="dropzone-file"
                       className="flex flex-col items-center justify-center w-1/2 h-64 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-gray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600">
                    <div className="flex flex-col items-center justify-center pt-5 pb-6">
                        <svg className="w-8 h-8 mb-4 text-gray-500 dark:text-gray-400" aria-hidden="true"
                             xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
                            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round"
                                  strokeWidth="2"
                                  d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"/>
                        </svg>
                        <p className="mb-2 text-sm text-gray-500 dark:text-gray-400"><span
                            className="font-semibold">Click to upload</span> or drag and drop</p>
                        <p className="text-xs text-gray-500 dark:text-gray-400">PNG, JPG or GIF (MAX.
                            800x400px)</p>
                    </div>
                    <input
                        id="dropzone-file"
                        type="file"
                        className="hidden"
                        multiple
                        accept="image/png, image/jpeg, image/gif"
                        onChange={handleFileChange}
                    />
                </label>
            </div>
        );
    };

    const isFormValid = !!(selectedProduct && description && duration && files.length > 0);

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();

        const formData = new FormData();
        if (isFormValid) {
            // formData.append("productId", selectedProduct.id.toString());
            // formData.append("duration", duration.toString());
        }

    };

    const renderSelectProductButton = () => {
        if (productList.isLoading) {
            return <button disabled type="button"
                           className="justify-center py-2.5 px-5 me-2 text-sm font-medium text-gray-900 bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:outline-none focus:ring-blue-700 focus:text-blue-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700 inline-flex items-center">
                <svg aria-hidden="true" role="status"
                     className="inline w-4 h-4 me-3 text-gray-200 animate-spin dark:text-gray-600"
                     viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                        d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                        fill="currentColor"/>
                    <path
                        d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                        fill="#1C64F2"/>
                </svg>
                Loading...
            </button>
        } else {
            return <button
                className="py-2.5 px-5 me-2 mb-2 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100 dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700"
                type="button"
                id="product"
                onClick={openModal}
            >
                {selectedProduct ? (
                        <>
                            <img src={selectedProduct.image.uploadUrl} alt={selectedProduct.name}/>
                            <span>{selectedProduct.name}</span>
                        </>
                    ) :
                    <div>상품 선택</div>
                }
            </button>
        }

    };

    const renderDescription = () => {
        return (
            <div className="itmes-end">
                <div className="relative w-full min-w-[200px]">
                <textarea placeholder="상품설명"
                          className="peer h-full min-h-[100px] w-full resize-none border-b border-blue-gray-200 bg-transparent pt-4 pb-1.5 font-sans text-sm font-normal text-blue-gray-700 outline outline-0 transition-all placeholder-shown:border-blue-gray-200 focus:border-gray-900 focus:outline-0 disabled:resize-none disabled:border-0 disabled:bg-blue-gray-50"></textarea>
                    <label
                        className="after:content[' '] pointer-events-none absolute left-0 -top-2.5 flex h-full w-full select-none text-sm font-normal leading-tight text-blue-gray-500 transition-all after:absolute after:-bottom-1 after:block after:w-full after:scale-x-0 after:border-b-2 after:border-gray-900 after:transition-transform after:duration-300 peer-placeholder-shown:leading-tight peer-placeholder-shown:text-blue-gray-500 peer-focus:text-sm peer-focus:leading-tight peer-focus:text-gray-900 peer-focus:after:scale-x-100 peer-focus:after:border-gray-900 peer-disabled:text-transparent peer-disabled:peer-placeholder-shown:text-blue-gray-500">
                    </label>
                </div>
            </div>
        );
    };

    return (
        <form onSubmit={handleSubmit} className="flex justify-center items-center h-screen mx-auto">
            <div className="w-3/4 items-center mt-10 mb-10 ml-10">
                <div className="w-full mb-6">
                    <Label className="block">
                        상품:
                    </Label>
                    {renderSelectProductButton()}
                    <input type="hidden" name="productId" value={selectedProduct?.id}/>
                </div>
                <div className="w-full mb-6">
                    <Label>
                        경매 기간:
                    </Label>
                    <span className="ml-2 rfont-light accent-gray-400">{getDuration(duration)}</span>
                    {durationSelectButton()}
                    <input type="hidden" name="duration"/>
                    {/* input value 설정 필요 */}
                </div>
                <div className="w-full mb-6">
                    <Label>
                        업로드 이미지:
                    </Label>
                    {renderUploadBox()}
                </div>
                {renderDescription()}

                {/*<Modal
                    isOpen={isOpen}
                    onClose={closeModal}
                    onSelectProduct={handleProductSelect}
                    products={productList}
                />
*/}
                <button type="submit"
                        className={`mt-4 py-2 px-4 rounded text-white ${isFormValid ? 'bg-blue-500 hover:bg-blue-700' : 'bg-gray-400 cursor-not-allowed'}`}
                        disabled={!isFormValid}>
                    경매 등록
                </button>
            </div>

        </form>
    );

};