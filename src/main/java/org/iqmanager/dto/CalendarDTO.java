package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.Calendar;

import java.util.List;
import java.util.stream.Collectors;

/** Календарь */

@Getter
@Setter
@NoArgsConstructor
public class CalendarDTO {

    /** id */
    private long id;

    /** Дата начала */
    long beginDate;

    /** Дата конца */
    long endDate;

    /** Изменение цены */
    byte changePrice;

    /** Статус */
    String status;

    public CalendarDTO(long id, long beginDate, long endDate, byte changePrice, String status) {
        this.id = id;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.changePrice = changePrice;
        this.status = status;
    }

    public static List<CalendarDTO> calendarToDTO(List<Calendar> calendars) {
        return calendars.stream().map(x ->
                new CalendarDTO(x.getId(),
                        x.getBeginDate().getEpochSecond(),
                        x.getEndDate().getEpochSecond(),
                        x.getChangePrice(),
                        x.getStatus().name())).collect(Collectors.toList());
    }
}
