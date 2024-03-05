package com.myblog8.payload;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {


    private Long id;

    @NotEmpty
    @Size(min = 2,message = "Post title should have at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 2,message = "Post description should have at least 10 characters")
    private String description;

    @NotEmpty
    private String content;

}
