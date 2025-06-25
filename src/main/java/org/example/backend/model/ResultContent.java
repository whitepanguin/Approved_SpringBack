package org.example.backend.model;

import lombok.Data;

import java.util.List;

@Data
public class ResultContent {
    private String answer;
    private List<String> referenced_laws;
    private List<ReferenceDocument> reference_documents;
}
