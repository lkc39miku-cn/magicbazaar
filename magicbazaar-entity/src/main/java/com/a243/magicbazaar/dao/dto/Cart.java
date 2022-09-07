package com.a243.magicbazaar.dao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = -4111539703945449571L;
    private Long userId;
    private Long commodityId;
    private Long commodityNumber;
}
