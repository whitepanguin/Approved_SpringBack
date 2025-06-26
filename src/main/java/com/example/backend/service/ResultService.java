package com.example.backend.service;

import com.example.backend.model.Result;
import com.example.backend.model.ResultContent;
import com.example.backend.repository.ResultRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Result searchAndSave(String email, String question) {
        String llmUrl = "https://port-0-rag-legal-mc3ho385f405b6d9.sel5.cloudtype.app/rag";
        ResultContent parsedResult;

        try {
            Map<String, String> requestBody = Map.of("query", question);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    llmUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<?, ?> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("response")) {
                String json = objectMapper.writeValueAsString(responseBody.get("response"));
                parsedResult = objectMapper.readValue(json, ResultContent.class);
                System.out.println("✅ [LLM 응답 파싱 성공] " + parsedResult.getAnswer());
            } else {
                parsedResult = new ResultContent();
                parsedResult.setAnswer("⚠️ LLM 응답 파싱 실패");
                System.out.println("⚠️ 'response' 키 없음");
            }

        } catch (Exception e) {
            parsedResult = new ResultContent();
            parsedResult.setAnswer("⚠️ LLM 요청 오류: " + e.getMessage());
            System.out.println("⚠️ 예외 발생: " + e.getMessage());
        }

        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Result result = Result.builder()
                .email(email)
                .question(question)
                .result(parsedResult)
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

    // 이메일 기준 검색 결과 조회 메서드 (이름 충돌 피함)
    public List<Result> getResultsByUserEmail(String email) {
        try {
            List<Result> results = resultRepo.findByEmail(email);
            System.out.println("✅ 조회 결과 개수: " + results.size());
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;  // 필요하면 그대로 예외 던지기
        }
    }



    public long getcount() {
        return resultRepo.count();
    }
}
