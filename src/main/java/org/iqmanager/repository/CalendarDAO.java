package org.iqmanager.repository;

import org.iqmanager.models.Calendar;
import org.iqmanager.models.PerformerData;
import org.iqmanager.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.time.Instant;
import java.util.List;

@RepositoryRestController
public interface CalendarDAO extends JpaRepository<Calendar, Long> {
    List<Calendar> getAllByPerformer(PerformerData performerData);
    List<Calendar> findCalendarByPostOrPerformer(Post post, PerformerData performerData);

    List<Calendar> findByBeginDateBeforeAndEndDateAfter(Instant beginDate,  Instant endDate );

}
