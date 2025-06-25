package org.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "result")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    @Id
    private String id;

    private String email;
    private String question;

    // ✅ 여기: String → ResultContent
    private ResultContent result;

    private String address;
    private String file = "";
    private String map = "";
    private String createdAt;
}
