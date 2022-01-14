package inu.market.major.dto;

import inu.market.major.domain.Major;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorResponse {

    private Long majorId;

    private String name;

    public static MajorResponse from(Major major) {
        return new MajorResponse(major.getId(), major.getName());
    }
}
