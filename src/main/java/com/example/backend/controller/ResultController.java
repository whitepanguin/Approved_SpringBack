package com.example.backend.controller;

import com.example.backend.model.Result;
import com.example.backend.service.ResultService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/searchllm")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public Result handleSearchLlm(@RequestParam String search,
                                  @RequestParam String email) {
        return resultService.searchAndSave(email, search);
    }
}
