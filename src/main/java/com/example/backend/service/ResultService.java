package com.example.backend.service;

import com.example.backend.model.Result;
import com.example.backend.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepo;
    private final RestTemplate restTemplate = new RestTemplate(); // 외부 API 요청용

    public Result searchAndSave(String email, String question) {
        // 1. LLM 요청
        String llmUrl = "https://port-0-rag-legal-mc3ho385f405b6d9.sel5.cloudtype.app/rag";
        String llmResponse = " ";

        try {
            // 요청 Body
            Map<String, String> requestBody = Map.of("query", question);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 응답 처리
            ResponseEntity<Map> response = restTemplate.exchange(
                    llmUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<?, ?> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("response")) {
                llmResponse = responseBody.get("response").toString();
            } else {
                llmResponse = "⚠️ LLM 응답 파싱 실패";
            }

        } catch (Exception e) {
            llmResponse = "⚠️ LLM 요청 오류: " + e.getMessage();
        }

        // 2. 현재 시간
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 3. Mongo 저장
        Result result = Result.builder()
                .email(email)
                .question(question)
                .result(llmResponse)
                .address("")
                .file("")
                .map("")
                .createdAt(createdAt)
                .build();

        return resultRepo.save(result);
    }
    public List<Result> getResultsByEmail(String email) {
        return resultRepo.findByEmail(email);
    }
}
