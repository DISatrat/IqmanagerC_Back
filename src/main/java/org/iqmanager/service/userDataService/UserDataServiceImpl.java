package org.iqmanager.service.userDataService;

import lombok.SneakyThrows;
import org.iqmanager.dto.BasketDTO;
import org.iqmanager.dto.PurchasedNumbersDTO;
import org.iqmanager.models.*;
import org.iqmanager.models.enum_models.CalendarStatus;
import org.iqmanager.repository.CalendarDAO;
import org.iqmanager.repository.OrderElementDAO;
import org.iqmanager.repository.UserDataDAO;
import org.iqmanager.repository.UserLoginDataDAO;
import org.iqmanager.service.postService.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {

    private final UserDataDAO userDataDAO;
    private final UserLoginDataDAO userLoginDataDAO;
    private final PostService postService;
    private final PasswordEncoder passwordEncoder;
    private final OrderElementDAO orderElementDAO;
    private final CalendarDAO calendarDAO;

    @Autowired
    public UserDataServiceImpl(UserDataDAO userDataDAO, UserLoginDataDAO userLoginDataDAO, PostService postService,
                               PasswordEncoder passwordEncoder, OrderElementDAO orderElementDAO, CalendarDAO calendarDAO) {
        this.userLoginDataDAO = userLoginDataDAO;
        this.userDataDAO = userDataDAO;
        this.postService = postService;
        this.passwordEncoder = passwordEncoder;
        this.orderElementDAO = orderElementDAO;
        this.calendarDAO = calendarDAO;
    }

    /** Сохранение данных заказчика */
    @Override
    public void save(UserData userData) {
        userDataDAO.save(userData);
    }

    /** Регистрация нового заказчика */


    /** Получить заказчика по id */
    @Override
    public UserData getUser(long id) {
        UserData userData = userDataDAO.getById(id);
        userData.setFavorites(userDataDAO.getFirstById(id).getFavorites());
        return userData;
    }

    /** Получить текущего пользователя */
    @SneakyThrows
    @Override
    public UserData getLoginnedAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что аутентификация прошла успешно
        if (auth != null && auth.isAuthenticated()) {
            UserLoginData userLoginData = userLoginDataDAO.findByUsername(auth.getName());

            // Если пользователь существует, возвращаем его данные
            if (userLoginData != null) {
                return userLoginData.getUserData();
            } else {
                // Если пользователь не найден, выбрасываем исключение UsernameNotFoundException
                throw new UsernameNotFoundException("User not found with username: " + auth.getName());
            }
        } else {
            // Если пользователь не аутентифицирован, выбрасываем исключение AuthenticationCredentialsNotFoundException
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }
    }


    /** Залогинен ли пользователь */
    @Override
    public boolean hasUserLoginned() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    /** Получение корзины */
    @Override
    public Page<BasketDTO> getFilteredBasket(List<String> filter, Pageable pageable) {
        UserData userData = getUser(getLoginnedAccount().getId());
        List<OrderElement> orderElements = userData.getOrderElements().stream()
                .filter(orderElement -> filter.contains(orderElement.getStatusOrder()))
                .collect(Collectors.toList());
        List<BasketDTO> basketDTOs = orderElements.stream().map(BasketDTO::BasketToDTO).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), basketDTOs.size());

        if (start >= basketDTOs.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, basketDTOs.size());
        }

        return new PageImpl<>(basketDTOs.subList(start, end), pageable, basketDTOs.size());
    }


    @Override
    public Page<BasketDTO> getBasket(Pageable pageable) {
        UserData userData = getUser(getLoginnedAccount().getId());
        List<OrderElement> orderElements = userData.getOrderElements();
        List<BasketDTO> basketDTOs = orderElements.stream().map(BasketDTO::BasketToDTO).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), basketDTOs.size());

        if (start >= basketDTOs.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, basketDTOs.size());
        }

        return new PageImpl<>(basketDTOs.subList(start, end), pageable, basketDTOs.size());
    }


    /** Добавление в избранное */
    @Override
    public void addToFavorite(long id_post) {
        UserData user = getLoginnedAccount();
        List<Post> posts = user.getFavorites();
        Post post = postService.getPost(id_post);
        if(posts.isEmpty() || !posts.stream().map(Post::getId).collect(Collectors.toList()).contains(post.getId())){
            user.addToFavorites(post);
            userDataDAO.save(user);
        }
    }

    //todo Подключить оповещения админам и исполнителю, добавить планировщик
    /** Добавление в корзину */
    @Override
    public void addToBasket(OrderElement orderElement) {
        UserData userData = getUser(getLoginnedAccount().getId());
        orderElement.setUserData(userData);

        PerformerData performerData = orderElement.getPost().getPerformer();

        Calendar calendar = new Calendar();

        calendar.setStatus(String.valueOf(CalendarStatus.CONSIDERATION_OF_ORDER));
        calendar.setPerformer(performerData);
        calendar.setBeginDate(orderElement.getDateEvent());
        Duration duration = Duration.ofHours((long) orderElement.getDuration());
        calendar.setEndDate(orderElement.getDateEvent().plus(duration));
        calendar.setOrderElement(orderElement);
        orderElement.setCalendar(calendar);
        calendarDAO.save(calendar);
        orderElementDAO.save(orderElement);
    }

    /** Добавление в корзину */
    @Override
    public long addToBasketForUnregistered (OrderElement orderElement) {
        UserData userData = getUser(1);
        orderElement.setUserData(userData);
        return orderElementDAO.save(orderElement).getId();
    }

    @Override
    public void addPurchasedNumbers(Post post) {
        UserData userData = getLoginnedAccount();
        userData.addPurchasedPerformerNumbers(post);
        save(userData);
    }

    @Override
    public List<PurchasedNumbersDTO> getPurchaseNumbers() {
        Set<Post> posts = getLoginnedAccount().getPurchasedPerformerNumbers();
        return posts.stream().map(PurchasedNumbersDTO::createPurchasedNumbersDTO).collect(Collectors.toList());
    }

    // регистрация заказчика
    @Override
    public void register(String username, String password) {
        password = passwordEncoder.encode(password);
        UserLoginData userLoginData = new UserLoginData(username, password);
        userLoginData.setUserData(new UserData());
        userLoginDataDAO.save(userLoginData);
    }

    //регистрация исполнителя
    @Override
    public void registerPerformer(String username, String password) {
        password=passwordEncoder.encode(password);
        UserLoginData userLoginData= new UserLoginData(username,password);
        userLoginData.setUserData(new UserData());
        userLoginDataDAO.save(userLoginData);
    }



    /** Существует ли пользователь с таким логином */
    @Override
    public boolean userNotExists(String phone) {
        return userLoginDataDAO.findByUsername(phone) == null;
    }

    /** Сброс пароля */
    @Override
    public void passwordReset(String phone, String password) {
        UserLoginData userLoginData = userLoginDataDAO.findByUsername(phone);
        userLoginData.setPassword(passwordEncoder.encode(password));
        userLoginDataDAO.save(userLoginData);
    }
}
