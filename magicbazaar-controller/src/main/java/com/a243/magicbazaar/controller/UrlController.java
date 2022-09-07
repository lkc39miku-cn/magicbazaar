package com.a243.magicbazaar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "url")
public class UrlController {

    @RequestMapping(value = "commodity")
    public String commodity() {
        return "backstage/commodity";
    }

    @RequestMapping(value = "commodity/type")
    public String commodityType() {
        return "backstage/commoditytype";
    }

    @RequestMapping(value = "commodity/brand")
    public String commodityBrand() {
        return "backstage/commoditybrand";
    }

    @RequestMapping(value = "commodity/verify")
    public String commodityVerify() {
        return "backstage/commodityverify";
    }

    @RequestMapping(value = "purchase")
    public String purchase() {
        return "backstage/purchase";
    }

    @RequestMapping(value = "purchase/verify")
    public String purchaseVerify() {
        return "backstage/purchaseverify";
    }

    @RequestMapping(value = "finance/verify")
    public String financeVerify() {
        return "backstage/financeverify";
    }

    @RequestMapping(value = "store/house/in")
    public String storeHouseIn() {
        return "backstage/storehousein";
    }

    @RequestMapping(value = "store/house")
    public String storeHouse() {
        return "backstage/storehouse";
    }

    @RequestMapping(value = "store/house/out")
    public String storeHouseOut() {
        return "backstage/storehouseout";
    }

    @RequestMapping(value = "user")
    public String user() {
        return "backstage/user";
    }

    @RequestMapping(value = "address")
    public String address() {
        return "backstage/address";
    }

    @RequestMapping(value = "staff")
    public String staff() {
        return "backstage/staff";
    }

    @RequestMapping(value = "role")
    public String role() {
        return "backstage/role";
    }

    @RequestMapping(value = "order/info")
    public String order() {
        return "backstage/order";
    }

    @RequestMapping(value = "order/type")
    public String orderType() {
        return "backstage/ordertype";
    }

    @RequestMapping(value = "commodity/dispute")
    public String commodityDispute() {
        return "backstage/commoditydispute";
    }

    @RequestMapping(value = "menu/permission")
    public String menuPermission() {
        return "backstage/menupermission";
    }

    @RequestMapping(value = "user/stars")
    public String userStars() {
        return "backstage/userstars";
    }

    @RequestMapping(value = "user/comment/status")
    public String userCommentStatus() {
        return "backstage/usercommentstatus";
    }

    @RequestMapping(value = "paypal")
    public String paypal() {
        return "backstage/paypal";
    }

    @RequestMapping(value = "commodity/comment")
    public String commodityComment() {
        return "backstage/commoditycomment";
    }

    @RequestMapping(value = "index")
    public String index() {
        return "backstage/index";
    }

    @RequestMapping(value = "404")
    public String notFoundPage() {
        return "backstage/404";
    }

    @RequestMapping(value = "nopage")
    public String noPage() {
        return "backstage/nopage";
    }

    @RequestMapping(value = "usersetting")
    public String userSetting() {
        return "backstage/usersetting";
    }

    @RequestMapping(value = "userpassword")
    public String userPassword() {
        return "backstage/userpassword";
    }

    @RequestMapping(value = "online")
    public String online() {
        return "backstage/message";
    }
}
