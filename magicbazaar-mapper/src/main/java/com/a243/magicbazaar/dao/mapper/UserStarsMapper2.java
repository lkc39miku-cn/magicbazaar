package com.a243.magicbazaar.dao.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserStarsMapper2 {
    List<Map<String,Object>> selectAll(String nickname, String order_number);
    List<Map<String,Object>> selectPages(Integer page, Integer limit, String nickname, String order_number);
    int delete(Integer id);
}
