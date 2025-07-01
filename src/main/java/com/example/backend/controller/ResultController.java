package com.example.backend.controller;


import com.example.backend.model.Result;
import com.example.backend.service.ResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/searchllm")
@CrossOrigin(origins = "https://port-next-approved-front-m5mcnm8ebdc80276.sel4.cloudtype.app")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public Result handleSearchLlm(@RequestParam("search") String search,
                                  @RequestParam("email") String email) {
        return resultService.searchAndSave(email, search);
    }
    @GetMapping("/results")
    public List<Result> getResultsByEmail(@RequestParam String email) {
        return resultService.getResultsByEmail(email);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount() {
        try {
            long count = resultService.getcount();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "검색 수 조회",
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "글 수 조회 실패"));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Result>> getResultsByUserEmail(@PathVariable String email) {
        List<Result> results = resultService.getResultsByUserEmail(email);
        return ResponseEntity.ok(results);
    }

}
