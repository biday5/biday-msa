package shop.biday.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserRequest (

    @Schema(description = "사용자 이름", example = "김비드")
    @NotBlank(message = "필수 값입니다.")
    String name,

    @Schema(description = "이메일", example = "test@bid.com")
    @NotBlank(message = "필수 값입니다.")
    String email,

    @Schema(description = "패스워드", example = "Biddaty12!@")
    @NotBlank(message = "필수 값입니다.")
    String password,

    @Schema(description = "전화번호", example = "010-0000-0000")
    @NotBlank(message = "필수 값입니다.")
    String phoneNum){

}
