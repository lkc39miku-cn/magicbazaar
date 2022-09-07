package com.a243.magicbazaar.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "mb_commodity")
public class Commodity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5151598017673920123L;
    private Long id;
    private String title;
    private String subTitle;
    private BigDecimal price;
    private Long commodityNumber;
    private String description;
    private String photo;
    private String fullPhoto;
    private Long commodityBrandId;
    private Long commodityTypeId;
    private Integer commodityStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate previewEndTime;
    private String previewInfo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleStartTime;
    private BigDecimal promotionPrice;
    private Integer promotionType;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime promotionStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime promotionEndTime;
    private Integer publishStatus;
    private Integer deleteStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
