package inu.market.major.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorUpdateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;
}
