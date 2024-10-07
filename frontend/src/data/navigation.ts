import { NavItemType } from "@/shared/Navigation/NavigationItem";
import ncNanoId from "@/utils/ncNanoId";

export const MEGAMENU_TEMPLATES: NavItemType[] = [
  {
    id: ncNanoId(),
    href: "/#",
    name: "홈 페이지",
    children: [
      { id: ncNanoId(), href: "/search", name: "검색 페이지" },
    ],
  },
  {
    id: ncNanoId(),
    href: "/#",
    name: "쇼핑 페이지",
    children: [
      { id: ncNanoId(), href: "/cart", name: "장바구니" },
    ],
  },
  {
    id: ncNanoId(),
    href: "/#",
    name: "기타 페이지",
    children: [
      { id: ncNanoId(), href: "/checkout", name: "결제 페이지" },
    ],
  },
  {
    id: ncNanoId(),
    href: "/#",
    name: "블로그 페이지",
    children: [
      { id: ncNanoId(), href: "/about", name: "페이지 소개 " },
    ],
  },
];

const OTHER_PAGE_CHILD: NavItemType[] = [
  {

    id: ncNanoId(),
    href: "/",
    name: "홈1 (현재 진행중인 경매)",
  },
  {
    id: ncNanoId(),
    href: "/home-2",
    name: "홈2 (마감 입박중인 경매)",
  },
  {
    id: ncNanoId(),
    href: "/cart",
    name: "장바구니 페이지",
  },
  {
    id: ncNanoId(),
    href: "/checkout",
    name: "결제 페이지",
  },
  {
    id: ncNanoId(),
    href: "/search",
    name: "검색 페이지",
  },
  {
    id: ncNanoId(),
    href: "/account",
    name: "마이 페이지",
  },
  {
    id: ncNanoId(),
    href: "/about",
    name: "고객센터",
    type: "dropdown",
    children: [
      {
        id: ncNanoId(),
        href: "/contact",
        name: "문의하기",
      },
      {
        id: ncNanoId(),
        href: "/signup",
        name: "회원가입",
      },
    ],
  },
];

export const NAVIGATION_DEMO_2: NavItemType[] = [
  {
    id: ncNanoId(),
    href: "/collection",
    name: "상의",
  },
  {
    id: ncNanoId(),
    href: "/collection-2",
    name: "하의",
  },
  {
    id: ncNanoId(),
    href: "/collection",
    name: "신발",
  },

  {
    id: ncNanoId(),
    href: "/collection-2",
    name: "가방",
  },
  {
    id: ncNanoId(),
    href: "/collection",
    name: "템플릿",
    type: "megaMenu",
    children: MEGAMENU_TEMPLATES,
  },
  {
    id: ncNanoId(),
    href: "/search",
    name: "탐색",
    type: "dropdown",
    children: OTHER_PAGE_CHILD,
  },
  {
    id: ncNanoId(),
    href: "/",
    name: "경매 등록"
  }
];
