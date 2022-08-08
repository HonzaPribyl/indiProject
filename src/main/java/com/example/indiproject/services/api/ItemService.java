package com.example.indiproject.services.api;

import com.example.indiproject.DTOs.CreateItemDTO;
import com.example.indiproject.DTOs.ListItemsDTO;
import com.example.indiproject.DTOs.ListViewItemDTO;
import com.example.indiproject.DTOs.ViewItemDTO;

public interface ItemService {
    boolean urlIsValid(String url);

    void createItem (CreateItemDTO createItemDTO, Long id);

    ListViewItemDTO listItems(Integer n);

    ViewItemDTO viewItem(Long id);

    boolean itemWithIdExists(Long id);

    boolean hasEnoughDollars(Long appUserId, Double bid);

    boolean bidHighEnough(Long id, Double bid);

    void bid(Long appUserId, Long itemId, Double bid);
}
