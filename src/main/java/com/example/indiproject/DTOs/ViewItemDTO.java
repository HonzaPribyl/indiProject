package com.example.indiproject.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewItemDTO {
    private String name;
    private String description;
    private String photoUrl;
    private Double price;
    private String seller;
    private String highestPriceBuyer;
}
