package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.repository.WishRepository;
import shop.biday.service.WishService;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
@Tag(name = "wishes", description = "Wish Controller")
public class WishController {
    private final WishService wishService;
    private final WishRepository wishRepository;


    @GetMapping("/user")
    @Operation(summary = "사용자 기준 위시 목록", description = "마이페이지 등에서 보여질 때 불러질 특정 사용자의 wish 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "위시 찾을 수 없음")
    })
    @Parameter(name = "userId", description = "유저 id", example = "sdfksdfsdfoijekf")
    public ResponseEntity<List<?>> findByUser(@RequestHeader("UserInfo") String userInfoHeader) {
        List<?> wishList = wishRepository.findByUserId(userInfoHeader);

        return (wishList == null || wishList.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(wishList);

    }

    @GetMapping
    @Operation(summary = "사이즈 수정", description = "사이즈 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사이즈 수정 성공"),
            @ApiResponse(responseCode = "404", description = "사이즈 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleSizeModel", value = """ 
                        { 
                            "id" : "사이즈 id",
                            "product" : "브랜드 이름",
                            "name" : "사이즈 이름(enum : XS~XXL)", 
                            "createdAt" : "등록 시간"
                        } 
                    """)})
    })
    public ResponseEntity<?> toggleWish(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("productId") Long productId) {
        return wishService.toggleWish(userId, productId)
                ? ResponseEntity.status(HttpStatus.CREATED).body("위시 생성 성공")
                : ResponseEntity.ok("위시 삭제 성공");

    }

    @DeleteMapping
    @Operation(summary = "사이즈 수정", description = "사이즈 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사이즈 수정 성공"),
            @ApiResponse(responseCode = "404", description = "사이즈 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleSizeModel", value = """ 
                        { 
                            "id" : "사이즈 id",
                            "product" : "브랜드 이름",
                            "name" : "사이즈 이름(enum : XS~XXL)", 
                            "createdAt" : "등록 시간"
                        } 
                    """)})
    })
    public ResponseEntity<?> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("wishId") Long id) {
        return wishRepository.findById(id)
                .filter(wish -> wish.getUserId().equals(userId))
                .map(wish -> {
                    wishRepository.deleteById(id);
                    return ResponseEntity.ok("위시 삭제 성공");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 wishId: " + id));

    }
}