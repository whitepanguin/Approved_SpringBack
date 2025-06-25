package org.example.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.backend.model.ReferenceDocument;
import org.example.backend.model.Result;
import org.example.backend.model.ResultContent;
import org.example.backend.repository.ResultRepository;
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
    private final RestTemplate restTemplate = new RestTemplate();
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

    public List<Result> getResultsByUserEmail(String email) {
        return resultRepo.findByEmailOrderByCreatedAtDesc(email);
    }

    public long getcount() {
        return resultRepo.count();
    }
}
