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
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;
import shop.biday.service.ProductService;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "products", description = "Product Controller")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/findAll")
    @Operation(summary = "전체 상품 목록", description = "경매 등록 시 사용될 전체 상품 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 전체 목록 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 전체 목록 찾을 수 없음")
    })
    public ResponseEntity<Map<Long, ProductModel>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/findByFilter")
    @Operation(summary = "상품 목록", description = "메인에서 보여지거나, 검색 조건에 따른 상품 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 목록 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "brandId", description = "브랜드 id", example = "1L"),
            @Parameter(name = "categoryId", description = "카테고리 id", example = "1L"),
            @Parameter(name = "keyword", description = "상품 키워드", example = "바지"),
            @Parameter(name = "color", description = "검색 이후 선택 가능한 색깔 필터", example = "red"),
            @Parameter(name = "order", description = "상품 목록 정렬 방식", example = "최신 등록순"),
            @Parameter(name = "lastItemId", description = "현재 페이지에서 가장 마지막 상품의 id", example = "1L")
    })
    public ResponseEntity<List<ProductDto>> searchByFilter(
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "color", required = false, defaultValue = "") String color,
            @RequestParam(value = "order", required = false, defaultValue = "") String order,
            @RequestParam(value = "lastItemId", required = false) Long lastItemId) {
        return ResponseEntity.ok(productService.findByFilter(categoryId, brandId, keyword, color, order, lastItemId));
    }

    @GetMapping
    @Operation(summary = "상품 상세보기", description = "상품 리스트 혹은 마이페이지-찜 등에서 눌렀을 때 이동되는 상품 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "선택된 상품의 id", example = "1L")
    public ResponseEntity<List<Map.Entry<Long, ProductModel>>> findByName(@RequestParam(value = "id", required = true) Long id) {
        return ResponseEntity.ok(productService.findAllByProductName(id));
    }

    @GetMapping("/findOne")
    @Operation(summary = "상품 1개 상세보기", description = "상품 리스트 혹은 마이페이지-찜 등에서 눌렀을 때 이동되는 상품 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "선택된 상품의 id", example = "1L")
    public ResponseEntity<Map<Long, ProductModel>> findById(@RequestParam(value = "id", required = true) Long id) {
        return ResponseEntity.ok(productService.findByProductId(id));
    }

    @PostMapping
    @Operation(summary = "상품 등록", description = "새로운 상품 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "404", description = "상품 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleProductModel", value = """ 
                        { 
                            "brand" : "브랜드 이름",
                            "category" : "카테고리 이름", 
                            "name" : "상품명", 
                            "subName" : "상품명2",
                            "productCode" : "제품번호",
                            "price" : "가격",
                            "color" : "색깔",
                            "description" : "상품 설명"
                        } 
                    """)})
    })
    public ResponseEntity<ProductEntity> saveProduct(
            @RequestHeader String role,
            @RequestBody ProductModel product) {
        return ResponseEntity.ok(productService.save(role, product));
    }

    @PatchMapping
    @Operation(summary = "상품 수정", description = "기존 상품 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "404", description = "상품 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleProductModel", value = """ 
                        { 
                            "brand" : "브랜드 이름",
                            "category" : "카테고리 이름", 
                            "name" : "상품명", 
                            "subName" : "상품명2",
                            "productCode" : "제품번호",
                            "price" : "가격",
                            "color" : "색깔",
                            "description" : "상품 설명"
                        } 
                    """)})
    })
    public ResponseEntity<ProductEntity> updateProduct(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody ProductModel product) {
        return ResponseEntity.ok(productService.update(userInfoHeader, product));
    }

    @DeleteMapping
    @Operation(summary = "상품 삭제", description = "상품 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(name = "productId", description = "상품 id", example = "1")
    })
    public ResponseEntity<String> deleteProduct(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam(value = "productId", required = true) Long productId) {
        return ResponseEntity.ok(productService.deleteById(userInfoHeader, productId));
    }
}
