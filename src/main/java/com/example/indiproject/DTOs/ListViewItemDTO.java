package com.example.indiproject.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListViewItemDTO {
    List<ViewItemDTO> items;

    public void addItem(ViewItemDTO viewItemDTO){
        items.add(viewItemDTO);
    }
}
