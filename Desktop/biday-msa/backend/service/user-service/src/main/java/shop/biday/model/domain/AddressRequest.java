package shop.biday.model.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest (
    @Schema(description = "주소", example = "서울강남구 강남대로 94번길 20")
    @NotBlank(message = "필수 값입니다.")
    String streetAddress,

    @Schema(description = "상세주소", example = "5-8층")
    String detailAddress,

    @Schema(description = "우편번호", example = "서울 강남구 역삼동 819-3")
    @NotBlank(message = "필수 값입니다.")
     String zipcode,

    @Schema(description = "타입", example = "Other/Home/Work")
    String type){

}
