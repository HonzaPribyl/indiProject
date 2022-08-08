package com.example.indiproject.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDTO {
    private String name;
    private String description;
    private String photoUrl;
    private double price;
}
