package com.example.backend.service;

import com.example.backend.model.Result;
import com.example.backend.repository.ResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class ResultService {

    private final ResultRepository resultRepo;
    private final RestTemplate restTemplate;

    public ResultService(ResultRepository resultRepo) {
        this.resultRepo = resultRepo;
        this.restTemplate = new RestTemplate(); // 외부 API 요청용
    }

    public Result searchAndSave(String email, String question) {
        // 1. LLM 요청
        String llmUrl = "http://localhost:9000/llm"; // 외부 LLM 서버 주소
        Map<String, String> request = Map.of("question", question);
        String llmResponse = "";

        try {
            llmResponse = restTemplate.postForObject(llmUrl, request, String.class);
        } catch (Exception e) {
            llmResponse = "⚠️ LLM 응답 오류: " + e.getMessage();
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
}
