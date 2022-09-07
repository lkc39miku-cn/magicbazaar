package com.a243.magicbazaar.service.impl.reception;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerRequest;
import com.a243.magicbazaar.dao.dto.Cart;
import com.a243.magicbazaar.dao.dto.StarsReviews;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.reception.ReCommodityService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.*;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.UserThreadLocal;
import com.a243.magicbazaar.view.param.CommodityParam;
import com.a243.magicbazaar.view.param.OrderTypeParam;
import com.a243.magicbazaar.view.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReCommodityServiceImpl implements ReCommodityService {
    private final CommodityMapper commodityMapper;
    private final CommodityConvert commodityConvert;
    private final CartMapper cartMapper;
    private final CartConvert cartConvert;

    @Autowired
    public ReCommodityServiceImpl(CommodityMapper commodityMapper, CommodityConvert commodityConvert, CartMapper cartMapper, CartConvert cartConvert) {
        this.commodityMapper = commodityMapper;
        this.commodityConvert = commodityConvert;
        this.cartMapper = cartMapper;
        this.cartConvert = cartConvert;
    }

    private OrderInfoMapper orderInfoMapper;
    private CommodityVerifyMapper commodityVerifyMapper;
    private OrderTypeMapper orderTypeMapper;
    private OrderTypeConvert orderTypeConvert;
    private UserStarsMapper userStarsMapper;
    private UserStarsConvert userStarsConvert;
    private CommodityCommentConvert commodityCommentConvert;
    private UserColletMapper userColletMapper;

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setCommodityVerifyMapper(CommodityVerifyMapper commodityVerifyMapper) {
        this.commodityVerifyMapper = commodityVerifyMapper;
    }

    @Autowired
    public void setOrderTypeMapper(OrderTypeMapper orderTypeMapper) {
        this.orderTypeMapper = orderTypeMapper;
    }

    @Autowired
    public void setOrderTypeConvert(OrderTypeConvert orderTypeConvert) {
        this.orderTypeConvert = orderTypeConvert;
    }


    @Autowired
    public void setUserStarsMapper(UserStarsMapper userStarsMapper) {
        this.userStarsMapper = userStarsMapper;
    }

    @Autowired
    public void setUserStarsConvert(UserStarsConvert userStarsConvert) {
        this.userStarsConvert = userStarsConvert;
    }

    @Autowired
    public void setCommodityCommentConvert(CommodityCommentConvert commodityCommentConvert) {
        this.commodityCommentConvert = commodityCommentConvert;
    }

    @Autowired
    public void setUserColletMapper(UserColletMapper userColletMapper) {
        this.userColletMapper = userColletMapper;
    }

    @Override
    public Result<List<CommodityVo>> trend() {

        List<Long> verifyStatus = commodityVerifyMapper.selectList(new QueryWrapper<CommodityVerify>().eq("verify_status", 1)).stream().map(CommodityVerify::getCommodityId).collect(Collectors.toList());

        List<Long> collect = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().groupBy("commodity_id").orderByDesc("commodity_id").last(" limit 8")).stream().map(OrderInfo::getCommodityId).collect(Collectors.toList());

        List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>()
                .eq("publish_status", 1)
                .eq("commodity_status", 1)
                .eq("delete_status", 0)
                .eq("promotion_type", 0)
                .in("id", verifyStatus)
//                .in("id", collect)
                .gt("commodity_number", 0)
                .last(" limit 8"));
        return Result.ok(commodityConvert.convert(commodityList));
    }

    @Override
    public Result<List<CommodityVo>> sale() {
        List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>()
                .eq("publish_status", 1)
                .eq("commodity_status", 1)
                .eq("delete_status", 0)
                .eq("promotion_type", 1)
                .gt("commodity_number", 0)
                .orderByDesc("price / promotion_price")
                .last(" limit 8"));

        return Result.ok(commodityConvert.convert(commodityList));
    }

    @Override
    public CommodityVo findCommodityById(Long id) {
        Commodity commodity = commodityMapper.selectById(id);
        return commodityConvert.convert(commodity);
    }

    @Override
    public <T> Result<T> addCart(Long commodityId, Long commodityNumber, Boolean sub, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        // 未登录
        if (UserThreadLocal.get() == null) {
            // 购物车
            List<Cart> carts = new ArrayList<>();
            // cookie保存转换
            ObjectMapper objectMapper = new ObjectMapper();
            // 检查是否有购物车
            boolean checkCart = false;

            // 循环cookie
            Cookie[] cookies = request.getCookies();
            if (cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("cart")) {
                        carts = objectMapper.readValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8), new TypeReference<>() {});
                        checkCart = true;
                    }
                }
            }

            // 如果有购物车
            if (checkCart) {
                if (commodityNumber != null) {
                    // 有数量 +数量

                    // 判断购物车中是否存在原有商品
                    if (carts.stream().map(Cart::getCommodityId).collect(Collectors.toList()).contains(commodityId)) {
                        // 存在原有商品
                        for (Cart cart : carts) {
                            if (cart.getCommodityId().equals(commodityId)) {
                                cart.setCommodityNumber(cart.getCommodityNumber() + commodityNumber);
                                break;
                            }
                        }
                    } else {
                        // 不存在 添加新商品 数量 commodityNumber
                        carts.add(new Cart().setCommodityId(commodityId).setCommodityNumber(commodityNumber));
                    }
                } else {
                    // 如果没数量 则 +1 -1

                    // 判断购物车中是否存在原有商品
                    if (carts.stream().map(Cart::getCommodityId).collect(Collectors.toList()).contains(commodityId)) {
                        // 存在原有商品
                        for (Cart cart : carts) {

                            // 判断加减
                            if (cart.getCommodityId().equals(commodityId)) {
                                if (!sub) {
                                    cart.setCommodityNumber(cart.getCommodityNumber() + 1);
                                } else {
                                    cart.setCommodityNumber(cart.getCommodityNumber() - 1);
                                }
                                break;
                            }
                        }
                    } else {
                        // 不存在 添加新商品 数量1
                        carts.add(new Cart().setCommodityId(commodityId).setCommodityNumber(1L));
                    }
                }

                // 存入cookie
                Cookie cookie = new Cookie("cart", URLEncoder.encode(objectMapper.writeValueAsString(carts), StandardCharsets.UTF_8));
                cookie.setMaxAge(60 * 60 * 24 * 7);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            // 如果没有购物车
            if (!checkCart) {
                carts.add(new Cart().setCommodityId(commodityId).setCommodityNumber(Objects.requireNonNullElse(commodityNumber, 1L)));
                Cookie cookie = new Cookie("cart", URLEncoder.encode(objectMapper.writeValueAsString(carts), StandardCharsets.UTF_8));
                cookie.setMaxAge(60 * 60 * 24 * 7);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

        } else {
            // 已登录用户

            User user = UserThreadLocal.get();

            // 判断购物车中是否存在原有商品
            Cart selectOne = cartMapper.selectOne(new QueryWrapper<Cart>()
                    .eq("user_id", user.getId())
                    .eq("commodity_id", commodityId));

            if (commodityNumber != null) {
                // 有数量 +数量
                // 存在原有商品
                if (selectOne != null) {
                    cartMapper.update(selectOne.setCommodityNumber(selectOne.getCommodityNumber() + commodityNumber), new QueryWrapper<Cart>()
                            .eq("user_id", user.getId())
                            .eq("commodity_id", commodityId));
                } else {
                    // 不存在 添加新商品 数量 commodityNumber
                    cartMapper.insert(new Cart().setUserId(user.getId()).setCommodityId(commodityId).setCommodityNumber(commodityNumber));
                }

            } else {
                // 如果没数量 则 +1 -1
                // 存在原有商品
                if (selectOne != null) {
                    if (!sub) {
                        cartMapper.update(selectOne.setCommodityNumber(selectOne.getCommodityNumber() + 1), new QueryWrapper<Cart>()
                                .eq("user_id", user.getId())
                                .eq("commodity_id", commodityId));
                    } else {
                        cartMapper.update(selectOne.setCommodityNumber(selectOne.getCommodityNumber() - 1), new QueryWrapper<Cart>()
                                .eq("user_id", user.getId())
                                .eq("commodity_id", commodityId));
                    }
                } else {
                    // 不存在 添加新商品 数量 commodityNumber
                    cartMapper.insert(new Cart().setUserId(user.getId()).setCommodityId(commodityId).setCommodityNumber(1L));
                }
            }
        }
        return Result.ok(BusinessCode.ADD_SUCCESS);
    }

    @Override
    public Result<String> cartNum(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if (UserThreadLocal.get() == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Cookie[] cookies = request.getCookies();

            List<Cart> carts = new ArrayList<>();
            if (cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("cart")) {
                        carts = objectMapper.readValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8), new TypeReference<>() {});
                        break;
                    }
                }
            }
            return Result.ok(String.valueOf(carts.size()));
        } else {
            Long user_id = cartMapper.selectCount(new QueryWrapper<Cart>().eq("user_id", UserThreadLocal.get().getId()));
            return Result.ok(user_id.toString());
        }
    }

    @Override
    public Result<List<CartVo>> loadCart(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if (UserThreadLocal.get() == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Cookie[] cookies = request.getCookies();

            List<Cart> carts = new ArrayList<>();
            if (cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("cart")) {
                        carts = objectMapper.readValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8), new TypeReference<>() {});
                        break;
                    }
                }
            }

            List<Cart> cartList = new ArrayList<>();
            for (Cart cart : carts) {
                Commodity commodity = commodityMapper.selectById(cart.getCommodityId());
                if (commodity.getPublishStatus() == 1) {
                    if (commodity.getDeleteStatus() == 0) {
                        cartList.add(cart);
                    }
                }
            }
            return Result.ok(cartConvert.convert(carts));
        } else {
            List<Cart> carts = cartMapper.selectList(new QueryWrapper<Cart>().eq("user_id", UserThreadLocal.get().getId()));

            List<Cart> cartList = new ArrayList<>();
            for (Cart cart : carts) {
                Commodity commodity = commodityMapper.selectById(cart.getCommodityId());
                if (commodity.getPublishStatus() == 1) {
                    if (commodity.getDeleteStatus() == 0) {
                        cartList.add(cart);
                    }
                }
            }

            return Result.ok(cartConvert.convert(cartList));
        }
    }

    @Override
    public <T> Result<T> deleteCart(Long commodityId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if (UserThreadLocal.get() == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Cookie[] cookies = request.getCookies();

            List<Cart> carts = new ArrayList<>();
            if (cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("cart")) {
                        carts = objectMapper.readValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8), new TypeReference<>() {});
                        break;
                    }
                }
            }

            Iterator<Cart> iterator = carts.listIterator();
            while (iterator.hasNext()) {
                Cart cart = iterator.next();
                if (cart.getCommodityId().equals(commodityId)) {
                    iterator.remove();
                    break;
                }
            }

            Cookie cookie = new Cookie("cart", URLEncoder.encode(objectMapper.writeValueAsString(carts), StandardCharsets.UTF_8));
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath("/");
            response.addCookie(cookie);
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {

            int i = cartMapper.delete(new QueryWrapper<Cart>().eq("user_id", UserThreadLocal.get().getId()).eq("commodity_id", commodityId));
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        }
    }

    @Override
    public Result<List<CommodityVo>> list(CommodityParam commodityParam) {
        IPage<Commodity> iPage = commodityMapper.selectPage(new Page<>(commodityParam.getPage(), commodityParam.getLimit()), new QueryWrapper<Commodity>()
                .eq(!StrUtil.isBlank(commodityParam.getCommodityTypeId()), "commodity_type_id", commodityParam.getCommodityTypeId())
                .eq(!StrUtil.isBlank(commodityParam.getCommodityBrandId()), "commodity_brand_id", commodityParam.getCommodityBrandId())
                .between(!StrUtil.isBlank(commodityParam.getMinPrice()) && !StrUtil.isBlank(commodityParam.getMaxPrice()), "price", commodityParam.getMinPrice(), commodityParam.getMaxPrice())
                .orderByDesc(!StrUtil.isBlank(commodityParam.getOrderByDesc()), commodityParam.getOrderByDesc())
                .like(!StrUtil.isBlank(commodityParam.getTitle()), "title", commodityParam.getTitle())
                .eq("publish_status", 1)
                .eq("delete_status", 0));

        return Result.ok(Math.toIntExact(iPage.getTotal()), commodityConvert.convert(iPage.getRecords()));
    }

    @Override
    public Result<List<OrderTypeVo>> typeNumber(OrderTypeParam orderTypeParam) {
        return Result.ok(orderTypeConvert.convert(orderTypeMapper.selectList(new QueryWrapper<OrderType>()
        .in("id", 8, 9, 10))));
    }

    @Override
    public Result<StarsReviews> stars(Long id) {
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("commodity_id", id));
        if (orderInfoList.size() == 0) {
            return Result.ok();
        }

        List<UserStars> order_info_id = userStarsMapper.selectList(new QueryWrapper<UserStars>().in("order_info_id", orderInfoList.stream().map(OrderInfo::getId).collect(Collectors.toList())));

        List<UserStarsVo> userStarsVos = userStarsConvert.convert(order_info_id);

        StarsReviews starsReviews = new StarsReviews();
        starsReviews.setStarsVoList(userStarsVos);

        BigDecimal i = new BigDecimal(0);
        BigDecimal all = new BigDecimal(0);
        int five = 0;
        int four = 0;
        int three = 0;
        int two = 0;
        int one = 0;
        for (UserStarsVo userStarsVo : userStarsVos) {
            i = i.add(new BigDecimal(1));
            all = all.add(userStarsVo.getStars());
            if (Math.ceil(userStarsVo.getStars().doubleValue()) == 5) {
                five += 1;
            }
            if (Math.ceil(userStarsVo.getStars().doubleValue()) == 4) {
                four += 1;
            }
            if (Math.ceil(userStarsVo.getStars().doubleValue()) == 3) {
                three += 1;
            }
            if (Math.ceil(userStarsVo.getStars().doubleValue()) == 2) {
                two += 1;
            }
            if (Math.ceil(userStarsVo.getStars().doubleValue()) == 1) {
                one += 1;
            }
        }

        starsReviews.setAvgStars(String.valueOf(all.divide(i, 1, RoundingMode.HALF_UP)));
        starsReviews.setSumNumber(String.valueOf(i));
        starsReviews.setOne(String.valueOf(one));
        starsReviews.setTwo(String.valueOf(two));
        starsReviews.setThree(String.valueOf(three));
        starsReviews.setFour(String.valueOf(four));
        starsReviews.setFive(String.valueOf(five));

        return Result.ok(starsReviews);
    }

    @Override
    public Result<List<CommodityCommentVo>> comment(Long id) {
        return Result.ok(commodityCommentConvert.childChange(id));
    }

    @Override
    public Result<List<CommodityVo>> newCommodity() {
        List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>()
                .eq("publish_status", 1)
                .eq("commodity_status", 1)
                .eq("delete_status", 0)
                .gt("commodity_number", 0)
                .orderByDesc("create_time")
                .last(" limit 8"));

        return Result.ok(commodityConvert.convert(commodityList));
    }

    @Override
    public <T> Result<T> collet(Long commodityId) {
        User user = UserThreadLocal.get();
        UserCollet collet = userColletMapper.selectOne(new QueryWrapper<UserCollet>().eq("user_id", user.getId())
                .eq("commodity_id", commodityId));

        if (collet != null) {
            return Result.fail("你已收藏该物品");
        }

        int i = userColletMapper.insert(new UserCollet()
                .setUserId(UserThreadLocal.get().getId())
                .setCommodityId(commodityId));

        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }
}
