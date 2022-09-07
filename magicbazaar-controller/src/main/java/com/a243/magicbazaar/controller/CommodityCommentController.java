package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.CommodityComment;
import com.a243.magicbazaar.dao.entity.TableData;
import com.a243.magicbazaar.service.CommodityCommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("commodityComment")
public class CommodityCommentController {
@Resource
    private CommodityCommentService commodityCommentServiceImpl;
@RequestMapping("find")
@ResponseBody
    public TableData find( Integer page, Integer limit,CommodityComment comment){
    return commodityCommentServiceImpl.selectPage(page,limit,comment);
}

    @RequestMapping("update")
    @ResponseBody
    public Integer update(Integer pid,Long id){
    return commodityCommentServiceImpl.update(pid,id);
    }
    @RequestMapping("delete")
    @ResponseBody
    public Integer delete(Integer did,Long id){
    return commodityCommentServiceImpl.delete(did, id);
    }
}
