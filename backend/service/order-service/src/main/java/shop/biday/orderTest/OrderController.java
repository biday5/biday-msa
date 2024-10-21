// package shop.biday.controller;
package shop.biday.orderTest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "orders", description = "Order Controller")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "주문 목록", description = "마이 페이지에서 불러올 수 있는 주문 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 목록 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "주문 목록 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "period", description = "기간별 정렬", example = "3개월"),
            @Parameter(name = "cursor", description = "현재 페이지에서 가장 마지막 주문의 id", example = "1"),
            @Parameter(name = "page", description = "페이지 번호", example = "1"),
            @Parameter(name = "size", description = "한 페이지에서 보여질 주문의 개수", example = "20"),
    })
    public ResponseEntity<Slice<OrderModel>> findByUser(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam(value = "period", required = false, defaultValue = "3개월") String period,
            @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderService.findByUser(userInfoHeader, period, cursor, pageable);
    }

    @GetMapping("/findById")
    @Operation(summary = "주문 상세보기", description = "마이페이지에서 주문 리스트 통해 이동 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "주문 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "orderId", description = "상세보기할 주문의 id", example = "1")
    })
    public ResponseEntity<OrderModel> findById(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam(value = "orderId", required = true) Long orderId) {
        return orderService.findByOrderId(userInfoHeader, orderId);
    }

    @PostMapping
    @Operation(summary = "주문 등록", description = "결제 이후 실행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 추가 성공"),
            @ApiResponse(responseCode = "404", description = "주문 추가할 수 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleOrderModel", value = """ 
                                { 
                                    "orderId" : "주문 번호",
                                    "userId" : "결제한 유저의 id", 
                                    "address" : "배송받을 주소(기본 주소 아니어도 상관 x)", 
                                    "auctionId" : "경매 id",
                                    "awardId" : "낙찰 id",
                                    "productId" : "상품 id",
                                    "productName" : "상품 이름",
                                    "size" : "실제 사이즈(M, L etc)",
                                    "paymentId" : "결제 id",
                                    "shipperId" : "배송정보 id")
                                } 
                            """)})})
    public ResponseEntity<OrderEntity> findById(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody OrderDto order) {
        return orderService.save(userInfoHeader, order);
    }
}