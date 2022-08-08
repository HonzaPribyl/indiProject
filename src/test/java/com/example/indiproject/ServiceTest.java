package com.example.indiproject;

import com.example.indiproject.DTOs.ListViewItemDTO;
import com.example.indiproject.DTOs.ViewItemDTO;
import com.example.indiproject.models.AppUser;
import com.example.indiproject.models.Item;
import com.example.indiproject.repositories.AppUserRepository;
import com.example.indiproject.repositories.ItemRepository;
import com.example.indiproject.services.api.AppUserService;
import com.example.indiproject.services.api.ItemService;
import com.example.indiproject.services.impl.AppUserServiceImpl;
import com.example.indiproject.services.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ServiceTest {
    @Test
    public void appUserWithUsernameExistsTrue() {
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        AppUserService mockAppUserService = new AppUserServiceImpl(mockAppUserRepository);
        Mockito.when(mockAppUserRepository.findByUsername(any())).thenReturn(new AppUser("Kuba", "password123", 100.0));
        assertEquals(true, mockAppUserService.appUserWithUserNameExists("Kuba"));
    }

    @Test
    public void appUserWithUsernameExistsFalse() {
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        AppUserService mockAppUserService = new AppUserServiceImpl(mockAppUserRepository);
        Mockito.when(mockAppUserRepository.findByUsername(any())).thenReturn(null);
        assertEquals(false, mockAppUserService.appUserWithUserNameExists("Kuba"));
    }

    @Test
    public void appUserNameIsAppropriateTrue() {
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        AppUserService mockAppUserService = new AppUserServiceImpl(mockAppUserRepository);
        assertEquals(true, mockAppUserService.appUserNameIsAppropriate("Kuba"));
    }

    @Test
    public void appUserNameIsAppropriateFalse() {
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        AppUserService mockAppUserService = new AppUserServiceImpl(mockAppUserRepository);
        assertEquals(false, mockAppUserService.appUserNameIsAppropriate("MotherFucker"));
    }

    @Test
    public void urlIsValidTrue() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        assertEquals(true, mockItemService.urlIsValid("https://www.youtube.com/watch?v=OG0AvwM_QAI&ab_channel=AquaVEVO"));
    }

    @Test
    public void urlIsValidFalse() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        assertEquals(false, mockItemService.urlIsValid("What the hell is URL?"));
    }

    @Test
    public void listItems() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        AppUser appUser = new AppUser("Kuba", "password123", 100.0);
        Item item = new Item("ball", "Good for throwing", "https://www.sportisimo.cz/galerie-produktu/?produkt=738519&varianta=3203479&obrazek=1432107", 5.5, appUser);
        item.setBidder(appUser);
        List<Item> items = new ArrayList<>();
        items.add(item);
        Mockito.when(mockItemRepository.findAll()).thenReturn(items);
        ViewItemDTO viewItemDTO = new ViewItemDTO("ball", "Good for throwing", "https://www.sportisimo.cz/galerie-produktu/?produkt=738519&varianta=3203479&obrazek=1432107", 5.5, "Kuba", "Kuba");
        List<ViewItemDTO> viewItemDTOS= new ArrayList<>();
        viewItemDTOS.add(viewItemDTO);
        ListViewItemDTO listViewItemDTO = new ListViewItemDTO(viewItemDTOS);
        assertEquals(listViewItemDTO, mockItemService.listItems(1));
    }

    @Test
    public void viewItem() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        AppUser appUser = new AppUser("Kuba", "password123", 100.0);
        Item item = new Item("ball", "Good for throwing", "https://www.sportisimo.cz/galerie-produktu/?produkt=738519&varianta=3203479&obrazek=1432107", 5.5, appUser);
        item.setBidder(appUser);
        Mockito.when(mockItemRepository.getReferenceById(any())).thenReturn(item);
        ViewItemDTO viewItemDTO = new ViewItemDTO("ball", "Good for throwing", "https://www.sportisimo.cz/galerie-produktu/?produkt=738519&varianta=3203479&obrazek=1432107", 5.5, "Kuba", "Kuba");
        assertEquals(viewItemDTO, mockItemService.viewItem(1L));
    }

    @Test
    public void itemWithIdExistsTrue() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        Mockito.when(mockItemRepository.getReferenceById(any())).thenReturn(new Item());
        assertEquals(true, mockItemService.itemWithIdExists(1L));
    }

    @Test
    public void itemWithIdExistsFalse() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        Mockito.when(mockItemRepository.getReferenceById(any())).thenReturn(null);
        assertEquals(false, mockItemService.itemWithIdExists(1L));
    }

    @Test
    public void hasEnoughDollarsTrue() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        Mockito.when(mockAppUserRepository.getReferenceById(any())).thenReturn(new AppUser("Kuba", "password123", 10.0));
        assertEquals(true, mockItemService.hasEnoughDollars(1L, 7.0));
    }

    @Test
    public void hasEnoughDollarsFalse() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        Mockito.when(mockAppUserRepository.getReferenceById(any())).thenReturn(new AppUser("Kuba", "password123", 10.0));
        assertEquals(false, mockItemService.hasEnoughDollars(1L, 13.0));
    }

    @Test
    public void bidHighEnoughTrue() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        Mockito.when(mockItemRepository.getReferenceById(any())).thenReturn(new Item("ball", "good for throwing", "https://www.sportisimo.cz/galerie-produktu/?produkt=738519&varianta=3203479&obrazek=1432107", 5.5, new AppUser()));
        assertEquals(true, mockItemService.bidHighEnough(1L, 7.));
    }

    @Test
    public void bidHighEnoughFalse() {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        AppUserRepository mockAppUserRepository = Mockito.mock(AppUserRepository.class);
        ItemService mockItemService = new ItemServiceImpl(mockItemRepository, mockAppUserRepository);
        Mockito.when(mockItemRepository.getReferenceById(any())).thenReturn(new Item("ball", "good for throwing", "https://www.sportisimo.cz/galerie-produktu/?produkt=738519&varianta=3203479&obrazek=1432107", 5.5, new AppUser()));
        assertEquals(false, mockItemService.bidHighEnough(1L, 4.));
    }
}
