package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtraForOrderElementDTO {


    private String title;

    private String type;
    private long[] idRatesAndService;

}
