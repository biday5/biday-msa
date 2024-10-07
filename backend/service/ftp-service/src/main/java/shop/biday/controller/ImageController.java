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
import org.springframework.web.multipart.MultipartFile;
import shop.biday.service.ImageService;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "images", description = "Image Controller")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/findImage")
    @Operation(summary = "이미지 불러오기", description = "이미지 불러오기.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "사진 불러오기 실패")
    })
    @Parameter(name = "imageId", description = "이미지의 id", example = "1L")
    public ResponseEntity<byte[]> getImageById(@RequestParam("imageId") String id) {
        log.info("이미지 불러오는 중");
        return imageService.getImage(id);
    }

    @PostMapping("/uploadByAdmin")
    @Operation(summary = "단일 이미지 업로드", description = "관리자가 브랜드/상품/평점/에러 관련 단일 이미지를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 등록 성공"),
            @ApiResponse(responseCode = "404", description = "사진 등록 실패")
    })
    @Parameters(value = {
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(name = "files", description = "업로드할 이미지 파일", example = "단일 사진 파일"),
            @Parameter(name = "filePath", description = "NCloud storage 업로드 폴더 경로 지정", example = "brand 혹은 product 혹은 rate 혹은 error"),
            @Parameter(name = "type", description = "이미지 타입", example = "브랜드 or 상품 or 평점 or 에러"),
            @Parameter(name = "referencedId", description = "이미지의 참조 ID, 평점이나 에러는 아무 값이나 줘도 상관 x")
    })
    public String uploadImage(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("filePath") String filePath,
            @RequestParam("type") String type,
            @RequestParam("referenceId") Long referenceId
    ) {
        log.info("이미지 업로드 중");
        return imageService.uploadFileByAdmin(userInfoHeader, files, filePath, type, referenceId).toString();
    }

    @PostMapping("/uploadByUser")
    @Operation(summary = "이미지 업로드", description = "판매자 혹은 구매자가 경매와 환불 관련된 여러 이미지를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 등록 성공"),
            @ApiResponse(responseCode = "404", description = "사진 등록 실패")
    })
    @Parameters(value = {
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(name = "files", description = "업로드할 이미지 파일 목록", example = "여러 사진 파일"),
            @Parameter(name = "filePath", description = "NCloud storage 업로드 폴더 경로 지정", example = "auctions 혹은 refunds"),
            @Parameter(name = "type", description = "이미지 타입", example = "경매 or 환불"),
            @Parameter(name = "referencedId", description = "이미지의 참조 ID")
    })
    public String uploadImages(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("filePath") String filePath,
            @RequestParam("type") String type,
            @RequestParam("referenceId") Long referenceId
    ) {
        log.info("이미지 업로드 중");
        return imageService.uploadFilesByUser(userInfoHeader, files, filePath, type, referenceId).toString();
    }

    @PatchMapping
    @Operation(summary = "이미지 업데이트", description = "ID로 기존 이미지를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 수정 성공"),
            @ApiResponse(responseCode = "404", description = "사진 수정 실패")
    })
    @Parameters(value = {
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(description = "업데이트할 이미지 파일"),
            @Parameter(description = "업데이트할 이미지의 ID")
    })
    public String updateImages(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("id") String id) {
        log.info("이미지 업데이트 중");
        return imageService.update(userInfoHeader, files, id);
    }

    @DeleteMapping
    @Operation(summary = "이미지 삭제", description = "ID로 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사진 삭제 실패")
    })
    @Parameters({
            @Parameter(name = "UserInfo", description = "현재 로그인한 사용자 token", example = ""),
            @Parameter(name = "imageId", description = "삭제할 이미지의 id", example = "1")
    })
    public String deleteImages(
            @RequestHeader("UserInfo") String userInfoHeader,
            @RequestParam("id") String id) {
        log.info("이미지 삭제 중");
        return imageService.deleteById(userInfoHeader, id);
    }

}
