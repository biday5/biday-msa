import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import LikeButton from "@/components/LikeButton";
import {StarIcon} from "@heroicons/react/24/solid";
import BagIcon from "@/components/BagIcon";


import {ClockIcon, NoSymbolIcon, SparklesIcon,} from "@heroicons/react/24/outline";
import IconDiscount from "@/components/IconDiscount";
import Prices from "@/components/Prices";
import SectionSliderProductCard from "@/components/SectionSliderProductCard";
import detail1JPG from "@/images/products/detail1.jpg";
import Policy from "./Policy";
import SectionPromo2 from "@/components/SectionPromo2";
import Image from "next/image";
import {AuctionModel} from "@/model/AuctionModel";
import {fetchProduct, fetchProductDetails} from "@/service/product/product.api";
import {ProductModel} from "@/model/ProductModel";
import {Route} from "@/routers/types";
import {fetchImage} from "@/service/image/image.api";
import {ImageModel} from "@/model/ImageModel";
import Link from "next/link";


export default async function ProductDetailPage({params}: { params: { id: string | string[]; }; }) {

    const {colorIds, product, size, auctions} = await fetchProductDetails(Number(params.id));


    const insertAuctionUrl = `/auction/insert?productId=${params.id}`;

    const getColor = () => {
        const parts = product.name.split("(");
        if (parts.length > 1) {
            return parts[1].replace(")","").trim();
        }
        return "";
    };

    const renderAuctionTable = () => {
        if (auctions == null || auctions.length == 0) {
            return <div>현재 진행중인 경매가 없습니다.</div>;
        }

        return (
            <div className="">
                <table className="text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
                    <thead
                        className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400 whitespace-nowrap">
                    <tr>
                        <th scope="col" className="px-6 py-3">
                            색상
                        </th>
                        <th scope="col" className="px-6 py-3">
                            사이즈
                        </th>
                        <th scope="col" className="px-6 py-3">
                            판매자
                        </th>
                        <th scope="col" className="px-6 py-3">
                            경매종료일
                        </th>
                        <th scope="col" className="px-6 py-3">
                            최고입찰가
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {auctions.map((auction: AuctionModel) => (
                        <tr className="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700"
                            key={auction.id}>
                            <Link href={`/auction/${auction.id}`}>
                                <th scope="row"
                                    className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                                    {getColor()}
                                </th>
                                <td className="px-6 py-4">
                                    {auction.size}
                                </td>
                                <td className="px-6 py-4">
                                    {auction.userId}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {auction.endedAt && !isNaN(new Date(auction.endedAt).getTime())
                                        ? new Date(auction.endedAt).toLocaleDateString()
                                        : "N/A"}
                                </td>
                                <td className="px-6 py-4">
                                    {auction.currentBid}
                                </td>
                            </Link>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        );
    };

    /* const renderStatus = () => {
         if (!status) {
             return null;
         }
         const CLASSES =
             "absolute top-3 left-3 px-2.5 py-1.5 text-xs bg-white dark:bg-slate-900 nc-shadow-lg rounded-full flex items-center justify-center text-slate-700 text-slate-900 dark:text-slate-300";
         if (status === "New in") {
             return (
                 <div className={CLASSES}>
                     <SparklesIcon className="w-3.5 h-3.5"/>
                     <span className="ml-1 leading-none">{status}</span>
                 </div>
             );
         }
         if (status === "50% Discount") {
             return (
                 <div className={CLASSES}>
                     <IconDiscount className="w-3.5 h-3.5"/>
                     <span className="ml-1 leading-none">{status}</span>
                 </div>
             );
         }
         if (status === "Sold Out") {
             return (
                 <div className={CLASSES}>
                     <NoSymbolIcon className="w-3.5 h-3.5"/>
                     <span className="ml-1 leading-none">{status}</span>
                 </div>
             );
         }
         if (status === "limited edition") {
             return (
                 <div className={CLASSES}>
                     <ClockIcon className="w-3.5 h-3.5"/>
                     <span className="ml-1 leading-none">{status}</span>
                 </div>
             );
         }
         return null;
     };*/

    const renderSectionContent = () => {
        return (
            <div className="space-y-7 2xl:space-y-8">
                {/* ---------- 1 HEADING ----------  */}
                <div>
                    <h2 className="text-2xl sm:text-3xl font-semibold">
                        {product.name}
                    </h2>
                    <h6 className="text-gray-300 text-left">
                        {product.subName}
                    </h6>

                    <div className="flex items-center mt-5 space-x-4 sm:space-x-5">
                        {/* <div className="flex text-xl font-semibold">$112.00</div> */}
                        <Prices
                            contentClass="py-1 px-2 md:py-1.5 md:px-3 text-lg font-semibold"
                            price={product.price}
                        />

                        <div className="h-7 border-l border-slate-300 dark:border-slate-700"></div>

                        <div className="flex items-center">
                            <a
                                className="flex items-center text-sm font-medium"
                            >
                                <StarIcon className="w-5 h-5 pb-[1px] text-yellow-400"/>
                                <div className="ml-1.5 flex">
                  <span className="text-slate-600 dark:text-slate-400">
                    {product.wishes} wishes
                  </span>
                                </div>
                            </a>
                            <span className="hidden sm:block mx-2.5">·</span>
                            <div className="hidden sm:flex items-center text-sm">
                                <SparklesIcon className="w-3.5 h-3.5"/>
                                <span className="ml-1 leading-none">임시 status</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/*  ---------- 4  QTY AND ADD TO CART BUTTON */}
                <div className="flex space-x-3.5">
                    <ButtonPrimary
                        className="flex-1 flex-shrink-0"
                        href={insertAuctionUrl as Route}
                    >
                        <BagIcon className="hidden sm:inline-block w-5 h-5 mb-0.5"/>
                        <span className="ml-3">판매하기</span>
                    </ButtonPrimary>
                </div>
                <hr className=" 2xl:!my-10 border-slate-200 dark:border-slate-700"></hr>
                <div className="flex-1 items-center justify-center mt-5 space-x-3.5">
                    {renderAuctionTable()}
                </div>

                {/* ---------- 5 ----------  */}
                {/*<AccordionInfo />*/}

                {/* ---------- 6 ----------  */}
                <div className="hidden xl:block">
                    <Policy/>
                </div>
            </div>
        );
    };

    const renderDetailSection = () => {
        return (
            <div className="">
                <h2 className="text-2xl font-semibold">Product Details</h2>
                <div className="prose prose-sm sm:prose dark:prose-invert sm:max-w-4xl mt-7">
                    {product.description}
                </div>
            </div>
        );
    };

    return (
        <div className={`nc-ProductDetailPage `}>
            {/* MAIn */}
            <main className="container mt-5 lg:mt-11">
                <div className="lg:flex">
                    {/* CONTENT */}
                    <div className="w-full lg:w-[55%] ">
                        {/* HEADING */}
                        <div className="relative">
                            <div className="aspect-w-16 aspect-h-16 relative">
                                <Image
                                    fill
                                    sizes="(max-width: 640px) 100vw, 33vw"
                                    src="/esafai/eListPrdImage523_1.jpg"
                                    className="w-full rounded-2xl object-cover"
                                    alt={"test"}
                                />
                            </div>
                            {/*{renderStatus()}*/}
                            {/* META FAVORITES */}
                            <LikeButton className="absolute right-3 top-3 "/>
                        </div>
                    </div>

                    {/* SIDEBAR */}
                    <div className="w-full lg:w-[45%] pt-10 lg:pt-0 lg:pl-7 xl:pl-9 2xl:pl-10">
                        {renderSectionContent()}
                    </div>
                </div>
                `
                {/* DETAIL AND REVIEW */}
                <div className="mt-12 sm:mt-16 space-y-10 sm:space-y-16">

                    {renderDetailSection()}

                    <hr className="border-slate-200 dark:border-slate-700"/>

                    <hr className="border-slate-200 dark:border-slate-700"/>

                    {/* OTHER SECTION */}
                    <SectionSliderProductCard
                        heading="Customers also purchased"
                        subHeading=""
                        headingFontClassName="text-2xl font-semibold"
                        headingClassName="mb-10 text-neutral-900 dark:text-neutral-50"
                    />

                    {/* SECTION */}
                    <div className="pb-20 xl:pb-28 lg:pt-14">
                        <SectionPromo2/>
                    </div>
                </div>
            </main>
        </div>
    );
};

