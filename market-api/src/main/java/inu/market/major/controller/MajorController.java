package inu.market.major.controller;

import inu.market.major.dto.MajorCreateRequest;
import inu.market.major.dto.MajorResponse;
import inu.market.major.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @PostMapping("/api/majors")
    public ResponseEntity<MajorResponse> create(@RequestBody @Valid MajorCreateRequest request) {
        return ResponseEntity.ok(majorService.create(request));
    }
}
