package com.green1st.mandalartWeb.shared_project.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SharedProjectDetailDto {
    private long projectId;
    private String nickName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likeFg;
}