"use client";

import React, {Suspense, useEffect, useState} from "react";
import {ClockIcon, NoSymbolIcon, SparklesIcon,} from "@heroicons/react/24/outline";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import ButtonSecondary from "@/shared/Button/ButtonSecondary";
import NcImage from "@/shared/NcImage/NcImage";
import detail21JPG from "@/images/products/detail3-1.webp";
import detail22JPG from "@/images/products/detail3-2.webp";
import detail23JPG from "@/images/products/detail3-3.webp";
import detail24JPG from "@/images/products/detail3-4.webp";
import {PRODUCTS} from "@/data/data";
import IconDiscount from "@/components/IconDiscount";
import BagIcon from "@/components/BagIcon";
import toast from "react-hot-toast";
import {StarIcon} from "@heroicons/react/24/solid";
import SectionSliderProductCard from "@/components/SectionSliderProductCard";
import NotifyAddTocart from "@/components/NotifyAddTocart";
import {StaticImageData} from "next/image";
import LikeSaveBtns from "@/components/LikeSaveBtns";
import AccordionInfo from "@/components/AccordionInfo";
import ListingImageGallery from "@/components/listing-image-gallery/ListingImageGallery";
import {useParams, usePathname, useRouter, useSearchParams} from "next/navigation";
import {Route} from "next";
import {fetchAuction} from "@/service/auction/auction.api";
import {useMutation, useQuery} from "@tanstack/react-query";
import {ImageType} from "@/model/ImageModel";
import {fetchProduct, fetchProductOne} from "@/service/product/product.api";
import {BidModel, BidStreamModel} from "@/model/BidModel";
import {saveBid} from "@/api/bid/bid.api";
import {insertBid} from "@/service/bid/bid.api";
import {fetchImageFromClient} from "@/service/image/image.api";
import Cookies from "js-cookie";
import {useColor} from "@/hooks/useColor";

const LIST_IMAGES_GALLERY_DEMO: (string | StaticImageData)[] = [
    detail21JPG,
    detail22JPG,
    detail23JPG,
    detail24JPG,
    "https://images.pexels.com/photos/3812433/pexels-photo-3812433.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/1884581/pexels-photo-1884581.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/1127000/pexels-photo-1127000.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/292999/pexels-photo-292999.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/1778412/pexels-photo-1778412.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/871494/pexels-photo-871494.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
    "https://images.pexels.com/photos/2850487/pexels-photo-2850487.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
];


const PRICE = 108;



