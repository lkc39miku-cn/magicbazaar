package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.entity.CommodityComment;
import com.a243.magicbazaar.dao.entity.TableData;
import com.a243.magicbazaar.dao.mapper.CommodityCommentDao;
import com.a243.magicbazaar.service.CommodityCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service("commodityCommentServiceImpl")
public class CommodityCommentServiceImpl implements CommodityCommentService {
    @Resource
    CommodityCommentDao commodityCommentImpl;
    @Override
    public List<Map<String, Object>> selectAll(CommodityComment comment)
    {
        return commodityCommentImpl.selectAll(comment);
    }

    @Override
    public TableData selectPage(Integer page, Integer limit,CommodityComment comment) {
        int count=commodityCommentImpl.selectAll(comment).size();
        List<Map<String,Object>> list=commodityCommentImpl.selectAll(comment);
        List<Map<String,Object>> data=commodityCommentImpl.selectPage(page,limit,comment);
//        if(data!=null){
//            for (Map m:data){
//               LocalDateTime dd= (LocalDateTime) m.get("delete_");
//            }
//        }
//        for (Map m:list){
//            if (data!=null){
//                data.replaceAll();
//            }
//        }
        return new TableData(count,data) ;
    }

    @Override
    public int update(Integer pid, Long id) {
        return commodityCommentImpl.update(pid, id);
    }

    @Override
    public int delete(Integer did, Long id) {
        return commodityCommentImpl.delete(did, id);
    }
}
