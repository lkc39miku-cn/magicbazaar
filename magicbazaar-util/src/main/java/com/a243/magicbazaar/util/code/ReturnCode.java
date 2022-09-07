package com.a243.magicbazaar.util.code;

/**
 * 自定义返回编码
 * 继承ReturnCode接口实现自定义返回编码
 */
public interface ReturnCode {
    /**
     * 编码
     * 返回时的状态码
     *
     * @return 状态码
     */
    Integer code();

    /**
     * 信息
     * 返回时所展示的状态信息
     *
     * @return 信息
     */
    String msg();
}