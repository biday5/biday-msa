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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "categories", description = "Category Controller")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "카테고리 목록", description = "상품 등록이나 메인/검색 페이지에서 카테고리 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<CategoryModel>> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/findById")
    @Operation(summary = "카테고리 상세보기", description = "카테고리 상세 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "상세보기할 카테고리 ID", example = "1L")
    public ResponseEntity<CategoryModel> findById(@RequestParam Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @Operation(summary = "카테고리 등록", description = "새 카테고리 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 등록 성공"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleCategoryModel", value = """ 
                        { 
                            "name" : "카테고리 이름"
                        } 
                    """)})
    })
    public ResponseEntity<CategoryEntity> create(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody CategoryModel category) {
        return categoryService.save(userInfoHeader, category);
    }

    @PatchMapping
    @Operation(summary = "카테고리 수정", description = "카테고리 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족"),
            @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleCategoryModel", value = """ 
                        { 
                            "id" : "변경할 카테고리 ID",
                            "name" : "카테고리 이름",
                            "updatedAt" : "수정한 시간"
                        } 
                    """)})
    })
    public ResponseEntity<CategoryEntity> update(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody CategoryModel category) {
        return categoryService.update(userInfoHeader, category);
    }

    @DeleteMapping
    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "categoryId", description = "삭제할 카테고리 ID", example = "1")
    })
    public ResponseEntity<String> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("categoryId") Long id) {
        return categoryService.deleteById(userInfoHeader, id);
    }
}
