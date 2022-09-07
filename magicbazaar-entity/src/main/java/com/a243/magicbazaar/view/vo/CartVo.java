package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.dto.Cart;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CartVo extends Cart {

    @Serial
    private static final long serialVersionUID = 23416864551785754L;
    private UserVo userVo;
    private CommodityVo commodityVo;
    private Long commodityNumber;
}
