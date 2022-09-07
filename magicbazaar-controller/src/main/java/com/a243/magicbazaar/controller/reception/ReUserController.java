package com.a243.magicbazaar.controller.reception;

import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.service.reception.ReUserService;
import com.a243.magicbazaar.util.FileUploadUtil;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserAddressVo;
import com.a243.magicbazaar.view.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "reception/user")
public class ReUserController {
    private final ReUserService reUserService;

    @Autowired
    public ReUserController(ReUserService reUserService) {
        this.reUserService = reUserService;
    }

    @PostMapping(value = "checkusername")
    public <T> Result<T> checkUsername(String username) {
        return reUserService.checkUsername(username);
    }

    @PostMapping(value = "registercode")
    public <T> Result<T> registerCode(HttpServletRequest request, HttpServletResponse response, String phone) {
        return reUserService.registerCode(request, response, phone);
    }

    @PostMapping(value = "checkregistercode")
    public <T> Result<T> checkRegisterCode(HttpServletRequest request, HttpServletResponse response, String registerCode) {
        return reUserService.checkRegisterCode(request, response, registerCode);
    }

    @PostMapping(value = "register")
    public <T> Result<T> register(HttpServletRequest request, HttpServletResponse response, UserParam userParam) throws JsonProcessingException {
        return reUserService.register(request, response, userParam);
    }

    @PostMapping(value = "checkuser")
    public Result<UserVo> checkUser(String username, String password) {
        return reUserService.checkUser(username, password);
    }

    @PostMapping(value = "logincode")
    public <T> Result<T> loginCode(HttpServletRequest request, HttpServletResponse response, String phone) {
        return reUserService.loginCode(request, response, phone);
    }

    @PostMapping(value = "checklogincode")
    public <T> Result<T> checkLoginCode(HttpServletRequest request, HttpServletResponse response, String loginCode) {
        return reUserService.checkLoginCode(request, response, loginCode);
    }

    @PostMapping(value = "info")
    public Result<UserVo> info() {
        return reUserService.info();
    }

    @PostMapping(value = "addressinfo")
    public Result<List<UserAddressVo>> addressInfo() {
        return reUserService.addressInfo();
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(User user) {
        return reUserService.update(user);
    }

    @PostMapping(value = "checkpassword")
    public <T> Result<T> checkPassword(String password) {
        return reUserService.checkPassword(password);
    }

    @PostMapping(value = "changepassword")
    public <T> Result<T> changePassword(String password) {
        return reUserService.changePassword(password);
    }

    @PostMapping(value = "address")
    public Result<List<UserAddressVo>> address() {
        return reUserService.address();
    }

    @PostMapping(value = "defaultaddress")
    public <T> Result<T> defaultAddress(Long id) {
        return reUserService.defaultAddress(id);
    }

    @PostMapping(value = "updatephonecode")
    public <T> Result<T> updatePhoneCode(HttpServletResponse response, String phone) {
        return reUserService.updatePhoneCode(response, phone);
    }

    @PostMapping(value = "checkupdatephonecode")
    public <T> Result<T> checkUpdatePhoneCode(HttpServletRequest request, HttpServletResponse response, String code, String phone) {
        return reUserService.checkUpdatePhoneCode(request, response, code, phone);
    }

    @PostMapping(value = "reply")
    public <T> Result<T> reply(Long id, String context) {
        return reUserService.reply(id, context);
    }

    @PostMapping(value = "deleteaddress")
    public <T> Result<T> deleteAddress(Long id) {
        return reUserService.deleteAddress(id);
    }

    @PostMapping(value = "productreply")
    public <T> Result<T> productReply(Long commodityId, Long toUserId, String context) {
        return reUserService.productReply(commodityId, toUserId, context);
    }

    @PostMapping(value = "firstreply")
    public <T> Result<T> firstReply(Long commodityId, String context) {
        return reUserService.firstReply(commodityId, context);
    }

    @PostMapping(value = "upload")
    public <T> Result<T> upload(MultipartFile file) {
        String upload = FileUploadUtil.upload(file);
        return reUserService.upload(upload);
    }

    @PostMapping(value = "checklogin")
    public <T> Result<T> checkLogin() {
        return reUserService.checkLogin();
    }

    @PostMapping(value = "useraddressadd")
    public Integer userAddressAdd(String addressinfo,Integer childMenu,String phone){
        return reUserService.userAddressAdd(addressinfo,childMenu,phone);
    }
    @PostMapping(value = "useraddressupdate")
    public Integer userAddressUpdate(String addressinfo,Integer childMenu,String phone,Integer id){
        return reUserService.userAddressUpdate(addressinfo,childMenu,phone,id);
    }
    @PostMapping(value = "useraddressfindone")
    public List<Map<String,Object>> userAddressFindOne(Long id){
        return reUserService.userAddressFindOne(id);
    }
}
