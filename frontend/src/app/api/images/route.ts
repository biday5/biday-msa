import {PrismaClient} from "@prisma/client";
import {ImageModel, ImageType} from "@/model/ImageModel";
import {NextResponse} from "next/server";

const prisma = new PrismaClient();

export async function GET(request: Request) {
    try {

        const {searchParams} = new URL(request.url);
        const id = searchParams.get("id");
        const type = searchParams.get("type") as ImageType;

        if (type === "상품" && !id) {
            const productImages = await prisma.image.findMany({
                    where: {
                        type: type
                    },
                    orderBy: {
                        id: "desc",

                    },
                }
            );

            return NextResponse.json(productImages);
        }

        if (!id || !type) {
            return NextResponse.json({error: "잘못된 요청 : id 또는 type 누락 "}, {status: 400});
        }

        const images = await prisma.image.findMany({
            where: {
                AND: [
                    {type: type},
                    {referencedId: id}
                ]
            },
            take: 3,
            orderBy: {
                id: "desc",
            }
        });


        return NextResponse.json(images);

    } catch (error) {

        return NextResponse.json({error: "이미지 로드 중 오류 발생"}, {status: 500})

    }
}