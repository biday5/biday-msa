package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
    @Operation(summary = "배송 목록", description = "상품 등록하거나, 메인/검색 페이지에서 배송 목록 띄울 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "배송 찾을 수 없음")
    })
    public ResponseEntity<List<ShipperModel>> findAll() {
        log.info("Shipper Controller findAll");
        return ResponseEntity.ok(shipperService.findAll());
    }

    @GetMapping("/findById")
    @Operation(summary = "배송 상세보기", description = "배송 상세보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "배송 찾을 수 없음")
    })
    @Parameter(name = "id", description = "상세보기할 배송 id", example = "1")
    public ResponseEntity<ShipperModel> findById(@RequestParam Long id) {
        return ResponseEntity.ok(shipperService.findById(id));
    }

    @PostMapping
    @Operation(summary = "배송 등록", description = "배송 새로 등록하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 등록 성공"),
            @ApiResponse(responseCode = "404", description = "배송 등록 할 수 없음")
    })
    @Parameter(name = "name", description = "배송 이름", example = "나이키(Nike)")
    public ResponseEntity<ShipperEntity> create(@RequestHeader("UserInfo") String userInfo,
                                                @RequestBody ShipperModel shipper) {
        log.info("Shipper Controller create userInfo: {}, shipperModel: {}", userInfo, shipper);
        return ResponseEntity.ok(shipperService.save(userInfo, shipper));
    }

    @PatchMapping
    @Operation(summary = "배송 수정", description = "배송 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 수정 성공"),
            @ApiResponse(responseCode = "404", description = "배송 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "name", description = "배송 이름", example = "나이키(Nike)"),
            @Parameter(name = "createdAt", description = "등록 시간", example = "localDateTime 값"),
            @Parameter(name = "updatedAt", description = "수정 시간", example = "localDateTime 값")
    })
    public ResponseEntity<ShipperEntity> update(@RequestHeader("UserInfo") String userInfo,
                                                @RequestBody ShipperModel shipper) {
        return ResponseEntity.ok(shipperService.update(userInfo, shipper));
    }

    @DeleteMapping
    @Operation(summary = "배송 삭제", description = "배송 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "배송 삭제 할 수 없음")
    })
    @Parameter(name = "id", description = "배송 id", example = "1")
    public ResponseEntity<String> delete(@RequestHeader("UserInfo") String userInfo, @RequestParam Long id) {
        return ResponseEntity.ok(shipperService.deleteById(userInfo, id));
    }
}
