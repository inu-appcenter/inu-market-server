package inu.market.major.controller;

import inu.market.major.dto.MajorCreateRequest;
import inu.market.major.dto.MajorResponse;
import inu.market.major.dto.MajorUpdateRequest;
import inu.market.major.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @PostMapping("/api/majors")
    public ResponseEntity<MajorResponse> createParent(@RequestBody @Valid MajorCreateRequest request) {
        return ResponseEntity.ok(majorService.createParent(request));
    }

    @PostMapping("/api/majors/{majorId}/children")
    public ResponseEntity<MajorResponse> createChild(@PathVariable Long majorId,
                                                     @RequestBody @Valid MajorCreateRequest request) {
        return ResponseEntity.ok(majorService.createChildren(majorId, request));
    }

    @PutMapping("/api/majors/{majorId}")
    public ResponseEntity<MajorResponse> update(@PathVariable Long majorId,
                                                @RequestBody @Valid MajorUpdateRequest request) {
        return ResponseEntity.ok(majorService.update(majorId, request));
    }

    @DeleteMapping("/api/majors/{majorId}")
    public ResponseEntity<MajorResponse> delete(@PathVariable Long majorId) {
        majorService.delete(majorId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/majors")
    public ResponseEntity<List<MajorResponse>> findAll() {
        return ResponseEntity.ok(majorService.findAll());
    }

}
