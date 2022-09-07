package com.a243.magicbazaar.controller.reception;

import cn.hutool.http.server.HttpServerRequest;
import com.a243.magicbazaar.dao.dto.StarsReviews;
import com.a243.magicbazaar.service.reception.ReCommodityService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.UserThreadLocal;
import com.a243.magicbazaar.view.param.CommodityParam;
import com.a243.magicbazaar.view.param.OrderTypeParam;
import com.a243.magicbazaar.view.vo.CartVo;
import com.a243.magicbazaar.view.vo.CommodityCommentVo;
import com.a243.magicbazaar.view.vo.CommodityVo;
import com.a243.magicbazaar.view.vo.OrderTypeVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "reception/commodity")
public class ReCommodityController {
    private final ReCommodityService reCommodityService;

    @Autowired
    public ReCommodityController(ReCommodityService reCommodityService) {
        this.reCommodityService = reCommodityService;
    }

    @ResponseBody
    @PostMapping(value = "trend")
    public Result<List<CommodityVo>> trend() {
        return reCommodityService.trend();
    }

    @ResponseBody
    @PostMapping(value = "sale")
    public Result<List<CommodityVo>> sale() {
        return reCommodityService.sale();
    }

    @GetMapping(value = "{id}")
    public ModelAndView id(@PathVariable(value = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        CommodityVo commodityVo = reCommodityService.findCommodityById(id);
        modelAndView.setViewName("reception/product");
        modelAndView.addObject("commodity", commodityVo);
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(value = "addcart")
    public <T> Result<T> addCart(Long commodityId, Long commodityNumber, @RequestParam(defaultValue = "false") Boolean sub, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        return reCommodityService.addCart(commodityId, commodityNumber, sub, request, response);
    }

    @ResponseBody
    @PostMapping(value = "cartnum")
    public Result<String> cartNum(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        return reCommodityService.cartNum(request, response);
    }

    @ResponseBody
    @PostMapping(value = "loadcart")
    public Result<List<CartVo>> loadCart(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        return reCommodityService.loadCart(request, response);
    }

    @ResponseBody
    @PostMapping(value = "deletecart")
    public <T> Result<T> deleteCart(Long commodityId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        return reCommodityService.deleteCart(commodityId, request, response);
    }

    @ResponseBody
    @PostMapping(value = "list")
    public Result<List<CommodityVo>> list(CommodityParam commodityParam) {
        return reCommodityService.list(commodityParam);
    }

    @ResponseBody
    @PostMapping(value = "typenumber")
    public Result<List<OrderTypeVo>> typeNumber(OrderTypeParam orderTypeParam) {
        return reCommodityService.typeNumber(orderTypeParam);
    }

    @ResponseBody
    @PostMapping(value = "stars")
    public Result<StarsReviews> stars(Long id) {
        return reCommodityService.stars(id);
    }

    @ResponseBody
    @PostMapping(value = "comment")
    public Result<List<CommodityCommentVo>> comment(Long id) {
        return reCommodityService.comment(id);
    }

    @ResponseBody
    @PostMapping(value = "newcommodity")
    public Result<List<CommodityVo>> newCommodity() {
        return reCommodityService.newCommodity();
    }

    @ResponseBody
    @PostMapping(value = "collet")
    public <T> Result<T> collet(Long commodityId) {
        return reCommodityService.collet(commodityId);
    }
}
