'use client'

import React, {FC, useState} from "react";
import SectionSliderCollections from "@/components/SectionSliderLargeProduct";
import SectionPromo1 from "@/components/SectionPromo1";
import ProductCard from "@/components/ProductCard";
import { PRODUCTS } from "@/data/data";
import SidebarFilters from "@/components/SidebarFilters";
import Pagination from "@/shared/Pagination/Pagination";
import { RootState } from "@/lib/store";

export default function PageCollection2({}){
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
    <div className={`nc-PageCollection2`}>
      <div className="container py-16 lg:pb-28 lg:pt-20 space-y-16 sm:space-y-20 lg:space-y-28">
        <div className="space-y-10 lg:space-y-14">
          {/* HEADING */}
          <div className="max-w-screen-sm">
            <h2 className="block text-2xl sm:text-3xl lg:text-4xl font-semibold">
              하의
            </h2>
          </div>

          <hr className="border-slate-200 dark:border-slate-700" />
          <main>
            {/* LOOP ITEMS */}
            <div className="flex flex-col lg:flex-row">
              <div className="flex-shrink-0 mb-10 lg:mb-0 lg:mx-4 border-t lg:border-t-0"></div>
              <div className="flex-1 ">
                <div className="flex-1 grid sm:grid-cols-2 xl:grid-cols-5 gap-x-8 gap-y-10 ">
                  {selectedProducts.map((item, index) => (
                    <ProductCard data={item} key={index} />
                  ))}
                </div>
                {/* Pagination */}
                <div className="mt-8">
                  <Pagination
                      currentPage={currentPage}
                      totalPages={totalPages}
                      onPageChange={handlePageChange}
                  />
                </div>
              </div>
            </div>
          </main>
        </div>

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