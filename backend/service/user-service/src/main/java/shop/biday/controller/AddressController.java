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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AddressDocument;
import shop.biday.model.domain.AddressModel;
import shop.biday.model.domain.AddressRequest;
import shop.biday.service.AddressService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
@Tag(name = "addresses", description = "Address Controller")
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/list")
    @Operation(summary = "주소 목록", description = "상품 등록이나 메인/검색 페이지에서 카테고리 목록을 불러올 때 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 목록을 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.")
    })
    @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
            example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}")
    public ResponseEntity<Flux<AddressDocument>> findAllByUserId(@RequestHeader("UserInfo") String userInfoHeader) {
        return ResponseEntity.ok(addressService.findAllByUserId(userInfoHeader));
    }

    @PutMapping("/pick")
    @Operation(
            summary = "주소 선택",
            description = "주어진 ID의 주소를 '선택됨' 상태로 업데이트하고, 동일한 사용자에 대해 다른 주소들은 '선택되지 않음'으로 업데이트합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소가 성공적으로 업데이트되었습니다."),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.", content = @Content)
    })
    @Parameter(name = "id", description = "기본주소로 선택할 id", example = "1L")
    public ResponseEntity<Mono<String>> pick(@RequestParam @Parameter(description = "선택할 주소의 ID") String id) {
        return ResponseEntity.ok(addressService.pick(id));
    }

    @GetMapping("/count")
    @Operation(
            summary = "주소 수 카운트",
            description = "주어진 이메일과 연관된 주소의 수를 카운트합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일과 연관된 주소의 수."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content)
    })
    @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
            example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}")
    public ResponseEntity<Mono<Long>> count(@RequestHeader("UserInfo") String userInfoHeader) {
        return ResponseEntity.ok(addressService.countByUserId(userInfoHeader));
    }

    // TODO UserInfo 넣기!
    @DeleteMapping("/deleteById")
    @Operation(
            summary = "주소 삭제",
            description = "주어진 ID의 주소를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "주소가 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.", content = @Content)
    })
    @Parameters({
        @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
        @Parameter(name = "id", description = "삭제 할 주소를 선택 id", example = "1L")
    })
    public ResponseEntity<Mono<Boolean>> deleteById(@RequestHeader("UserInfo") String userInfoHeader,
                                                    @RequestParam @Parameter(description = "삭제할 주소의 ID") String id) {
        try {
            addressService.deleteById(userInfoHeader,id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/insert")
    @Operation(
            summary = "주소 등록",
            description = "사용자를 위한 새 주소를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소가 성공적으로 등록되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleAddressModel", value = """ 
                        { 
                            "userId" : "사용자 id",
                            "streetAddress" : "도로명 주소", 
                            "detailAddress" : "상세 주소",
                            "zipcode" : "우편번호",
                            "type" : "",
                            "pick" : "기본 주소 선택 여부"
                        } 
                    """)})
    })
    public ResponseEntity<Mono<AddressDocument>> insert(
            @RequestHeader("UserInfo") String userInfo,
            @RequestBody @Validated @Parameter(description = "주소 세부 사항을 포함하는 모델") AddressRequest addressRequest) {
        log.info("addressRequest : {}", addressRequest);
        return ResponseEntity.ok(addressService.save(userInfo, addressRequest));
    }
}
