package com.example.mbn.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CommunityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String description;
    private String image;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    private String timeStamp;
    private Long likeCount;
    private Long viewCount;
    private Long reportCount;
}
