package com.example.collaborationtest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    private int productId;
    private int userId;
    private String body;
    private int stars;
}
