package com.example.membership.dto;

import com.example.membership.constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberShipDTO {

    private Long num;       // 회원읽기 등을 볼 때 pk 값 가지고 있어야함
                            // 등록시에는 필요없으니까 notnull 같은거 안씀

    @NotBlank(message = "이름을 꼭 쓰셔야 합니다.")
    @Size(min = 2, max = 10, message = "이름을 2~10글자로 작성하세요 ex)홍길동")
    private String name;    // 이름   // 낫 블링크 최소 : 2, 최대 : 10

    @NotBlank(message = "빈칸, 공백은 허용하지 않습니다. 이메일을 꼭 작성해야 합니다.")
    @Size(max = 50, message = "최대 50글자 입니다.")
    @Email(message = "이메일 형식에 맞춰서 작성하시오")
    private String email;   // 이메일   // email, 최대값은 20글자 : 메시지 :최대 50글자 이내로 작성하셔야 합니다.

    @NotBlank(message = "비밀번호는 꼭 작성해야 합니다.")
    @Size(min = 10, max = 16, message = "최소 10에서 최대 16이내로 비밀번호를 작성하시오")
    private String password; // 비밀번호  // 낫블링크,  제약 : not null

    @NotBlank(message = "주소는 꼭 작성해야 합니다.")
    private String address;  // 주소     // 낫 블링크

    // 권한

    private Role role;
}
