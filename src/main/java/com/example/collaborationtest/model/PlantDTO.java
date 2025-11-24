package com.example.collaborationtest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantDTO {
    private String name;
    private String shortDescription;
    private String longDescription;
    private String plantMessage;
    private int productId;
}
