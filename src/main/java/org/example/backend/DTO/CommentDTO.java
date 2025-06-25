package org.example.backend.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CommentDTO {
    private String postId;
    private String userid;
    private String email;
    private String content;
}
