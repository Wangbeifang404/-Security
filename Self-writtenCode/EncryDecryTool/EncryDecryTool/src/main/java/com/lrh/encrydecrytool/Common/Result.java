package com.lrh.encrydecrytool.Common;

import java.io.Serializable;
import java.util.Objects;

/**
 * 通用API返回结果封装类
 *
 * @param <T> 返回数据类型
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码，1表示成功，其他值表示失败
     */
    private Integer code;

    /**
     * 错误或提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    public Result() {
        // 默认构造函数
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功且无数据返回
     *
     * @param <T> 泛型类型
     * @return 结果对象
     */
    public static <T> Result<T> success() {
        return new Result<>(1, null, null);
    }

    /**
     * 成功且返回数据
     *
     * @param object 返回数据，不能为null
     * @param <T> 泛型类型
     * @return 结果对象
     * @throws NullPointerException 如果 object 为 null，建议调用无参 success 方法
     */
    public static <T> Result<T> success(T object) {
        Objects.requireNonNull(object, "成功返回的数据不能为空");
        return new Result<>(1, null, object);
    }

    /**
     * 失败，返回错误消息，code 默认为0（通用失败）
     *
     * @param msg 错误消息，不能为空
     * @param <T> 泛型类型
     * @return 结果对象
     * @throws IllegalArgumentException 如果 msg 为空或空字符串
     */
    public static <T> Result<T> error(String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            throw new IllegalArgumentException("错误消息不能为空");
        }
        return new Result<>(0, msg, null);
    }

    /**
     * 获取状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置状态码
     *
     * @param code 状态码，建议使用1成功，0或其他失败码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据
     */
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + (msg == null ? "" : msg) + '\'' +
                ", data=" + (data == null ? "null" : data.toString()) +
                '}';
    }
}
