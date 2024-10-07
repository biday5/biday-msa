'use client'
import React, {FC, useState} from "react";
import Pagination from "@/shared/Pagination/Pagination";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import SectionSliderCollections from "@/components/SectionSliderLargeProduct";
import SectionPromo1 from "@/components/SectionPromo1";
import HeaderFilterSearchPage from "@/components/HeaderFilterSearchPage";
import Input from "@/shared/Input/Input";
import ButtonCircle from "@/shared/Button/ButtonCircle";
import ProductCard from "@/components/ProductCard";
import { PRODUCTS } from "@/data/data";
import { RootState } from "@/lib/store";

export default function PageSearch({}){
    const itemsPerPage = 20; // 한 페이지에 20개씩 (가로 5 , 세로 4개)
    const totalPages = Math.ceil(PRODUCTS.length / itemsPerPage);

    // 현재 페이지 상태를 관리
    const [currentPage, setCurrentPage] = useState(1);

    const startIndex = (currentPage -1) * itemsPerPage;
    const selectedProducts = PRODUCTS.slice(startIndex, startIndex + itemsPerPage);

    // 페이지 처리 클릭 핸들러
    const handlePageChange = (page: number) => {
        console.log("페이지 변경", page)
        setCurrentPage(page);
    };


  return (
    <div className={`nc-PageSearch`} data-nc-id="PageSearch">
      <div
        className={`nc-HeadBackgroundCommon h-24 2xl:h-28 top-0 left-0 right-0 w-full bg-primary-50 dark:bg-neutral-800/20 `}
      />
      <div className="container">
        <header className="max-w-2xl mx-auto -mt-10 flex flex-col lg:-mt-7">

        </header>
      </div>

      <div className="container py-16 lg:pb-28 lg:pt-20 space-y-16 lg:space-y-28">
        <main>
          {/* FILTER */}
          <HeaderFilterSearchPage />

          {/* LOOP ITEMS */}
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-x-8 gap-y-10 mt-8 lg:mt-10">
            {PRODUCTS.map((item, index) => (
              <ProductCard data={item} key={index} />
            ))}
          </div>

          {/* PAGINATION */}
          <div className="flex flex-col mt-12 lg:mt-16 space-y-5 sm:space-y-0 sm:space-x-5 sm:flex-row sm:justify-between sm:items-center">
            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={handlePageChange}
            />
            <ButtonPrimary loading>Show me more</ButtonPrimary>
          </div>
        </main>

        {/* === SECTION 5 === */}
        <hr className="border-slate-200 dark:border-slate-700" />
        <SectionSliderCollections />
        <hr className="border-slate-200 dark:border-slate-700" />

        {/* SUBCRIBES */}
        <SectionPromo1 />
      </div>
    </div>
  );
};