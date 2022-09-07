package com.a243.magicbazaar.dao.entity;

import java.util.List;

public class TableData {
    private Integer code;// 状态码，0成功
    private String msg;
    private Integer count; // 总条数
    private List data;//查询到的List<Map>结果集数据

    public TableData(List data) {
        this.code = data.size()>0 ? 0 : 1;
        if(code==0){
            this.msg = "";
        }else{
            this.msg = "没有查询数据";
        }
        this.count = data.size();
        this.data = data;
    }

    public TableData(Integer count, List data) {
        this.code = data.size()>0 ? 0 : 1;
        if(code==0){
            this.msg = "";
        }else{
            this.msg = "没有查询数据";
        }
        this.count = count;
        this.data = data;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
