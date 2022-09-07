package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.CommodityComment;
import com.a243.magicbazaar.dao.entity.TableData;

import java.util.List;
import java.util.Map;

public interface CommodityCommentService {
    List<Map<String,Object>> selectAll(CommodityComment comment);
    TableData selectPage(Integer page, Integer limit,CommodityComment comment);
    int update(Integer pid,Long id);
    int delete(Integer did,Long id);
}
