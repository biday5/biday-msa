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
        return productService.findAll();
    }

    @GetMapping("/findByFilter")
    @Operation(summary = "상품 목록", description = "메인에서 보여지거나, 검색 조건에 따른 상품 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 목록 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "brand", description = "브랜드 이름", example = "esfai"),
            @Parameter(name = "category", description = "카테고리 이름", example = "top"),
            @Parameter(name = "keyword", description = "상품 키워드", example = "바지"),
            @Parameter(name = "color", description = "검색 이후 선택 가능한 색깔 필터", example = "red"),
            @Parameter(name = "order", description = "상품 목록 정렬 방식", example = "최신 등록순"),
    })
    public ResponseEntity<List<ProductDto>> searchByFilter(
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "color", required = false, defaultValue = "") String color,
            @RequestParam(value = "order", required = false, defaultValue = "") String order
            ) {
        return productService.findByFilter(category, brand, keyword, color, order);
    }

    @GetMapping
    @Operation(summary = "상품 상세보기", description = "상품 리스트 혹은 마이페이지-찜 등에서 눌렀을 때 이동되는 상품 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "선택된 상품의 id", example = "1L")
    public ResponseEntity<List<Map.Entry<Long, ProductModel>>> findByName(@RequestParam(value = "id", required = true) Long id) {
        return productService.findAllByProductName(id);
    }

    @GetMapping("/findOne")
    @Operation(summary = "상품 1개 상세보기", description = "상품 리스트 혹은 마이페이지-찜 등에서 눌렀을 때 이동되는 상품 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "선택된 상품의 id", example = "1L")
    public ResponseEntity<Map<Long, ProductModel>> findById(@RequestParam(value = "id", required = true) Long id) {
        return productService.findByProductId(id);
    }

    @PostMapping
    @Operation(summary = "상품 등록", description = "새로운 상품 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족"),
            @ApiResponse(responseCode = "400", description = "상품 등록 실패")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
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
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody ProductModel product) {
        return productService.save(userInfoHeader, product);
    }

    @PatchMapping
    @Operation(summary = "상품 수정", description = "기존 상품 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "404", description = "상품 수정 할 수 없음"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleProductModel", value = """ 
                        { 
                            "id" : "상품 id",
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
        return productService.update(userInfoHeader, product);
    }

    @DeleteMapping
    @Operation(summary = "상품 삭제", description = "상품 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 부족")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "productId", description = "상품 id", example = "1")
    })
    public ResponseEntity<String> deleteProduct(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam(value = "productId", required = true) Long productId) {
        return productService.deleteById(userInfoHeader, productId);
    }
}
