package org.iqmanager.repository;

import org.iqmanager.models.Calendar;
import org.iqmanager.models.PerformerData;
import org.iqmanager.models.Post;
import org.iqmanager.models.enum_models.CalendarStatus;
import org.iqmanager.models.enum_models.CalendarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.time.Instant;
import java.util.List;

@RepositoryRestController
public interface CalendarDAO extends JpaRepository<Calendar, Long> {
    List<Calendar> getAllByPerformer(PerformerData performerData);
    List<Calendar> findCalendarByPostOrPerformer(Post post, PerformerData performerData);
    List<Calendar> findCalendarsByPost(Post post);

    @Query("SELECT c FROM Calendar c WHERE c.post IS NULL AND c.performer = :performer")
    List<Calendar> findCalendarsByPostIsNullAndPerformer(@Param("performer") PerformerData performer);



    List<Calendar> findByBeginDateBeforeAndEndDateAfter(Instant beginDate,  Instant endDate );

}
