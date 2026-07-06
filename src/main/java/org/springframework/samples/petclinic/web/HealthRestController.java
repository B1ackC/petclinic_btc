package org.springframework.samples.petclinic.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 로드밸런서(ALB) 헬스체크용 경량 엔드포인트.
 * DB에 의존하지 않는 liveness 응답이라 RDS 상태와 무관하게 인스턴스 생존만 확인한다.
 * 경로: 컨텍스트 + /api/health (예: /petclinic/api/health) -> 200 {"status":"UP"}
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthRestController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", "UP");
        return ResponseEntity.ok(body);
    }
}
