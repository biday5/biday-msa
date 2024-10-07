package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
@Tag(name = "brands", description = "Brand Controller")
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "브랜드 목록", description = "상품 등록하거나, 메인/검색 페이지에서 브랜드 목록 띄울 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음")
    })
    public ResponseEntity<List<BrandModel>> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping("/findById")
    @Operation(summary = "브랜드 상세보기", description = "브랜드 상세보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음")
    })
    @Parameter(name = "id", description = "상세보기할 브랜드 id", example = "1")
    public ResponseEntity<BrandModel> findById(@RequestParam Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @PostMapping
    @Operation(summary = "브랜드 등록", description = "브랜드 새로 등록하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 등록 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 등록 할 수 없음")
    })

    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleBrandModel", value = """ 
                        { 
                            "name" : "브랜드 이름", 
                            "createdAt" : "등록 시간", 
                            "updatedAt" : "시간 시간"
                        } 
                    """)})
    })
    public ResponseEntity<BrandEntity> create(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody BrandModel brand) {
        return ResponseEntity.ok(brandService.save(userInfoHeader, brand));
    }

    @PatchMapping
    @Operation(summary = "브랜드 수정", description = "브랜드 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 수정 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleBrandModel", value = """ 
                        { 
                            "id" : "변경할 브랜드 id",
                            "name" : "브랜드 이름", 
                            "createdAt" : "등록 시간", 
                            "updatedAt" : "시간 시간"
                        } 
                    """)})
    })
    public ResponseEntity<BrandEntity> update(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody BrandModel brand) {
        return ResponseEntity.ok(brandService.update(userInfoHeader, brand));
    }

    @DeleteMapping
    @Operation(summary = "브랜드 삭제", description = "브랜드 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 삭제 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(name = "brandId", description = "브랜드 id", example = "1")
    })
    public ResponseEntity<String> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("brandId") Long id) {
        return ResponseEntity.ok(brandService.deleteById(userInfoHeader, id));
    }
}
