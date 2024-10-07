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
import shop.biday.model.domain.CategoryModel;
import shop.biday.model.entity.CategoryEntity;
import shop.biday.service.CategoryService;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "categories", description = "Category Controller")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "카테고리 목록", description = "상품 등록하거나, 메인/검색 페이지에서 카테고리 목록 띄울 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음")
    })
    public ResponseEntity<List<CategoryModel>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/findById")
    @Operation(summary = "카테고리 상세보기", description = "카테고리 상세보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음")
    })
    @Parameter(name = "id", description = "상세보기할 카테고리 id", example = "1L")
    public ResponseEntity<CategoryModel> findById(@RequestParam Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    @Operation(summary = "카테고리 등록", description = "카테고리 새로 등록하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 등록 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleCategoryModel", value = """ 
                        { 
                            "name" : "카테고리 이름", 
                            "createdAt" : "등록 시간", 
                            "updatedAt" : "시간 시간"
                        } 
                    """)})
    })
    public ResponseEntity<CategoryEntity> create(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody CategoryModel category) {
        return ResponseEntity.ok(categoryService.save(userInfoHeader, category));
    }

    @PatchMapping
    @Operation(summary = "카테고리 수정", description = "카테고리 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleCategoryModel", value = """ 
                        { 
                            "id" : "변경할 카테고리 id"
                            "name" : "카테고리 이름", 
                            "createdAt" : "등록 시간", 
                            "updatedAt" : "시간 시간"
                        } 
                    """)})
    })
    public ResponseEntity<CategoryEntity> update(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody CategoryModel category) {
        return ResponseEntity.ok(categoryService.update(userInfoHeader, category));
    }

    @DeleteMapping
    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 삭제 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(name = "brandId", description = "브랜드 id", example = "1")
    })
    public ResponseEntity<String> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam Long id) {
        return ResponseEntity.ok(categoryService.deleteById(userInfoHeader, id));
    }
}
