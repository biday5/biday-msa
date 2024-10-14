package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.service.WishService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
@Tag(name = "wishes", description = "Wish Controller")
public class WishController {
    private final WishService wishService;


    @GetMapping("/user")
    @Operation(summary = "사용자 기준 위시 목록", description = "마이페이지 등에서 보여질 때 불러질 특정 사용자의 wish 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시 목록 불러오기 성공"),
            @ApiResponse(responseCode = "204", description = "위시 목록이 없음"),
            @ApiResponse(responseCode = "404", description = "위시를 찾을 수 없음")
    })
    @Parameter(name = "UserInfo", description = "현재 로그인한 사용자",
            example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}")
    public ResponseEntity<List<?>> findByUser(@RequestHeader("UserInfo") String userInfoHeader) {
        List<?> wishList = wishService.findByUserId(userInfoHeader);

        return (wishList == null || wishList.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(wishList);

    }

    @GetMapping
    @Operation(summary = "위시 수정", description = "위시 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시 수정 성공"),
            @ApiResponse(responseCode = "201", description = "위시 생성 성공"),
            @ApiResponse(responseCode = "404", description = "위시 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "productId", description = "상품 id", example = "1")
    })
    public ResponseEntity<?> toggleWish(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("productId") Long productId) {
        return wishService.toggleWish(userInfoHeader, productId)
                ? ResponseEntity.status(HttpStatus.CREATED).body("위시 생성 성공")
                : ResponseEntity.ok("위시 삭제 성공");

    }

    @DeleteMapping
    @Operation(summary = "위시 삭제", description = "위시 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "위시 삭제 할 수 없음"),
            @ApiResponse(responseCode = "403", description = "삭제 권한 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "wishId", description = "위시 id", example = "1")
    })
    public ResponseEntity<?> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("wishId") Long wishId) {
        return wishService.deleteByWishId(userInfoHeader, wishId);

    }
}