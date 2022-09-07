package com.a243.magicbazaar.dao.dto;

import com.a243.magicbazaar.view.vo.UserStarsVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class StarsReviews {
    private String avgStars;
    private String sumNumber;
    private String five;
    private String four;
    private String three;
    private String two;
    private String one;
    private List<UserStarsVo> starsVoList;
}
