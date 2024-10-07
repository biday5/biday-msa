//src/app/collection/page.tsx
"use client"

import React, {FC, useState} from "react";
import Pagination from "@/shared/Pagination/Pagination";
import ButtonPrimary from "@/shared/Button/ButtonPrimary";
import SectionSliderCollections from "@/components/SectionSliderLargeProduct";
import SectionPromo1 from "@/components/SectionPromo1";
import ProductCard from "@/components/ProductCard";
import { PRODUCTS } from "@/data/data";
import TabFilters from "@/components/TabFilters";

export default function PageCollection({}){
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
      <div className={`nc-PageCollection`}>
        <div className="container py-16 lg:pb-28 lg:pt-20 space-y-16 sm:space-y-20 lg:space-y-28">
          <div className="space-y-10 lg:space-y-14">
            {/* HEADING */}
            <div className="max-w-screen-sm">
              <h2 className="block text-2xl sm:text-3xl lg:text-4xl font-semibold">
                상의
              </h2>
            </div>

            <hr className="border-slate-200 dark:border-slate-700"/>
            <main>
              {/* TABS FILTER */}
              <TabFilters/>

              {/* LOOP ITEMS */}
              <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-x-8 gap-y-10 mt-8 lg:mt-10">
                {selectedProducts.map((item, index) => (
                    <ProductCard data={item} key={index}/>
                ))}
              </div>

              <div
                  className="flex flex-col mt-12 lg:mt-16 space-y-5 sm:space-y-0 sm:space-x-3 sm:flex-row sm:justify-between sm:items-center">
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={handlePageChange}
                />
                <ButtonPrimary loading>Show me more</ButtonPrimary>
              </div>
            </main>
          </div>

          {/* === SECTION 5 === */}
          <hr className="border-slate-200 dark:border-slate-700"/>

          <SectionSliderCollections/>
          <hr className="border-slate-200 dark:border-slate-700"/>

          {/* SUBCRIBES */}
          <SectionPromo1/>
        </div>
      </div>
  );
};