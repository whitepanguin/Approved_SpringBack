package com.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "result")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    @Id
    private String id;
    @Field("email")
    private String email;
    private String question;
    @Field("result")
    private ResultContent result;
    private String address;
    private String file = "";
    private String map = "";
    private String createdAt;



}
