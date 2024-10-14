package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.entity.ShipperEntity;
import shop.biday.service.ShipperService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shippers")
@Tag(name = "shippers", description = "Shipper Controller")
public class ShipperController {

    private final ShipperService shipperService;

    @GetMapping
    @Operation(summary = "배송 목록", description = "메인/검색 페이지에서 배송 목록 띄울 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 목록 불러오기 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "배송 목록을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<ShipperModel>> findAll() {
        log.info("Shipper Controller findAll");
        return shipperService.findAll();
    }

    @GetMapping("/findById")
    @Operation(summary = "배송 상세보기", description = "배송 상세보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 불러오기 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "배송을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "상세보기할 배송 id", example = "1")
    public ResponseEntity<ShipperModel> findById(@RequestParam Long id) {
        return shipperService.findById(id);
    }

    @PostMapping
    @Operation(summary = "배송 등록", description = "배송 새로 등록하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 등록 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "배송 등록 할 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleShipperModel", value = """ 
                        { 
                            "paymentId" : "결제 id",
                            "carrier" : "운송업체 이름", 
                            "trackingNumber" : "운송장번호", 
                            "shipmentDate" : "배송 시작 날짜",
                            "estimatedDeliveryDate" : "예상 배송 완료 날짜",
                            "deliveryDate" : "실제 배송 완료 날짜",
                            "status" : "상태",
                            "deliveryAddress" : "배송 주소"
                        } 
                    """)})
    })
    public ResponseEntity<ShipperEntity> create(@RequestHeader("UserInfo") String userInfo,
                                                @RequestBody ShipperModel shipper) {
        log.info("Shipper Controller create userInfo: {}, shipperModel: {}", userInfo, shipper);
        return shipperService.save(userInfo, shipper);
    }

    @PatchMapping
    @Operation(summary = "배송 수정", description = "배송 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 수정 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "배송 수정 할 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleShipperModel", value = """ 
                        { 
                            "id" : "배송 id",
                            "paymentId" : "결제 id",
                            "carrier" : "운송업체 이름", 
                            "trackingNumber" : "운송장번호", 
                            "shipmentDate" : "배송 시작 날짜",
                            "estimatedDeliveryDate" : "예상 배송 완료 날짜",
                            "deliveryDate" : "실제 배송 완료 날짜",
                            "status" : "상태",
                            "deliveryAddress" : "배송 주소"
                        } 
                    """)})
    })
    public ResponseEntity<ShipperEntity> update(@RequestHeader("UserInfo") String userInfo,
                                                @RequestBody ShipperModel shipper) {
        return shipperService.update(userInfo, shipper);
    }

    @DeleteMapping
    @Operation(summary = "배송 삭제", description = "배송 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "배송 삭제 할 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "id", description = "삭제할 배송 id", example = "1")
    })
    public ResponseEntity<String> delete(@RequestHeader("UserInfo") String userInfo, @RequestParam Long id) {
        return shipperService.deleteById(userInfo, id);
    }
}
