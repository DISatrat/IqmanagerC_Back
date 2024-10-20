package org.iqmanager.service.orderElementService;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.iqmanager.dto.BasketDTO;
import org.iqmanager.dto.ExtraForOrderElementDTO;
import org.iqmanager.dto.OrderElemDTO;
import org.iqmanager.models.*;
import org.iqmanager.models.Calendar;
import org.iqmanager.models.enum_models.CalendarStatus;
import org.iqmanager.models.enum_models.DeliveryType;
import org.iqmanager.repository.CalendarDAO;
import org.iqmanager.repository.OrderElementDAO;
import org.iqmanager.scheduler.AutoRejectScheduler;
import org.iqmanager.service.calendarService.CalendarService;
import org.iqmanager.service.orderedExtraService.OrderedExtraService;
import org.iqmanager.service.postService.PostService;
import org.iqmanager.service.ratesService.RatesService;
import org.iqmanager.service.shedulerService.SchedulerService;
import org.iqmanager.service.userDataService.UserDataService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrderElementServiceImpl implements OrderElementService {


    private final RatesService ratesService;
    private final UserDataService userDataService;
    private final OrderElementDAO orderElementDAO;
    private final OrderedExtraService orderedExtraService;
    private final PostService postService;
    private final SchedulerService schedulerService;
    private final CalendarService calendarService;
    private final CalendarDAO calendarDAO;



    public OrderElementServiceImpl(RatesService ratesService, UserDataService userDataService,
                                   OrderElementDAO orderElementDAO, OrderedExtraService orderedExtraService,
                                   PostService postService, SchedulerService schedulerService, CalendarService calendarService, CalendarDAO calendarDAO) {
        this.ratesService = ratesService;
        this.userDataService = userDataService;
        this.orderElementDAO = orderElementDAO;
        this.orderedExtraService = orderedExtraService;
        this.postService = postService;
        this.schedulerService = schedulerService;
        this.calendarService = calendarService;
        this.calendarDAO = calendarDAO;
    }

    //todo планировщик и смс
    @SneakyThrows
    @Override
    public OrderElement save(OrderElement orderElement) {
        OrderElement orderElementNew = orderElementDAO.save(orderElement);
        schedulerService.saveOrUpdate(new SchedulerJobInfo("autoReject+" + orderElementNew.getId(), "autoReject", AutoRejectScheduler.class.getName(), ""));
        return orderElementNew;
    }

    /** Удалить элемент заказа из корзины */
    @Override
    @Transactional
    public void deleteOrderElem(long id) {

        OrderElement orderElement = orderElementDAO.getOne(id);

        calendarDAO.delete(orderElement.getCalendar());

        Set<OrderedExtras> orderedExtras = orderElement.getOrderedExtras();
        for(OrderedExtras orderedExtra : orderedExtras) {
            orderedExtra.setRatesAndServices(new HashSet<>());
            orderedExtraService.delete(orderedExtra.getId());
        }
        orderElementDAO.deleteById(id);
    }

    /** Очистить корзину */
    @Override
    @Transactional
    public void deleteAllOderElem() {
        UserData userData = userDataService.getLoginnedAccount();
        List<Long> orderElementList = userData.getOrderElements().stream().map(OrderElement::getId).collect(Collectors.toList());

        List<Calendar> calendarList = userData.getOrderElements().stream().map(OrderElement::getCalendar).collect(Collectors.toList());

        calendarList.forEach(x-> calendarDAO.deleteById(x.getId()));

        for(long orderElementId : orderElementList) {
            OrderElement orderElement = orderElementDAO.getOne(orderElementId);
            Set<OrderedExtras> orderedExtras = orderElement.getOrderedExtras();
            for(OrderedExtras orderedExtra : orderedExtras) {
                orderedExtra.setRatesAndServices(new HashSet<>());
                orderedExtraService.save(orderedExtra);
                orderedExtraService.delete(orderedExtra.getId());
            }
            userData.deleteFromBasket(orderElement);
            userDataService.save(userData);
            orderElementDAO.deleteById(orderElementId);
        }
    }
    /** Получить элемент заказа по id */
    @Override
    public OrderElement getOrderElement(long id) {
        return orderElementDAO.findById(id).orElse(null);
    }

    /** Получить DTO заказа по id */
    @Override
    public BasketDTO getBasketDTO(long id) {
        OrderElement orderElement = orderElementDAO.getOne(id);
        return BasketDTO.BasketToDTO(orderElement);
    }


    /** Получить номер телефона заказчика по id элемента заказа */
    @Override
    public String getCustomerPhone(long id) {
        return orderElementDAO.getOne(id).getUserData().getUserLoginData().getUsername();
    }

    public long calculatePriceChange(double partInsideDuration, long tFactor, Calendar calendar) {
        long priceChange = 0;

        if (calendar.getStatus() == CalendarStatus.PRICE_UP) {
            // Используйте правильное значение partInsideDuration
            priceChange = (long) (partInsideDuration * tFactor * (calendar.getChangePrice() / 100.0));
        } else if (calendar.getStatus() == CalendarStatus.PRICE_DOWN) {
            priceChange = (long) (partInsideDuration * tFactor * (calendar.getChangePrice() / 100.0));
        } else if (calendar.getStatus() == CalendarStatus.PRICE_DOWN_FOR_AGENT && userDataService.getLoginnedAccount().isAgent()) {
            priceChange = (long) (partInsideDuration * tFactor * (calendar.getChangePrice() / 100.0));
        }
        return priceChange; // Возвращаем рассчитанное изменение цены
    }


    @Override
    public long calculatePrice(OrderElemDTO orderElemDTO, OrderElement orderElement) {
        long priceRates = 0;
        double tariff;
        boolean hasRate = false;
        long result;
        Post post = postService.getPost(orderElemDTO.getIdPost());
        for (ExtraForOrderElementDTO extraForOrderElementDTO : orderElemDTO.getExtra()) {
            if (Objects.equals(extraForOrderElementDTO.getType(), "SELECT_SINGLE")) {
                hasRate = true;
                break;
            }
        }
        for (OrderedExtras extras : orderElement.getOrderedExtras()) {
            if (Objects.equals(extras.getType(), "SELECT_MULTIPLE")) {
                priceRates += extras.getRatesAndServices().stream().mapToLong(RatesAndServices::getPrice).sum();
            }
        }

        if (orderElemDTO.getExtra() != null && !orderElemDTO.getExtra().isEmpty() && hasRate) {
            tariff = ratesService.getPriceService(orderElemDTO.getTariffId());
        } else {
            tariff = postService.getPost(orderElemDTO.getIdPost()).getPrice();
        }
        if (Objects.equals(post.getPaymentType(), "HOURS_AND_FIX")) {
            result = (long) (post.getPrice() + post.getConditions().getAdditionalPrice() * orderElemDTO.getDuration());
        } else {
            result = (long) ((tariff * orderElemDTO.getFactor()) + priceRates);
        }

        List<Calendar> calenders = calendarService.getCalendarByPost(orderElemDTO.getIdPost());

        long beginEvent = orderElemDTO.getDateEvent().getEpochSecond();
        long endEvent = orderElemDTO.getDateEvent().plusSeconds((long) (orderElemDTO.getDuration() * 3600)).getEpochSecond();

        for (Calendar calendar : calenders) {
            long tFactor = (long) (tariff * orderElemDTO.getFactor());
            if (beginEvent >= calendar.getBeginDate().getEpochSecond()) {
                if (endEvent <= calendar.getEndDate().getEpochSecond()) {
                    // Полное совпадение

                    result += calculatePriceChange(1.0, tFactor, calendar);
                } else {
                    // Частичное совпадение
                    long partInsidePeriod = Math.max(0, calendar.getEndDate().getEpochSecond() - beginEvent);
                    double partInsideDuration = partInsidePeriod / (double) (endEvent - beginEvent);
                    result += calculatePriceChange(partInsideDuration, tFactor, calendar);
                }
            } else {
                if (endEvent <= calendar.getEndDate().getEpochSecond()) {
                    // Если заказ начинается до изменения цены, но заканчивается в нём
                    long partInsidePeriod = Math.max(0, endEvent - calendar.getBeginDate().getEpochSecond());
                    double partInsideDuration = partInsidePeriod / (double) (endEvent - beginEvent);
                    result += calculatePriceChange(partInsideDuration, tFactor, calendar);
                } else {
                    // Если событие начинается до периода изменения цен и заканчивается после
                    long partInsidePeriod = Math.max(0, calendar.getEndDate().getEpochSecond() - calendar.getBeginDate().getEpochSecond());
                    double partInsideDuration = partInsidePeriod / (double) (endEvent - beginEvent);
                    result += calculatePriceChange(partInsideDuration, tFactor, calendar);
                }
            }
        }

        return result;
    }
    public static long getLength(String responseBody) {
        JSONObject response = new JSONObject(responseBody);
        JSONArray routes = response.getJSONArray("routes");
        JSONObject firstRoute = routes.getJSONObject(0);
        JSONArray legs = firstRoute.getJSONArray("legs");
        JSONObject firstLeg = legs.getJSONObject(0);
        JSONObject distance = firstLeg.getJSONObject("distance");
        Integer distValue = distance.getInt("value");
        long numKm = (long)Math.ceil(distValue*1.0/1000);
//        for (int i = 0 ; i < albums.length(); i++) {
//            JSONObject album = albums.getJSONObject(i);
//            int userId = album.getInt("userId");
//            int id = album.getInt("id");
//            String title = album.getString("title");
//            System.out.println(id+" "+title+" "+userId);
//        }
        return numKm;
    }


    private static HttpURLConnection conn;
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    //todo подключить api
    @Override
    public double calculateDelivery(String address, long idPost, Double distance) {
        if (address.trim().length() == 0){
            return 0;
        }
        //address = "Москва|Бауманская 62-66|Текстильщики метро";
        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        try{
            Post post = postService.getPost(idPost);
            String postOriginAddress = post.getAddress(); // начальная точка
            String[] userAddressesList = address.split("\\|");
            String destinationAddress = ""; // финальная точка
            ArrayList<String> waypoints = new ArrayList<String>(); // промежут. точки
            for (int i = 0;i < userAddressesList.length; i++){
                if (i == userAddressesList.length - 1){
                    destinationAddress = userAddressesList[i];
                    break;
                }
                waypoints.add(userAddressesList[i]);
            }
            String baseURL = "https://maps.googleapis.com/maps/api/directions/json?";
            String apiKey = "AIzaSyAaYkX_QpZoFjldHhd0W9FuY0nlOR9blxU";

            StringBuilder formattedWaypoints = new StringBuilder();
            if (waypoints.size()>0){
                for (int i = 0; i < waypoints.size() - 1; i++){
                    formattedWaypoints.append("via:");
                    formattedWaypoints.append(waypoints.get(i));
                    formattedWaypoints.append("|");

                }
            }
            URIBuilder builder = new URIBuilder()
                    .setScheme("https")
                    .setHost("maps.googleapis.com")
                    .setPath("/maps/api/directions/json")
                    .addParameter("origin", postOriginAddress)
                    .addParameter("waypoints","")
                    .addParameter("destination", destinationAddress.toString())
                    .addParameter("key",apiKey);


            URL url = new URL(builder.toString());
            conn = (HttpURLConnection) url.openConnection();

            // Request setup
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);// 5000 milliseconds = 5 seconds
            conn.setReadTimeout(5000);

            // Test if the response from the server is successful
            int status = conn.getResponseCode();

            if (status >= 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }

            double lengthKm = (distance == null) ? 0 : distance;

            String deliveryType = post.getDeliveryType();
            DeliveryType parsedType = DeliveryType.valueOf(deliveryType);
            Conditions conditions = post.getConditions();
            long performerRadius = conditions.getRadius();
            long pricePerKm = post.getPriceForDeliveryKm();
            long priceFix = post.getPriceForDeliveryFix();
            double difference = lengthKm - performerRadius;
            switch (parsedType) {
                case FIXED:
                    return priceFix;
                case FREE_LIMITED_KILOMETER:
                    if (difference > 0){
                        return difference * pricePerKm;
                    }else {
                        return 0;
                    }
                case FREE_LIMITED_FIXED:
                    if (difference > 0){
                        return priceFix;
                    }else {
                        return 0;
                    }
                case PER_KILOMETER:
                    return lengthKm * pricePerKm;
                case FIXED_AND_PER_KILOMETER:
                    if (difference > 0){
                        return priceFix + (difference * pricePerKm);
                    } else {
                        return priceFix;
                    }
                case  FIXED_PLUS_FIXED:
                    if (difference > 0){
                        return priceFix+priceFix;
                    }else {
                        return priceFix;
                    }
                case NO_DELIVERY:
                default:
                    return 0;
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }

        return 0;
    }



}