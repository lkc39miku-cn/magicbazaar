package com.a243.magicbazaar.service.reception;

import cn.hutool.http.server.HttpServerRequest;
import com.a243.magicbazaar.dao.dto.StarsReviews;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityParam;
import com.a243.magicbazaar.view.param.OrderTypeParam;
import com.a243.magicbazaar.view.vo.CartVo;
import com.a243.magicbazaar.view.vo.CommodityCommentVo;
import com.a243.magicbazaar.view.vo.CommodityVo;
import com.a243.magicbazaar.view.vo.OrderTypeVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ReCommodityService {
    /**
     * 最热商品
     * @return
     */
    Result<List<CommodityVo>> trend();

    /**
     * 商品详情
     * @param id
     * @return
     */
    CommodityVo findCommodityById(Long id);

    /**
     * 添加购物车
     * @param commodityId
     * @param commodityNumber
     * @param sub
     * @param request
     * @param response
     * @param <T>
     * @return
     */
    <T> Result<T> addCart(Long commodityId, Long commodityNumber, Boolean sub, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException;

    /**
     * 购物车数量
     * @param request
     * @param response
     * @return
     */
    Result<String> cartNum(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException ;

    /**
     * 取出购物车
     * @param request
     * @param response
     * @return
     */
    Result<List<CartVo>> loadCart(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException ;

    /**
     * 删除购物车
     * @param commodityId
     * @param request
     * @param response
     * @param <T>
     * @return
     */
    <T> Result<T> deleteCart(Long commodityId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException;

    /**
     * 销售幅度最大
     * @return
     */
    Result<List<CommodityVo>> sale();

    /**
     * 查询
     * @param commodityParam
     * @return
     */
    Result<List<CommodityVo>> list(CommodityParam commodityParam);

    /**
     * 类型数量
     * @param orderTypeParam
     * @return
     */
    Result<List<OrderTypeVo>> typeNumber(OrderTypeParam orderTypeParam);

    /**
     * 评分列表
     * @param id
     * @return
     */
    Result<StarsReviews> stars(Long id);

    /**
     * 评论
     * @param id
     * @return
     */
    Result<List<CommodityCommentVo>> comment(Long id);

    /**
     * 新品
     * @return
     */
    Result<List<CommodityVo>> newCommodity();

    /**
     * 收藏
     * @param commodityId
     * @param <T>
     * @return
     */
    <T> Result<T> collet(Long commodityId);
}
