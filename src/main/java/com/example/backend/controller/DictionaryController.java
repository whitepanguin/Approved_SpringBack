package com.example.backend.controller;

import com.example.backend.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dict")
@CrossOrigin(origins = "https://port-next-approved-front-m5mcnm8ebdc80276.sel4.cloudtype.app")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping
    public String getDefinition(@RequestParam("q") String q) {
        return dictionaryService.searchWord(q);
    }
}
