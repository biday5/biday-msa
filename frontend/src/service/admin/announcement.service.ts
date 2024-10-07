import {announcementAPI} from "@/api/admin/announcement.api";

export function fetchAnnouncement () {
    announcementAPI.findAll();
    // 보내줘야할 데이터 여기에서 쓰라는거다.
    //
}

