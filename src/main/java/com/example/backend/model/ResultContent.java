package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultContent {
    private String answer;
    private List<String> referenced_laws;
    private List<ReferenceDocument> reference_documents;
}