export default function AuctionDetailPage() {

    const searchParams = useSearchParams();
    const productId = searchParams.get("productId" || "0") as string;
    const router = useRouter();
    const {id} = useParams();

    const auction = useQuery({queryKey: ["auction"], queryFn: () => fetchAuction(Number(id))});
    const auctionImage = useQuery({queryKey: ["auctionImage"], queryFn: () => fetchImageFromClient(ImageType.AUCTION, id as string)});
    const product = useQuery({queryKey: ["product"], queryFn: () => fetchProductOne(1)});
    const productImage = useQuery({queryKey: ["productImage"], queryFn: () => fetchImageFromClient(ImageType.PRODUCT, productId)});

    if (!productImage.isLoading) {
        console.log("프로덕트 이미지", productImage.data);
    }

    if (!!auction.data) {
        console.log("불러온 옥션 id", auction.data.id);
    }

    // 이미지

    const {sizes, variants, status, allOfSizes, image} = PRODUCTS[0];

    const [message, setMessage] = useState();

    const [currentBid, setCurrentBid] = useState();

    const [highestBid, setHighestBid] = useState<number>();
    const [adjustBid, setAdjustBid] = useState<number>();

    const getColor = (productName: string) => {
        const parts = productName.split(`(`);
        if (parts.length > 1) {
            return parts[1].replace(')', '').trim();
        }
        return "";
    };

    const renderHighestBid = () => {

        const url = `${process.env.NEXT_PUBLIC_API_SERVER_URL}/api/bids/stream?auctionId=${Number(id)}`;
        useEffect(() => {
            console.log("useEffect 진입");

            const eventSource = new EventSource(url);

            eventSource.addEventListener("message", (event: MessageEvent) => {
                console.log("입찰 데이터 수신 완료, 데이터:", event.data);
                try {
                    const bidStream: BidStreamModel = JSON.parse(event.data);
                    setHighestBid(bidStream.currentBid);
                    setAdjustBid(bidStream.currentBid + 4000);
                    console.log("adjustBid", adjustBid);
                } catch (error) {
                    console.error("SSE 데이터 파싱 중 오류 발생", error);
                }
            });

            eventSource.addEventListener("open", () => {
                console.log("SSE 연결 완료");
            });

            eventSource.addEventListener("error", () => {
                console.error("SSE 오류 발생");
                if (eventSource.readyState === EventSource.CLOSED) {
                    console.log("연결 종료");
                }
            });

            return () => {
                console.log("연결 종료")
                eventSource.close();
            };

        }, [id]);

        useEffect(() => {
            if (highestBid !== undefined) {
                console.log("최고입찰가 갱신", highestBid);
            }
        }, [highestBid]);

        return <div>{highestBid}</div>;
    }





    const thisPathname = usePathname();
    const [variantActive, setVariantActive] = useState(0);
    const [sizeSelected, setSizeSelected] = useState(sizes ? sizes[0] : "");
    const [qualitySelected, setQualitySelected] = useState(1);
    const [isOpenModalViewAllReviews, setIsOpenModalViewAllReviews] =
        useState(false);

    //
    const handleCloseModalImageGallery = () => {
        let params = new URLSearchParams(document.location.search);
        params.delete("modal");
        router.push(`${thisPathname}/?${params.toString()}` as Route);
    };
    const handleOpenModalImageGallery = () => {
        router.push(`${thisPathname}/?modal=PHOTO_TOUR_SCROLLABLE` as Route);
    };

    //
    const renderVariants = () => {
        if (!variants || !variants.length) {
            return null;
        }

        return (
            <div>
                <label htmlFor="">
          <span className="text-sm font-medium">
            Color:
            <span className="ml-1 font-semibold">
              {product.isLoading? "" : getColor(product.data.name)}
            </span>
          </span>
                </label>
            </div>
        );
    };

    const mutation = useMutation({
        mutationFn: insertBid
    });

    const onClickBidButton = () => {
        const token = Cookies.get("token");
        if (!token) {
            router.push("/login");
            return;
        }

        if (!adjustBid) {
            return;
        }

        const currentBid = adjustBid;

        const bidData = {
            auctionId: Number(id),
            userId: "66f68ebf2bd718301c69f1e5",
            userName: "shull",
            userRole: "ROLE_USER",
            currentBid: currentBid,
        }

        mutation.mutate(bidData);

        toast.custom(
            (t) => (
                <NotifyAddTocart
                    productImage={image}
                    qualitySelected={qualitySelected}
                    show={t.visible}
                    sizeSelected={sizeSelected}
                    variantActive={variantActive}
                />
            ),
            {position: "top-right", id: "nc-product-notify", duration: 3000}
        );
    };

    const renderSizeList = () => {
        if (!allOfSizes || !sizes || !sizes.length) {
            return null;
        }
        return (
            <div>
                <div className="flex justify-between font-medium text-sm">
                    <label htmlFor="">
            <span className="">
              Size:
              <span className="ml-1 font-semibold">{auction.isLoading? "" : auction.data!!.size}</span>
            </span>
                    </label>
                    <a
                        target="_blank"
                        rel="noopener noreferrer"
                        href="##"
                        className="text-primary-6000 hover:text-primary-500"
                    >
                        See sizing chart
                    </a>
                </div>
            </div>
        );
    };

    const renderStatus = () => {
        if (!status) {
            return null;
        }
        const CLASSES =
            "text-sm flex items-center text-slate-700 text-slate-900 dark:text-slate-300";
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
    };

    const renderSectionSidebar = () => {
        return (
            <div className="listingSectionSidebar__wrap lg:shadow-lg">
                <div className="space-y-7 lg:space-y-8">
                    {/* PRICE */}
                    <div className="">
                        {/* ---------- 1 HEADING ----------  */}
                        <div className="flex items-center justify-between space-x-5">
                            <div className="flex text-2xl font-semibold">
                                {renderHighestBid()}원
                            </div>
                            <a
                                className="flex items-center text-sm font-medium"
                            >
                                <div className="">
                                    <StarIcon className="w-5 h-5 pb-[1px] text-orange-400"/>
                                </div>
                                <span className="ml-1.5 flex">
                  <span>4.9 </span>
                  <span className="mx-1.5">·</span>
                  <span className="text-slate-700 dark:text-slate-400">
                      {auction.isLoading? "입찰 없음" : `${auction.data!!.award.count} 입찰`}
                  </span>
                </span>
                            </a>
                        </div>

                        {/* ---------- 3 VARIANTS AND SIZE LIST ----------  */}
                        <div className="mt-6 space-y-7 lg:space-y-8">
                            <div className="">{renderVariants()}</div>
                            <div className="">{renderSizeList()}</div>
                        </div>
                    </div>
                    {/*  ---------- 4  QTY AND ADD TO CART BUTTON */}
                    <div className="flex space-x-3.5">
                        <div
                            className="flex items-center justify-center bg-slate-100/70 dark:bg-slate-800/70 px-2 py-3 sm:p-3.5 rounded-full">
                            {adjustBid ? `${adjustBid}` : "15000"}
                        </div>
                        <ButtonPrimary
                            className="flex-1 flex-shrink-0"
                            onClick={onClickBidButton}
                        >
                            <BagIcon className="hidden sm:inline-block w-5 h-5 mb-0.5"/>
                            <span className="ml-3">입찰 참여</span>
                        </ButtonPrimary>
                    </div>
                </div>
            </div>
        );
    };

    const renderSection1 = () => {
        return (
            <div className="listingSection__wrap !space-y-6">
                <div>
                    <h2 className="text-2xl md:text-3xl font-semibold">
                        {product.isLoading? "Loading..." : product.data!!.name}
                    </h2>
                    <div className="flex items-center mt-4 sm:mt-5">
                        <a
                            href="#reviews"
                            className="hidden sm:flex items-center text-sm font-medium "
                        >
                            <div className="">
                                <StarIcon className="w-5 h-5 pb-[1px] text-slate-800 dark:text-slate-200"/>
                            </div>
                            <span className="ml-1.5">
                <span>4.9</span>
                <span className="mx-1.5">·</span>
                <span className="text-slate-700 dark:text-slate-400 underline">
                  142 reviews
                </span>
              </span>
                        </a>
                        <span className="hidden sm:block mx-2.5">·</span>
                        {renderStatus()}

                        <div className="ml-auto">
                            <LikeSaveBtns/>
                        </div>
                    </div>
                </div>
                {/*  */}
                <div className="block lg:hidden">{renderSectionSidebar()}</div>

                {/*  */}
                <div className="w-14 border-b border-neutral-200 dark:border-neutral-700"></div>
                {/*  */}
                <AccordionInfo panelClassName="p-4 pt-3.5 text-slate-600 text-base dark:text-slate-300 leading-7"/>
            </div>
        );
    };

    const renderSection2 = () => {
        return (
            <div className="listingSection__wrap !border-b-0 !pb-0">
                <h2 className="text-2xl font-semibold">Product details</h2>
                {/* <div className="w-14 border-b border-neutral-200 dark:border-neutral-700"></div> */}
                <div className="prose prose-sm sm:prose dark:prose-invert sm:max-w-4xl">
                    {auction.isLoading? "" : auction.data!!.description}
                </div>
                {/* ---------- 6 ----------  */}
            </div>
        );
    };

    return (
        <div className={`ListingDetailPage nc-ProductDetailPage2`}>
            <>
                <header className="container mt-8 sm:mt-10">
                    <div className="relative ">
                        <div className="grid grid-cols-2 md:grid-cols-3 gap-3 lg:gap-6">
                            <div
                                className="md:h-full col-span-2 md:col-span-1 row-span-2 relative rounded-md sm:rounded-xl cursor-pointer"
                                onClick={handleOpenModalImageGallery}
                            >
                                <NcImage
                                    alt="firt"
                                    containerClassName="aspect-w-3 aspect-h-4 relative md:aspect-none md:absolute md:inset-0"
                                    className="object-cover rounded-md sm:rounded-xl"
                                    src={LIST_IMAGES_GALLERY_DEMO[0]}
                                    fill
                                    sizes="(max-width: 640px) 100vw, 50vw"
                                    priority
                                />
                                <div
                                    className="absolute inset-0 bg-neutral-900/20 opacity-0 hover:opacity-40 transition-opacity rounded-md sm:rounded-xl"></div>
                            </div>

                            {/*  */}
                            <div
                                className="col-span-1 row-span-2 relative rounded-md sm:rounded-xl overflow-hidden z-0 cursor-pointer"
                                onClick={handleOpenModalImageGallery}
                            >
                                <NcImage
                                    alt=""
                                    fill
                                    sizes="(max-width: 640px) 100vw, 50vw"
                                    containerClassName="absolute inset-0"
                                    className="object-cover w-full h-full rounded-md sm:rounded-xl"
                                    src={LIST_IMAGES_GALLERY_DEMO[1]}
                                />
                                <div
                                    className="absolute inset-0 bg-neutral-900/20 opacity-0 hover:opacity-40 transition-opacity"></div>
                            </div>

                            {/*  */}
                            {[LIST_IMAGES_GALLERY_DEMO[2], LIST_IMAGES_GALLERY_DEMO[3]].map(
                                (item, index) => (
                                    <div
                                        key={index}
                                        className={`relative rounded-md sm:rounded-xl overflow-hidden z-0 ${
                                            index >= 2 ? "block" : ""
                                        }`}
                                    >
                                        <NcImage
                                            alt=""
                                            fill
                                            sizes="(max-width: 640px) 100vw, 33vw"
                                            containerClassName="aspect-w-6 aspect-h-5 lg:aspect-h-4"
                                            className="object-cover w-full h-full rounded-md sm:rounded-xl "
                                            src={item || ""}
                                        />

                                        {/* OVERLAY */}
                                        <div
                                            className="absolute inset-0 bg-slate-900/20 opacity-0 hover:opacity-60 transition-opacity cursor-pointer"
                                            onClick={handleOpenModalImageGallery}
                                        />
                                    </div>
                                )
                            )}
                        </div>
                        <div
                            className="absolute hidden md:flex md:items-center md:justify-center left-3 bottom-3 px-4 py-2 rounded-xl bg-white text-slate-500 cursor-pointer hover:bg-slate-200 z-10"
                            onClick={handleOpenModalImageGallery}
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                className="h-5 w-5"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth={1.5}
                                    d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"
                                />
                            </svg>
                            <span className="ml-2 text-neutral-800 text-sm font-medium">
                Show all photos
              </span>
                        </div>
                    </div>
                </header>
            </>

            {/* MAIn */}
            <main className="container relative z-10 mt-9 sm:mt-11 flex ">
                {/* CONTENT */}
                <div className="w-full lg:w-3/5 xl:w-2/3 space-y-10 lg:pr-14 lg:space-y-14">
                    {renderSection1()}
                    {renderSection2()}
                </div>

                {/* SIDEBAR */}
                <div className="flex-grow">
                    <div className="hidden lg:block sticky top-28">
                        {renderSectionSidebar()}
                    </div>
                </div>
            </main>

            {/* OTHER SECTION */}
            <div className="container pb-24 lg:pb-28 pt-14 space-y-14">
                <hr className="border-slate-200 dark:border-slate-700"/>

                <SectionSliderProductCard
                    heading="Customers also purchased"
                    subHeading=""
                    headingFontClassName="text-2xl font-semibold"
                    headingClassName="mb-10 text-neutral-900 dark:text-neutral-50"
                />
            </div>

            {/* MODAL VIEW ALL REVIEW */}

            <Suspense>
                <ListingImageGallery
                    onClose={handleCloseModalImageGallery}
                    images={LIST_IMAGES_GALLERY_DEMO.map((item, index) => {
                        return {
                            id: index,
                            url: item,
                        };
                    })}
                />
            </Suspense>
        </div>
    );
};
