package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.TableData;
import com.a243.magicbazaar.dao.entity.UserStars;
import com.a243.magicbazaar.util.result.Result;

import java.util.List;
import java.util.Map;

public interface UserStarsService {
    List<Map<String,Object>> selectAll(String nickname,String order_number);
    TableData selectPages(Integer page, Integer limit, String nickname, String order_number);
    <T> Result<T> delete(Integer id);
}
