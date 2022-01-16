package inu.market.notice.controller;

import inu.market.notice.dto.NoticeCreateRequest;
import inu.market.notice.dto.NoticeResponse;
import inu.market.notice.dto.NoticeUpdateRequest;
import inu.market.notice.service.NoticeService;
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
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/api/notices")
    public ResponseEntity<Map<String, Long>> create(@RequestBody @Valid NoticeCreateRequest request) {
        Long noticeId = noticeService.create(request);

        Map<String, Long> response = new HashMap<>();
        response.put("noticeId", noticeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/notices/{noticeId}")
    public ResponseEntity<Void> update(@PathVariable Long noticeId,
                                       @RequestBody @Valid NoticeUpdateRequest request) {
        noticeService.update(noticeId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/notices/{noticeId}")
    public ResponseEntity<Void> delete(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/notices")
    public ResponseEntity<List<NoticeResponse>> findAll() {
        return ResponseEntity.ok(noticeService.findAll());
    }

    @GetMapping("/api/notices/{noticeId}")
    public ResponseEntity<NoticeResponse> findById(@PathVariable Long noticeId) {
       return ResponseEntity.ok(noticeService.findById(noticeId));
    }


}
