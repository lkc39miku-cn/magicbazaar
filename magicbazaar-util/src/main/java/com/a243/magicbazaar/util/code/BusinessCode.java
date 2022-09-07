package com.a243.magicbazaar.util.code;

/**
 * 自定义编码
 * 对视图层进行返回状态码以及状态信息
 */
public enum BusinessCode implements ReturnCode {
    SUCCESS(0, "请求成功"),
    ERROR(1, "请求失败"),
    ADD_ERROR(101, "添加错误"),
    ADD_SUCCESS(1, "添加成功"),
    DELETE_ERROR(102, "删除错误"),
    DELETE_SUCCESS(2, "删除成功"),
    UPDATE_ERROR(103, "修改错误"),
    UPDATE_SUCCESS(3, "修改成功"),
    PARAM_ERROR(10001, "参数错误"),
    LOGIN_SUCCESS(1000, "登陆成功"),
    LOGOUT_SUCCESS(1001, "注销成功"),
    NOT_LOGIN_ERROR(10002, "用户未登录"),
    LOGIN_TIMEOUT_ERROR(10003, "用户登录超时"),
    USERNAME_OR_PASSWORD_ERROR(10004, "账号或者密码错误"),
    SYSTEM_ERROR(100001, "系统错误");

    /**
     * 业务编号
     */
    private final Integer code;

    /**
     * 业务信息
     */
    private final String msg;

    BusinessCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 编码
     * 获取返回状态码
     *
     * @return 状态码
     */
    @Override
    public Integer code() {
        return code;
    }

    /**
     * 信息
     * 获取返回展示信息
     *
     * @return 信息
     */
    @Override
    public String msg() {
        return msg;
    }
}