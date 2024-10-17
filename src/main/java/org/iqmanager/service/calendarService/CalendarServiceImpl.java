package org.iqmanager.service.calendarService;

import org.iqmanager.models.Calendar;
import org.iqmanager.models.PerformerData;
import org.iqmanager.models.Post;
import org.iqmanager.models.enum_models.CalendarStatus;
import org.iqmanager.repository.CalendarDAO;
import org.iqmanager.repository.PostDAO;
import org.iqmanager.service.performerService.PerformerService;
import org.iqmanager.service.postService.PostService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@EnableScheduling
public class CalendarServiceImpl implements CalendarService {

    private final CalendarDAO calendarDAO;
    private final PerformerService performerService;
    private final PostService postService;
    private final PostDAO postDAO;

    public CalendarServiceImpl(CalendarDAO calendarDAO, PerformerService performerService, PostService postService, PostDAO postDAO) {
        this.calendarDAO = calendarDAO;
        this.performerService = performerService;
        this.postService = postService;
        this.postDAO = postDAO;
    }

    /**
     * Получение календаря
     */
    @Override
    public List<Calendar> getCalendar(PerformerData performerData) {

        List<Calendar> allByPerformer = calendarDAO.getAllByPerformer(performerData);
//        allByPerformer.forEach(x -> x.setBeginDate(x.getBeginDate().plus(3, ChronoUnit.HOURS)));
//        allByPerformer.forEach(x -> x.setEndDate(x.getEndDate().plus(3, ChronoUnit.HOURS)));

        allByPerformer.forEach(x-> System.out.println(x.getBeginDate()));
        return allByPerformer;
    }

    @Override
    public List<Calendar> getCalendarByPost(Long postId) {
        PerformerData performerData = postService.getPerformerByPostId(postId);
        return calendarDAO.getAllByPerformer(performerData);
    }

//    public void returnOriginPrice(){
//
//        if (currentDate.compareTo(calendar.getEndDate()) >= 0) {
//                    System.out.println("14142848274823748927848291");
//                    // Обычная цена становится равной измененной цене, а измененная цена обнуляется
//                    post.setPrice(post.getCalendarStatus());
//                    post.setCalendarStatus(0);
//                    postDAO.save(post);
//
//                }
//    }

    @Scheduled(fixedRate = 5000)
    public void updatePriceBasedOnCalendar() {
        Instant currentDate = Instant.now().plus(3,ChronoUnit.HOURS);
        Instant startDate = currentDate.minus(1, ChronoUnit.HOURS);
        Instant endDate = currentDate.plus(1, ChronoUnit.HOURS);

        List<Calendar> calendars = calendarDAO.findByBeginDateBeforeAndEndDateAfter(endDate, startDate);

        List<Calendar> calendarCopy = new ArrayList<>(calendars);

        calendarCopy.forEach(calendar -> {
            PerformerData performerData = calendar.getPerformer();
            List<Post> posts = performerData.getPosts();
            List<Post> postCopy = new ArrayList<>(posts);

            postCopy.forEach(post -> {
                calendarCopy.forEach(calendar1 -> System.out.println(calendar1.getId()));

                if (post.getCalendarStatus() == 0 && calendar.getUsed()!=1) {
                    post.setCalendarStatus(post.getPrice());
                    postDAO.save(post);
                    updatePostPrice(post, calendar);
                }
            boolean b =   currentDate.compareTo(calendar.getEndDate()) >= 0;
                System.out.println(currentDate);
                System.out.println(b);

                if (currentDate.compareTo(calendar.getEndDate()) >= 0) {
                    post.setPrice(post.getCalendarStatus());
                    post.setCalendarStatus(0);
                    calendar.setUsed(1);
                    postDAO.save(post);
                }
            });
            if(calendar.getUsed()==1){
                calendarDAO.deleteById(calendar.getId());
            }
        });
    }

    private void updatePostPrice(Post post, Calendar calendar) {

        byte changePricePercent = calendar.getChangePrice();

        double percentMultiplier = 1 + (changePricePercent / 100.0);

        if (calendar.getStatus().equals(CalendarStatus.PRICE_UP.name())) {
            double newPrice = post.getPrice() * percentMultiplier;
            post.setPrice((long) newPrice);
        } else if (calendar.getStatus().equals(CalendarStatus.PRICE_DOWN.name())) {
            double newPrice = post.getPrice() * (2 - percentMultiplier);
            post.setPrice((long) newPrice);
        }

        postDAO.save(post);
    }
}
