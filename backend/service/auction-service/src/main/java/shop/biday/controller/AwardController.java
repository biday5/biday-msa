package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.entity.AwardEntity;
import shop.biday.service.AwardService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/awards")
@Tag(name = "awards", description = "Award Controller")
public class AwardController {
    private final AwardService awardService;

    @GetMapping
    @Operation(summary = "낙찰 목록", description = "마이 페이지에서 불러올 수 있는 낙찰 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "낙찰 목록 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "낙찰 목록 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "period", description = "기간별 정렬", example = "3개월"),
            @Parameter(name = "cursor", description = "현재 페이지에서 가장 마지막 낙찰의 id", example = "1"),
            @Parameter(name = "page", description = "페이지 번호", example = "1"),
            @Parameter(name = "size", description = "한 페이지에서 보여질 경매의 개수", example = "20"),
    })
    public ResponseEntity<Slice<AwardModel>> findByUser(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam(value = "period", required = false, defaultValue = "3개월") String period,
            @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return awardService.findByUser(userInfoHeader, period, cursor, pageable);
    }

    @GetMapping("/findById")
    @Operation(summary = "낙찰 id 기준 낙찰 상세보기", description = "마이페이지에서 낙찰 리스트 통해 이동 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "낙찰 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "낙찰 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 ",
                    example = "UserInfo{'id': 'abc342', 'name': 'kim', role: 'ROLE_USER'}"),
            @Parameter(name = "awardId", description = "상세보기할 낙찰의 id", example = "1")
    })
    public ResponseEntity<AwardModel> findByAwardId(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam(value = "awardId", required = true) Long awardId) {
        return awardService.findByAwardId(userInfoHeader, awardId);
    }

    @GetMapping("/findByAuction")
    @Operation(summary = "경매 id 기준 낙찰 상세보기", description = "경매가 종료된 페이지 내에서 호출")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "낙찰 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "낙찰 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "auctionId", description = "낙찰 정보 확인할 경매 id", example = "1")
    })
    public ResponseEntity<AwardDto> findByAuctionId(
            @RequestParam(value = "auctionId", required = true) Long auctionId) {
        return awardService.findByAuctionId(auctionId);
    }

    @GetMapping("/findBySize")
    @Operation(summary = "사이즈 id 기준 낙찰 리스트 호출", description = "상품 상세 페이지 내에서 호출")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "낙찰 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "낙찰 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "sizeId", description = "낙찰 리스트 불러올 사이즈 id", example = "1")
    })
    public ResponseEntity<List<AwardModel>> findBySizeId(
            @RequestParam(value = "sizeId", required = true) Long sizeId) {
        return awardService.findBySizeId(sizeId);
    }

    @PatchMapping("/updateStatus")
    @Operation(summary = "결제했다면 status 업데이트", description = "결제 여부 확인하기 위함, true라면 결제 완료")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "낙찰 status 변경 성공"),
            @ApiResponse(responseCode = "404", description = "낙찰 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "awardId", description = "낙찰 id", example = "1")
    })
    public ResponseEntity<String> updateStatus(
            @RequestParam(value = "awardId", required = true) Long awardId) {
        return awardService.updateStatus(awardId);
    }
}