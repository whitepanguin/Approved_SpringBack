package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

@Service
public class DictionaryService {

    public String searchWord(String keyword) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://stdict.korean.go.kr/api/search.do")
                    .queryParam("certkey_no", "7734")
                    .queryParam("key", "F40F3A9D58EC3A8446C7B73DD12A5815")
                    .queryParam("type_search", "search")
                    .queryParam("req_type", "json")
                    .queryParam("q", keyword)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"사전 API 요청 실패\"}";
        }
    }
}
