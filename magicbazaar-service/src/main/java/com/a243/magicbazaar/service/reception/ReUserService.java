package com.a243.magicbazaar.service.reception;

import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserAddressVo;
import com.a243.magicbazaar.view.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ReUserService {
    /**
     * 注册
     *
     * @param request
     * @param response
     * @param userParam
     * @param <T>
     * @return
     */
    <T> Result<T> register(HttpServletRequest request, HttpServletResponse response, UserParam userParam) throws JsonProcessingException;

    /**
     * 验证码
     * @param request
     * @param response
     * @param phone
     * @param <T>
     * @return
     */
    <T> Result<T> registerCode(HttpServletRequest request, HttpServletResponse response, String phone);

    /**
     * 检车用户
     * @param token
     * @return
     */
    User checkUser(String token);

    /**
     * 寻找用户
     * @param user
     * @param autoLogin
     * @param request
     * @param response
     * @param <T>
     * @return
     */
    <T> Result<T> findUserByNameAndPassword(User user, String autoLogin, HttpServletRequest request, HttpServletResponse response);

    /**
     * 登出
     * @param request
     * @param <T>
     * @return
     */
    <T> Result<T> logout(HttpServletRequest request);

    /**
     * 重复用户名
     * @param username
     * @param <T>
     * @return
     */
    <T> Result<T> checkUsername(String username);

    /**
     * 检查用户
     * @param username
     * @param password
     * @return
     */
    Result<UserVo> checkUser(String username, String password);

    /**
     * 登录验证码
     * @param request
     * @param response
     * @param phone
     * @param <T>
     * @return
     */
    <T> Result<T> loginCode(HttpServletRequest request, HttpServletResponse response, String phone);

    /**
     * 检查验证码
     * @param request
     * @param response
     * @param loginCode
     * @param <T>
     * @return
     */
    <T> Result<T> checkLoginCode(HttpServletRequest request, HttpServletResponse response, String loginCode);

    /**
     * 检查验证码
     * @param request
     * @param response
     * @param registerCode
     * @param <T>
     * @return
     */
    <T> Result<T> checkRegisterCode(HttpServletRequest request, HttpServletResponse response, String registerCode);

    /**
     * 用户信息
     * @return
     */
    Result<UserVo> info();

    /**
     * 用户地址
     * @return
     */
    Result<List<UserAddressVo>> addressInfo();

    /**
     * 更新用户信息
     * @param user
     * @param <T>
     * @return
     */
    <T> Result<T> update(User user);

    /**
     * 检查密码
     * @param password
     * @param <T>
     * @return
     */
    <T> Result<T> checkPassword(String password);

    /**
     * 修改密码
     * @param password
     * @param <T>
     * @return
     */
    <T> Result<T> changePassword(String password);

    /**
     * 当前用户地址
     * @return
     */
    Result<List<UserAddressVo>> address();

    /**
     * 默认地址修改
     * @param id
     * @param <T>
     * @return
     */
    <T> Result<T> defaultAddress(Long id);

    /**
     * 修改手机号
     * @param phone
     * @param response
     * @param <T>
     * @return
     */
    <T> Result<T> updatePhoneCode(HttpServletResponse response, String phone);

    /**
     * 检查手机号验证码
     * @param request
     * @param response
     * @param code
     * @param phone
     * @param <T>
     * @return
     */
    <T> Result<T> checkUpdatePhoneCode(HttpServletRequest request, HttpServletResponse response, String code, String phone);

    /**
     * 回复
     * @param id
     * @param context
     * @param <T>
     * @return
     */
    <T> Result<T> reply(Long id, String context);

    /**
     * 删除地址
     * @param id
     * @param <T>
     * @return
     */
    <T> Result<T> deleteAddress(Long id);

    /**
     * 评论回复
     * @param commodityId
     * @param toUserId
     * @param context
     * @param <T>
     * @return
     */
    <T> Result<T> productReply(Long commodityId, Long toUserId, String context);

    /**
     * 评论
     * @param commodityId
     * @param context
     * @param <T>
     * @return
     */
    <T> Result<T> firstReply(Long commodityId, String context);

    /**
     * 更新头像
     * @param upload
     * @param <T>
     * @return
     */
    <T> Result<T> upload(String upload);

    /**
     * 检查用户登录状态
     * @param <T>
     * @return
     */
    <T> Result<T> checkLogin();
//添加用户地址信息
    Integer userAddressAdd(String addressinfo, Integer childMenu, String phone);
//查询用户地址（根据id）
    List<Map<String, Object>> userAddressFindOne(Long id);
//修改用户地址
    Integer userAddressUpdate(String addressinfo, Integer childMenu, String phone, Integer id);
}
