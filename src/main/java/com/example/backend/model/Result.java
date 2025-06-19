package com.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "result")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Result {

    @Id
    private String id;
    private String email;
    private String question;
    private String result;
    private String address;
    private String file = "";
    private String map = "";
    private String createdAt;

    // 생성자, Getter/Setter
    public Result() {}

    public Result(String email, String question, String result, String address) {
        this.email = email;
        this.question = question;
        this.result = result;
        this.address = address;
    }

}
