package org.iqmanager.service.calendarService;

import org.iqmanager.models.Calendar;
import org.iqmanager.models.PerformerData;

import java.util.List;

public interface CalendarService {
    List<Calendar> getCalendar(PerformerData performerData);
    List<Calendar> getCalendarByPosts(long id);

    List<Calendar> getCalendarByPost(Long postId);
}
