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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @PostMapping("/api/majors/parents")
    public ResponseEntity<Map<String, Long>> createParent(@RequestBody @Valid MajorCreateRequest request) {
        Long majorId = majorService.createParent(request);

        Map<String, Long> response = new HashMap<>();
        response.put("majorId", majorId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/majors/{majorId}/children")
    public ResponseEntity<Map<String, Long>> createChild(@PathVariable Long majorId,
                                                         @RequestBody @Valid MajorCreateRequest request) {
        Long createMajorId = majorService.createChildren(majorId, request);

        Map<String, Long> response = new HashMap<>();
        response.put("majorId", createMajorId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/majors/{majorId}")
    public ResponseEntity<Void> update(@PathVariable Long majorId,
                                       @RequestBody @Valid MajorUpdateRequest request) {
        majorService.update(majorId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/majors/{majorId}")
    public ResponseEntity<Void> delete(@PathVariable Long majorId) {
        majorService.delete(majorId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/majors/parents")
    public ResponseEntity<List<MajorResponse>> findParents() {
        return ResponseEntity.ok(majorService.findParents());
    }

    @GetMapping("/api/majors/{majorId}/children")
    public ResponseEntity<List<MajorResponse>> findChildrenById(@PathVariable Long majorId) {
        return ResponseEntity.ok(majorService.findChildrenById(majorId));
    }

}
