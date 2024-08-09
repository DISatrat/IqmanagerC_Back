package org.iqmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculateDTO {
    long price;

    long factor;

    long[] extras;
}
