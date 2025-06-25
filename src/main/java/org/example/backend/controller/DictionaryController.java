package org.example.backend.controller;

import org.example.backend.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dict")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping
    public String getDefinition(@RequestParam String q) {
        return dictionaryService.searchWord(q);
    }
}
