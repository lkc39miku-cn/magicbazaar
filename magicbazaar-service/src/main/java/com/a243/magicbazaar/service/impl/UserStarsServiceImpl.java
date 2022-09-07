package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.entity.TableData;
import com.a243.magicbazaar.dao.mapper.UserStarsMapper2;
import com.a243.magicbazaar.service.UserStarsService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("userStarsService")
public class UserStarsServiceImpl implements UserStarsService {
    @Resource
    UserStarsMapper2 userStarsDao2;
    @Override
    public List<Map<String, Object>> selectAll(String nickname,String order_number) {
        return userStarsDao2.selectAll(nickname,order_number);
    }

    @Override
    public TableData selectPages(Integer page, Integer limit, String nickname, String order_number) {
        int count = selectAll(nickname,order_number).size();//得到所有员工数据的数据条数
        List<Map<String, Object>> data = userStarsDao2.selectPages(page, limit,nickname,order_number);
        return new TableData(count,data);
    }

    @Override
    public <T> Result<T> delete(Integer id) {
        int delete = userStarsDao2.delete(id);
        if (delete > 0) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }
}
