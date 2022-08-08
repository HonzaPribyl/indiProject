package com.example.indiproject.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String photoUrl;
    private Double price;
    @ManyToOne
    private AppUser seller;
    @ManyToOne
    private AppUser bidder;

    public Item(String name, String description, String photoUrl, Double price, AppUser seller) {
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.price = price;
        this.seller = seller;
    }
}
