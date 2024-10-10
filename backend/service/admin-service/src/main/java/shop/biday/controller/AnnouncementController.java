package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.AnnouncementModel;
import shop.biday.service.AnnouncementService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcements")
@Tag(name = "announcements", description = "Announcement Controller")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    @Operation(summary = "모든 공지사항 조회", description = "DB에 저장된 모든 공지사항 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 공지사항 목록 조회 실패")
    })
    public ResponseEntity<List<AnnouncementModel>> findAll() {
        return ResponseEntity.ok(announcementService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 공지사항 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 조회 성공"),
            @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 공지사항 조회 실패")
    })
    @Parameter(name = "id", description = "조회할 공지사항의 ID", example = "1")
    public ResponseEntity<AnnouncementModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.findById(id));
    }

    @PostMapping
    @Operation(summary = "새로운 공지사항 추가", description = "새로운 공지사항을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공지사항 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 공지사항 추가 실패")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleAnnouncemnetModel", value = """ 
                        { 
                            "userId" : "사용자 id",
                            "title" : "공지사항 제목", 
                            "content" : "공지사항 내용"
                        } 
                    """)})
    })
    public ResponseEntity<AnnouncementModel> addAnnouncement(@RequestHeader("UserInfo") String userInfo,
                                                             @RequestBody AnnouncementModel announcementModel) {
        return ResponseEntity.ok(announcementService.save(userInfo, announcementModel));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "공지사항 삭제", description = "ID로 특정 공지사항을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "공지사항 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "삭제할 공지사항을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 공지사항 삭제 실패")
    })
    @Parameters({
            @Parameter(name = "id", description = "삭제할 공지사항의 ID", example = "1"),
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}")
    })
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id, @RequestHeader("UserInfo") String userInfo) {
        return ResponseEntity.ok(announcementService.deleteById(id, userInfo));
    }
}
