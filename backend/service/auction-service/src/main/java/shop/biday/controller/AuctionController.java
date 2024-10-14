package shop.biday.controller;

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
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.service.AuctionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
@Tag(name = "auctions", description = "Auction Controller")
public class AuctionController {
    private final AuctionService auctionService;

    @GetMapping("/findById")
    @Operation(summary = "경매 상세보기", description = "경매 상세보기, 여기서는 경매와 해당 상품에 관한 정보만 가져옴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 불러오기 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "상세보기할 경매의 id", example = "1")
    public ResponseEntity<AuctionModel> findById(@RequestParam(value = "id", required = true) Long id) {
        return auctionService.findById(id);
    }

    @GetMapping("/findBySize")
    @Operation(summary = "헤더 경매 목록", description = "종료 날짜에 따른 경매 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 목록 가져오기 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "경매 목록 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "sizeId", description = "경매에 등록된 상품의 사이즈 id", example = "1"),
            @Parameter(name = "order", description = "정렬할 시간 기준", example = "종료 임박 순"),
            @Parameter(name = "cursor", description = "현재 페이지에서 가장 마지막 경매의 id", example = "1"),
            @Parameter(name = "page", description = "페이지 번호", example = "1"),
            @Parameter(name = "size", description = "한 페이지에서 보여질 경매의 개수", example = "20"),
    })
    public ResponseEntity<Slice<AuctionDto>> findBySize(
            @RequestParam(value = "sizeId", required = false) Long sizeId,
            @RequestParam(value = "order", required = false, defaultValue = "") String order,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auctionService.findBySize(sizeId, order, cursor, pageable);
    }

    @GetMapping("/findAllBySize")
    @Operation(summary = "상품 상세 경매 목록", description = "상품 상세에서 size 기준으로 보여질 전체 경매 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 목록 가져오기 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "경매 목록 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "sizeId", description = "경매에 등록된 상품의 사이즈 id", example = "1"),
            @Parameter(name = "order", description = "정렬할 시간 기준", example = "종료 임박 순"),
    })
    public ResponseEntity<List<AuctionDto>> findAllBySize(
            @RequestParam(value = "sizeId", required = true) Long sizeId,
            @RequestParam(value = "order", required = false, defaultValue = "") String order) {
        return auctionService.findAllBySize(sizeId, order);
    }

    @GetMapping
    @Operation(summary = "마이페이지 경매 목록", description = "마이 페이지에서 불러올 수 있는 경매 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 목록 가져오기 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "경매 목록 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "period", description = "기간별 정렬", example = "3개월"),
            @Parameter(name = "cursor", description = "현재 페이지에서 가장 마지막 경매의 id", example = "1"),
            @Parameter(name = "page", description = "페이지 번호", example = "1"),
            @Parameter(name = "size", description = "한 페이지에서 보여질 경매의 개수", example = "20"),
    })
    public ResponseEntity<Slice<AuctionDto>> findByUser(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam(value = "period", required = false, defaultValue = "3개월") String period,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auctionService.findByUser(userInfoHeader, period, cursor, pageable);
    }

    @PostMapping
    @Operation(summary = "경매 등록", description = "새로운 경매 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 등록 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleAuctionModel", value = """ 
                        { 
                            "userId" : "변경 불가, 판매자 userId",
                            "sizeId" : "변경 불가, 경매로 등록할 상품의 sizeId, size의 findById 사용해서 선택!", 
                            "description" : "변경 불가, 경매로 등록할 판매자 상품의 설명", 
                            "startingBid" : "변경 불가, 경매 시작가, 상품 가격의 반값or40%로 시작",
                            "currentBid" : "변경 불가, 현재 경매가",
                            "startedAt" : "만약 이미 시작되었다면 변경 불가, 시작 날짜",
                            "endedAt" : "만약 이미 시작되었다면 변경 불가, 종료 날짜"
                        } 
                    """)})
    })
    public ResponseEntity<AuctionEntity> save(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody AuctionDto auctionModel) {
        return auctionService.save(userInfoHeader, auctionModel);
    }

    @PatchMapping
    @Operation(summary = "경매 수정", description = "진행 예정 경매 수정, 기존 시작 날짜 전에만 시작 날짜+끝나는 날짜만 변경 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "경매 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleProductModel", value = """ 
                        { 
                            "id" : "경매 id"
                            "userId" : "변경 불가, 판매자 userId",
                            "sizeId" : "변경 불가, 경매로 등록할 상품의 sizeId, size의 findById 사용해서 선택!", 
                            "description" : "변경 불가, 경매로 등록할 판매자 상품의 사진", 
                            "startingBid" : "변경 불가, 경매 시작가, 상품 가격의 반값 or 40%로 시작",
                            "currentBid" : "변경 불가, 현재 경매가",
                            "startedAt" : "만약 이미 시작되었다면 변경 불가, 시작 날짜",
                            "endedAt" : "만약 이미 시작되었다면 변경 불가, 종료 날짜"
                        } 
                    """)})
    })
    public ResponseEntity<AuctionEntity> update(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestBody AuctionDto auctionModel) {
        return auctionService.update(userInfoHeader, auctionModel);
    }

    @DeleteMapping
    @Operation(summary = "경매 삭제", description = "경매 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "경매 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "userId", description = "현재 로그인한 사용자 token에서 추출한 userId", example = "66f1442a7415bc47b04b3477"),
            @Parameter(name = "brandId", description = "브랜드 id", example = "1")
    })
    public ResponseEntity<String> delete(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam Long id) {
        return auctionService.deleteById(userInfoHeader, id);
    }
}
