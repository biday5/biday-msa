package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.FaqModel;
import shop.biday.service.FaqService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faqs")
@Tag(name = "faqs", description = "FAQs Controller")
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    @Operation(summary = "모든 질문 조회", description = "DB에 저장된 모든 질문 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 질문 목록 조회 실패")
    })
    public ResponseEntity<List<FaqModel>> findAll() {
        return ResponseEntity.ok(faqService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 질문 조회", description = "ID로 특정 질문을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 조회 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 질문 조회 실패")
    })
    @Parameter(name = "id", description = "조회할 질문의 ID", example = "1")
    public ResponseEntity<FaqModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.findById(id));
    }

    @PostMapping
    @Operation(summary = "새로운 질문 추가", description = "새로운 질문을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "질문 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 질문 추가 실패")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleFaqModel", value = """ 
                        { 
                            "userId" : "작성자 id", 
                            "title" : "질문 제목", 
                            "content" : "질문 내용"
                        } 
                    """)})
    })
    public ResponseEntity<FaqModel> addQuestion(@RequestHeader("UserInfo") String userInfo,
                                                @RequestBody FaqModel questionModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.save(userInfo, questionModel));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "질문 삭제", description = "ID로 특정 질문을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "질문 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "삭제할 질문을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 질문 삭제 실패")
    })
    @Parameters({
            @Parameter(name = "id", description = "삭제할 질문의 ID", example = "1"),
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}")
    })
    public ResponseEntity<Boolean> deleteById(
            @PathVariable Long id,
            @RequestHeader("UserInfo") String userInfo) {
        return ResponseEntity.ok(faqService.deleteById(id, userInfo));
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "질문 존재 여부 확인", description = "ID로 특정 질문이 존재하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 존재 여부 확인 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "질문이 존재하지 않음", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 질문 존재 여부 확인 실패", content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "존재 여부를 확인할 질문의 ID", example = "1")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.existsById(id));
    }
}
