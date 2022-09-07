package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.CommodityDisputeCommentService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeCommentParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/commodity/dispute/comment")
public class CommodityDisputeCommentController {
    private final CommodityDisputeCommentService commodityDisputeCommentService;

    @Autowired
    public CommodityDisputeCommentController(CommodityDisputeCommentService commodityDisputeCommentService) {
        this.commodityDisputeCommentService = commodityDisputeCommentService;
    }

    @PostMapping(value = "info")
    public Result<List<CommodityDisputeCommentVo>> info(CommodityDisputeCommentParam commodityDisputeCommentParam) {
        return commodityDisputeCommentService.info(commodityDisputeCommentParam);
    }

    @PostMapping(value = "reply")
    public <T> Result<T> reply(CommodityDisputeCommentParam commodityDisputeCommentParam) {
        return commodityDisputeCommentService.reply(commodityDisputeCommentParam);
    }
}
