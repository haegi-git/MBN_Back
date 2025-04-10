package com.example.mbn.posts.dto;

import com.example.mbn.posts.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    @NotBlank
    @Size(min = 3, max = 15)
    private String title;
    @NotBlank
    @Size(min = 3, max = 500)
    private String content;

    @NotBlank
    private String platform;

    private String tag;

    private List<String> imageUrls;


    public Post toEntity(){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .platform(this.platform)
                .images(new ArrayList<>())
                .tag(this.tag).build();
    }

}
