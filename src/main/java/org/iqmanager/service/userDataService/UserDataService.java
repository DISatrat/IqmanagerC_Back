package org.iqmanager.service.userDataService;

import org.iqmanager.dto.BasketDTO;
import org.iqmanager.dto.PurchasedNumbersDTO;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.PerformerData;
import org.iqmanager.models.Post;
import org.iqmanager.models.UserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDataService {
    void save(UserData userData);
    void register(String username, String password);
    UserData getUser(long id);
    UserData getLoginnedAccount();
    boolean hasUserLoginned();
    void addToFavorite(long id_post);
    void addToBasket(OrderElement orderElement);
    boolean userNotExists(String phone);
    Page<BasketDTO> getFilteredBasket(List<String> filter, Pageable pageable);
    Page<BasketDTO> getBasket(Pageable pageable);
    void passwordReset(String phone, String password);
    long addToBasketForUnregistered (OrderElement orderElement);
    void addPurchasedNumbers(Post post);
    List<PurchasedNumbersDTO> getPurchaseNumbers();

    void registerPerformer(String username, String password);
}
