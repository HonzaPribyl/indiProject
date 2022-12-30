package com.example.indiproject.services.impl;

import com.example.indiproject.DTOs.CreateItemDTO;
import com.example.indiproject.DTOs.ListViewItemDTO;
import com.example.indiproject.DTOs.ViewItemDTO;
import com.example.indiproject.models.Item;
import com.example.indiproject.repositories.AppUserRepository;
import com.example.indiproject.repositories.ItemRepository;
import com.example.indiproject.services.api.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;
    @Override
    public boolean urlIsValid(String url) {
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void createItem(CreateItemDTO createItemDTO, Long id) {
        itemRepository.save(new Item(createItemDTO.getName(), createItemDTO.getDescription(), createItemDTO.getPhotoUrl(), createItemDTO.getPrice(), appUserRepository.getReferenceById(id)));
    }

    @Override
    public ListViewItemDTO listItems(Integer n) {
        int c = 20;
        if (n != null) {
            c *= n;
        }
        List<Item> items = itemRepository.findAll();
        if (c > items.size()) {
            c = items.size();
        }
        ListViewItemDTO listViewItemDTO = new ListViewItemDTO(new ArrayList<>());
        for (int i = 0; i < c; i++) {
            String bidder;
            if (items.get(i).getBidder() != null) {
                bidder = items.get(i).getBidder().getUsername();
            } else {
                bidder = "";
            }
            listViewItemDTO.addItem(new ViewItemDTO(items.get(i).getName(), items.get(i).getDescription(), items.get(i).getPhotoUrl(), items.get(i).getPrice(), items.get(i).getSeller().getUsername(), bidder));
        }
        return listViewItemDTO;
    }

    @Override
    public ViewItemDTO viewItem(Long id) {
        Item item = itemRepository.getReferenceById(id);
        String bidder;
        if (item.getBidder() != null) {
            bidder = item.getBidder().getUsername();
        } else {
            bidder = "";
        }
        return new ViewItemDTO(item.getName(), item.getDescription(), item.getPhotoUrl(), item.getPrice(), item.getSeller().getUsername(), bidder);
    }

    @Override
    public boolean itemWithIdExists(Long id) {
        return (itemRepository.getReferenceById(id) != null);
    }

    @Override
    public boolean hasEnoughDollars(Long id, Double bid) {
        return (bid <= appUserRepository.getReferenceById(id).getDollars());
    }

    @Override
    public boolean bidHighEnough(Long id, Double bid) {
        return (bid >= itemRepository.getReferenceById(id).getPrice());
    }

    @Override
    public void bid(Long appUserId, Long itemId, Double bid) {
        Item item = itemRepository.getReferenceById(itemId);
        item.setPrice(bid);
        item.setBidder(appUserRepository.getReferenceById(appUserId));
        itemRepository.save(item);
    }
}
