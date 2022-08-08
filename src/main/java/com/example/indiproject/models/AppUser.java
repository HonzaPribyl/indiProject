package com.example.indiproject.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Double dollars;
    @OneToMany (mappedBy = "seller")
    private List<Item> sellingItems = new ArrayList<>();
    @OneToMany (mappedBy = "bidder")
    private List<Item> biddingItems = new ArrayList<>();

    public AppUser(String userName, String passWord, Double dollars) {
        this.username = userName;
        this.password = passWord;
        this.dollars = dollars;
    }
}
