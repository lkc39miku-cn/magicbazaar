package com.a243.magicbazaar.util.result;

import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.code.ReturnCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 结果集
 * 对于返回结果进行包装并返回视图层
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    /**
     * 序列化
     */
    @Serial
    private static final long serialVersionUID = 1030122660339750628L;

    /**
     * 判断请求是否成功
     */
    private Boolean success;

    /**
     * 返回的状态码
     */
    private Integer code;

    /**
     * 返回的状态信息
     */
    private String msg;

    /**
     * 返回的自定义的错误信息
     */
    private Integer count;

    /**
     * 返回时所带的数据
     */
    private T data;

    /**
     * 请求成功
     * 未带有数据的成功的返回请求
     *
     * @param <T> 泛型
     * @return 结果集
     */
    public static <T> Result<T> ok() {
        return new Result<T>()
                .setSuccess(true)
                .setCode(BusinessCode.SUCCESS.code())
                .setMsg(BusinessCode.SUCCESS.msg());
    }

    /**
     * 请求成功 带有成功请求信息的返回请求
     *
     * @param returnCode 错误码
     * @param <T>        泛型
     * @return 结果集
     */
    public static <T> Result<T> ok(ReturnCode returnCode) {
        return new Result<T>()
                .setSuccess(true)
                .setCode(returnCode.code())
                .setMsg(returnCode.msg());
    }

    /**
     * 请求成功
     * 带有数据的成功的返回请求
     *
     * @param body 返回数据
     * @param <T>  泛型
     * @return 结果集
     */
    public static <T> Result<T> ok(T body) {
        return new Result<T>()
                .setSuccess(true)
                .setCode(BusinessCode.SUCCESS.code())
                .setMsg(BusinessCode.SUCCESS.msg())
                .setData(body);
    }

    /**
     * layui数据表格接收返回结果
     *
     * @param count 返回数据量
     * @param body  返回数据
     * @param <T>   泛型
     * @return 结果集
     */
    public static <T> Result<T> ok(Integer count, T body) {
        return new Result<T>()
                .setSuccess(true)
                .setCode(BusinessCode.SUCCESS.code())
                .setMsg(BusinessCode.SUCCESS.msg())
                .setCount(count)
                .setData(body);
    }


    /**
     * 请求失败
     * 返回请求失败的状态码以及自定义错误信息
     *
     * @param msg 错误信息
     * @param <T> 泛型
     * @return 结果集
     */
    public static <T> Result<T> fail(String msg) {
        return new Result<T>()
                .setSuccess(false)
                .setCode(BusinessCode.ERROR.code())
                .setMsg(msg);
    }

    /**
     * 请求失败
     * 返回请求失败的状态码以及状态信息
     *
     * @param returnCode 错误码
     * @param <T>        泛型
     * @return 结果集
     */
    public static <T> Result<T> fail(ReturnCode returnCode) {
        return new Result<T>()
                .setSuccess(false)
                .setCode(returnCode.code())
                .setMsg(returnCode.msg());
    }

}