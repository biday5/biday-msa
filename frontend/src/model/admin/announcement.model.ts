// src/model/announcement.model.ts
export interface AnnouncementModel {
    id?: number;
    userId: number;
    title: string;
    content: string;
    createdAt: Date;
    updatedAt: Date;
}

// 초기값을 정의할 수 있는 객체도 추가해보세요:
export const initialAnnouncement: AnnouncementModel = {
    id: 0,
    userId: 0,
    title: '',
    content: '',
    createdAt: new Date(),
    updatedAt: new Date(),
};
