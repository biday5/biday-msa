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
import shop.biday.model.document.AccountDocument;
import shop.biday.model.domain.AccountModel;
import shop.biday.service.impl.AccountServiceImpl;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Tag(name = "account", description = "Account Controller")
public class AccountController {

    private final AccountServiceImpl accountService;

    @GetMapping()
    @Operation(summary = "판매자 계좌조회", description = "판매자 계좌조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "조회에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4011", description = "조회에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
    })
    public ResponseEntity<Mono<AccountDocument>> findById(@RequestHeader("UserInfo") String userInfoHeader) {
        return new ResponseEntity<>(accountService.findByUserId(userInfoHeader), HttpStatus.OK);
    }

    @PostMapping("/save")
    @Operation(summary = "판매자 계좌등록", description = "판매자 계좌인증 사용하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "2002", description = "이미 가입된 계정입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4011", description = "비밀번호 암호화에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "userId", description = "유저번호", example = "66f3601fd3d86243cceb4718"),
            @Parameter(name = "bankCode", description = "은행코드", example = "092"),
            @Parameter(name = "bankName", description = "은행이름", example = "카카오뱅크"),
            @Parameter(name = "accountName", description = "계좌주", example = "김**"),
            @Parameter(name = "accountNum", description = "계좌번호", example = "123456789"),
            @Parameter(name = "bankRspCoe", description = "은행응답코드", example = "000"),
            @Parameter(name = "bankTranDate", description = "인증날짜", example = "20160310"),
            @Parameter(name = "bankRspMessage", description = "응답메시지", example = " ")
    })
    public ResponseEntity <Mono<AccountDocument>>save(@RequestBody AccountModel accountModel) {
        return new ResponseEntity<>(accountService.save(accountModel), HttpStatus.CREATED);
    }

}
