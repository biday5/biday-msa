package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import shop.biday.model.document.LoginHistoryDocument;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.service.impl.LoginHistoryServiceImpl;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loginHistory")
@Tag(name = "loginHistory", description = "LoginHistory Controller")
public class LoginHistoryController {

    private final LoginHistoryServiceImpl loginHistoryService;

    @GetMapping("/{userId}")
    @Operation(summary = "유저 로그인이력조회", description = "유저 로그인이력조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4011", description = "조회에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "userId", description = "유저번호", example = "66f3601fd3d86243cceb4718")
    })
    public ResponseEntity<Mono<Boolean>> findById(@PathVariable String userId) {
        return new ResponseEntity<>(loginHistoryService.findByUserId(userId), HttpStatus.OK);
    }


    @PostMapping("")
    @Operation(summary = "유저 로그인이력 저장", description = "유저 로그인이력 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4011", description = "조회에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "userId", description = "유저번호", example = "66f3601fd3d86243cceb4718")
    })
    public ResponseEntity<Mono<LoginHistoryDocument>> save(@RequestBody LoginHistoryModel loginHistoryModel) {
        return new ResponseEntity<>(loginHistoryService.save(loginHistoryModel), HttpStatus.OK);
    }
}
