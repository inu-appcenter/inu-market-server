package inu.market.notice.controller;

import inu.market.notice.dto.NoticeCreateRequest;
import inu.market.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
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
}
