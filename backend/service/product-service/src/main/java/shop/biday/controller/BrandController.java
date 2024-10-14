package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;
import shop.biday.service.BrandService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
@Tag(name = "brands", description = "Brand Controller")
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "브랜드 목록", description = "상품 등록 및 메인/검색 페이지에서 브랜드 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "브랜드 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<BrandModel>> findAll() {
        return brandService.findAll();
    }

    @GetMapping("/findById")
    @Operation(summary = "브랜드 상세보기", description = "브랜드 상세 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "브랜드 조회 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "상세보기할 브랜드 ID", example = "1")
    public ResponseEntity<BrandModel> findById(@RequestParam Long id) {
        return brandService.findById(id);
    }

    @PostMapping
    @Operation(summary = "브랜드 등록", description = "새 브랜드 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "브랜드 등록 성공"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 정보",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', 'role': 'ROLE_USER'}"),
    })
    public ResponseEntity<BrandEntity> create(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody BrandModel brand) {
        return brandService.save(userInfoHeader, brand);
    }

    @PatchMapping
    @Operation(summary = "브랜드 수정", description = "브랜드 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "브랜드 수정 성공"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 정보",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', 'role': 'ROLE_USER'}"),
    })
    public ResponseEntity<BrandEntity> update(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody BrandModel brand) {
        return brandService.update(userInfoHeader, brand);
    }

    @DeleteMapping
    @Operation(summary = "브랜드 삭제", description = "브랜드 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "브랜드 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 정보",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', 'role': 'ROLE_USER'}"),
            @Parameter(name = "brandId", description = "삭제할 브랜드 ID", example = "1")
    })
    public ResponseEntity<String> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("brandId") Long id) {
        return brandService.deleteById(userInfoHeader, id);
    }
}
