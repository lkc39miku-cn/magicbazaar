package com.a243.magicbazaar.service.impl.reception;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.dto.Cart;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.reception.ReUserService;
import com.a243.magicbazaar.util.PhoneUtil;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.UserAddressConvert;
import com.a243.magicbazaar.util.convert.impl.UserConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.UserThreadLocal;
import com.a243.magicbazaar.util.token.JwtUtil;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserAddressVo;
import com.a243.magicbazaar.view.vo.UserVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ReReUserServiceImpl implements ReUserService {
    private final UserMapper userMapper;
    private final UserConvert userConvert;
@Resource
    private IAddressDao addressImpl;

    @Autowired
    public ReReUserServiceImpl(UserMapper userMapper, UserConvert userConvert) {
        this.userMapper = userMapper;
        this.userConvert = userConvert;
    }

    private RedisTemplate<String, String> redisTemplate;
    private UserAddressMapper userAddressMapper;
    private UserAddressConvert userAddressConvert;
    private CommodityDisputeInfoMapper commodityDisputeInfoMapper;
    private CommodityDisputeCommentMapper commodityDisputeCommentMapper;
    private CommodityCommentMapper commodityCommentMapper;
    private UserCommentStatusMapper userCommentStatusMapper;
    private CartMapper cartMapper;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setUserAddressMapper(UserAddressMapper userAddressMapper) {
        this.userAddressMapper = userAddressMapper;
    }

    @Autowired
    public void setUserAddressConvert(UserAddressConvert userAddressConvert) {
        this.userAddressConvert = userAddressConvert;
    }

    @Autowired
    public void setCommodityDisputeInfoMapper(CommodityDisputeInfoMapper commodityDisputeInfoMapper) {
        this.commodityDisputeInfoMapper = commodityDisputeInfoMapper;
    }

    @Autowired
    public void setCommodityDisputeCommentMapper(CommodityDisputeCommentMapper commodityDisputeCommentMapper) {
        this.commodityDisputeCommentMapper = commodityDisputeCommentMapper;
    }

    @Autowired
    public void setCommodityCommentMapper(CommodityCommentMapper commodityCommentMapper) {
        this.commodityCommentMapper = commodityCommentMapper;
    }

    @Autowired
    public void setUserCommentStatusMapper(UserCommentStatusMapper userCommentStatusMapper) {
        this.userCommentStatusMapper = userCommentStatusMapper;
    }

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public <T> Result<T> register(HttpServletRequest request, HttpServletResponse response, UserParam userParam) throws JsonProcessingException {
        User user = new User();
        int i = userMapper.insert(user.setUsername(userParam.getUsername()).setNickname(userParam.getUsername())
                .setPassword(userParam.getPassword()).setPhone(userParam.getPhone()).setEmail(userParam.getEmail())
                .setPublishStatus(0).setDeleteStatus(0));
        if (i == 1) {
            userCommentStatusMapper.insert(new UserCommentStatus()
                .setUserId(user.getId())
                .setCommentStatus(0));

            // 购物车移动
            ObjectMapper objectMapper = new ObjectMapper();
            Cookie[] cookies = request.getCookies();

            List<Cart> carts = new ArrayList<>();
            if (cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("cart")) {
                        carts = objectMapper.readValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8), new TypeReference<>() {});
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                    }
                }
            }

            if (carts.size() > 0) {
                for (Cart cart : carts) {
                    cartMapper.insert(new Cart()
                        .setUserId(user.getId())
                        .setCommodityId(cart.getCommodityId())
                        .setCommodityNumber(cart.getCommodityNumber()));
                }
            }

            return Result.ok(BusinessCode.ADD_SUCCESS);
        } else {
            return Result.fail(BusinessCode.ADD_ERROR);
        }
    }

    @Override
    public <T> Result<T> registerCode(HttpServletRequest request, HttpServletResponse response, String phone) {
        PhoneUtil.registerPhoneCode(response, phone);
        return Result.ok();
    }

    @Override
    public User checkUser(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }

        Map<String, Object> map = JwtUtil.checkToken(token);
        if (map == null) {
            return null;
        }

        String userJson = redisTemplate.opsForValue().get("USERTOKEN_" + token);
        if (StrUtil.isBlank(userJson)) {
            return null;
        }

        return JSON.parseObject(userJson, User.class);
    }

    @Override
    public <T> Result<T> findUserByNameAndPassword(User user, String autoLogin, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .eq("username", user.getUsername())
                .eq("password", user.getPassword());
        User one = userMapper.selectOne(userQueryWrapper);

        if (one == null) {
            return Result.fail(BusinessCode.USERNAME_OR_PASSWORD_ERROR);
        }

        if (one.getPublishStatus() == 1) {
            return Result.fail("该用户已被禁用");
        }

        if (one.getDeleteStatus() == 1) {
            return Result.fail("该用户已被删除");
        }

        if (!StrUtil.isBlank(autoLogin)) {
            if ("on".equals(autoLogin)) {
                String token = JwtUtil.createToken(one.getId());
                redisTemplate.opsForValue().set("USERTOKEN_" + token, JSON.toJSONString(one), 1, TimeUnit.DAYS);

                Cookie cookie = new Cookie("userToken", token);
                cookie.setMaxAge(60 * 60 * 24);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        } else {
            String token = JwtUtil.createToken(one.getId());
            redisTemplate.opsForValue().set("USERTOKEN_" + token, JSON.toJSONString(one), 3, TimeUnit.HOURS);

            Cookie cookie = new Cookie("userToken", token);
            cookie.setMaxAge(60 * 60 * 3);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return Result.ok(BusinessCode.LOGIN_SUCCESS);
    }

    @Override
    public <T> Result<T> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        redisTemplate.delete("USERTOKEN_" + token);
        return Result.ok(BusinessCode.LOGOUT_SUCCESS);
    }

    @Override
    public <T> Result<T> checkUsername(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            return Result.ok();
        } else {
            return Result.fail("用户名重复");
        }
    }

    @Override
    public Result<UserVo> checkUser(String username, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username).eq("password", password));
        if (user != null) {
            return Result.ok(userConvert.convert(user));
        } else {
            return Result.fail("用户名或密码错误");
        }
    }

    @Override
    public <T> Result<T> loginCode(HttpServletRequest request, HttpServletResponse response, String phone) {
        PhoneUtil.loginPhoneCode(response, phone);
        return Result.ok();
    }

    @Override
    public <T> Result<T> checkLoginCode(HttpServletRequest request, HttpServletResponse response, String loginCode) {
        Cookie[] cookies = request.getCookies();

        String code = null;

        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("loginPhoneCode".equals(cookie.getName())) {
                    code = cookie.getValue();
                }
            }
        }

        if (StrUtil.isBlank(code)) {
            return Result.fail("验证码已过期");
        }

        if (!code.equals(loginCode)) {
            return Result.fail("手机验证码不正确");
        }

        for (Cookie cookie : cookies) {
            if ("loginPhoneCode".equals(cookie.getName())) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return Result.ok();
    }

    @Override
    public <T> Result<T> checkRegisterCode(HttpServletRequest request, HttpServletResponse response, String registerCode) {
        Cookie[] cookies = request.getCookies();

        String code = null;

        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("registerPhoneCode".equals(cookie.getName())) {
                    code = cookie.getValue();
                }
            }
        }

        if (StrUtil.isBlank(code)) {
            return Result.fail("验证码已过期");
        }

        if (!code.equals(registerCode)) {
            return Result.fail("手机验证码不正确");
        }

        for (Cookie cookie : cookies) {
            if ("registerPhoneCode".equals(cookie.getName())) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return Result.ok();
    }

    @Override
    public Result<UserVo> info() {
        return Result.ok(userConvert.convert(UserThreadLocal.get()));
    }

    @Override
    public Result<List<UserAddressVo>> addressInfo() {
        return Result.ok(userAddressConvert.convert(userAddressMapper.selectList(new QueryWrapper<UserAddress>().eq("user_id", UserThreadLocal.get().getId()))));
    }

    @Override
    public <T> Result<T> update(User user) {
        int i = userMapper.updateById(user.setId(UserThreadLocal.get().getId()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> checkPassword(String password) {
        User user = userMapper.selectById(UserThreadLocal.get().getId());
        if (password.equals(user.getPassword())) {
            return Result.ok();
        }
        return Result.fail("原密码输入错误");
    }

    @Override
    public <T> Result<T> changePassword(String password) {

        int i = userMapper.updateById(new User().setId(UserThreadLocal.get().getId()).setPassword(password));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<List<UserAddressVo>> address() {
        List<UserAddress> userAddresses = userAddressMapper.selectList(new QueryWrapper<UserAddress>().eq("user_id", UserThreadLocal.get().getId()));
        return Result.ok(userAddressConvert.convert(userAddresses));
    }


    @Override
    public <T> Result<T> defaultAddress(Long id) {
        UserAddress userAddress = userAddressMapper.selectById(id);

        if (userAddress.getAddressStatus() == 1) {
            return Result.ok();
        }

        List<UserAddress> user_id = userAddressMapper.selectList(new QueryWrapper<UserAddress>().eq("user_id", userAddress.getUserId()));

        for (UserAddress userAddressChild : user_id) {
            userAddressMapper.updateById(userAddressChild.setAddressStatus(0));
        }

        int i = userAddressMapper.updateById(userAddress.setAddressStatus(1));

        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> updatePhoneCode(HttpServletResponse response, String phone) {
        PhoneUtil.updatePhoneCode(response, phone);
        return Result.ok();
    }

    @Override
    public <T> Result<T> checkUpdatePhoneCode(HttpServletRequest request, HttpServletResponse response, String code, String phone) {
        Cookie[] cookies = request.getCookies();

        String codes = null;

        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("updatePhoneCode".equals(cookie.getName())) {
                    codes = cookie.getValue();
                }
            }
        }

        if (StrUtil.isBlank(codes)) {
            return Result.fail("验证码已过期");
        }

        if (!codes.equals(code)) {
            return Result.fail("手机验证码不正确");
        }

        for (Cookie cookie : cookies) {
            if ("updatePhoneCode".equals(cookie.getName())) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        userMapper.updateById(new User().setId(UserThreadLocal.get().getId()).setPhone(phone));

        return Result.ok();
    }

    @Override
    public <T> Result<T> reply(Long id, String context) {
        CommodityDisputeInfo commodityDisputeInfo = commodityDisputeInfoMapper.selectOne(new QueryWrapper<CommodityDisputeInfo>().eq("order_info_id", id));

        commodityDisputeInfoMapper.updateById(commodityDisputeInfo.setDisputeStatus(0));
        int i = commodityDisputeCommentMapper.insert(new CommodityDisputeComment()
                .setContext(context)
                .setUserId(UserThreadLocal.get().getId())
                .setCommodityDisputeInfoId(commodityDisputeInfo.getId()));

        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }


    @Override
    public <T> Result<T> deleteAddress(Long id) {
        int i = userAddressMapper.deleteById(id);
        if (i == 1) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> productReply(Long commodityId, Long toUserId, String context) {
        UserCommentStatus userCommentStatus = userCommentStatusMapper.selectOne(new QueryWrapper<UserCommentStatus>().eq("user_id", UserThreadLocal.get().getId()));

        if (userCommentStatus.getCommentStatus() == 1) {
            return Result.fail("你已被禁止评论，原因：" + userCommentStatus.getCommentInfo() + "，截止时间：" + userCommentStatus.getCommentEndTime());
        }

        int i = commodityCommentMapper.insert(new CommodityComment()
                .setCommodityId(commodityId)
                .setUserId(UserThreadLocal.get().getId())
                .setContext(context)
                .setToUserId(toUserId)
                .setPublishStatus(1)
                .setDeleteStatus(0)
                .setParentId(1L));

        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> firstReply(Long commodityId, String context) {
        UserCommentStatus userCommentStatus = userCommentStatusMapper.selectOne(new QueryWrapper<UserCommentStatus>().eq("user_id", UserThreadLocal.get().getId()));

        if (userCommentStatus.getCommentStatus() == 1) {
            return Result.fail("你已被禁止评论，原因：" + userCommentStatus.getCommentInfo() + "，截止时间：" + userCommentStatus.getCommentEndTime());
        }

        int i = commodityCommentMapper.insert(new CommodityComment()
                .setCommodityId(commodityId)
                .setUserId(UserThreadLocal.get().getId())
                .setContext(context)
                .setPublishStatus(1)
                .setDeleteStatus(0)
                .setParentId(0L));

        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> upload(String upload) {
        int i = userMapper.updateById(new User()
                .setId(UserThreadLocal.get().getId())
                .setAvatar(upload));

        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> checkLogin() {
        if (UserThreadLocal.get() != null) {
            return Result.ok();
        } else {
            return Result.fail(BusinessCode.NOT_LOGIN_ERROR);
        }
    }

    @Override
    public Integer userAddressAdd(String addressinfo, Integer childMenu, String phone) {
        Long id=UserThreadLocal.get().getId();
        return   addressImpl.userAddressAdd(addressinfo,childMenu,phone,id);

    }

    @Override
    public List<Map<String, Object>> userAddressFindOne(Long id) {
        return addressImpl.userAddressFindOne(id);
    }

    @Override
    public Integer userAddressUpdate(String addressinfo, Integer childMenu, String phone, Integer id) {
        return addressImpl.userAddressUpdate(addressinfo,childMenu,phone,id);
    }
}
