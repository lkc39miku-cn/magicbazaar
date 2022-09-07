package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.CommodityDispute;
import com.a243.magicbazaar.dao.entity.CommodityDisputeComment;
import com.a243.magicbazaar.dao.entity.CommodityDisputeInfo;
import com.a243.magicbazaar.dao.mapper.CommodityDisputeCommentMapper;
import com.a243.magicbazaar.dao.mapper.CommodityDisputeInfoMapper;
import com.a243.magicbazaar.dao.mapper.CommodityDisputeMapper;
import com.a243.magicbazaar.service.CommodityDisputeCommentService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.CommodityDisputeCommentConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.CommodityDisputeCommentParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeCommentVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommodityDisputeCommentServiceImpl implements CommodityDisputeCommentService {
    private final CommodityDisputeCommentMapper commodityDisputeCommentMapper;
    private final CommodityDisputeCommentConvert commodityDisputeCommentConvert;
    private CommodityDisputeMapper commodityDisputeMapper;
    private CommodityDisputeInfoMapper commodityDisputeInfoMapper;

    @Autowired
    public CommodityDisputeCommentServiceImpl(CommodityDisputeCommentMapper commodityDisputeCommentMapper, CommodityDisputeCommentConvert commodityDisputeCommentConvert) {
        this.commodityDisputeCommentMapper = commodityDisputeCommentMapper;
        this.commodityDisputeCommentConvert = commodityDisputeCommentConvert;
    }

    @Autowired
    public void setCommodityDisputeMapper(CommodityDisputeMapper commodityDisputeMapper) {
        this.commodityDisputeMapper = commodityDisputeMapper;
    }

    @Autowired
    public void setCommodityDisputeInfoMapper(CommodityDisputeInfoMapper commodityDisputeInfoMapper) {
        this.commodityDisputeInfoMapper = commodityDisputeInfoMapper;
    }

    @Override
    public Result<List<CommodityDisputeCommentVo>> info(CommodityDisputeCommentParam commodityDisputeCommentParam) {
        List<CommodityDisputeComment> commodityDisputeCommentList = commodityDisputeCommentMapper.selectList(new QueryWrapper<CommodityDisputeComment>()
                .eq(!StrUtil.isBlank(commodityDisputeCommentParam.getCommodityDisputeInfoId()), "commodity_dispute_info_id", commodityDisputeCommentParam.getCommodityDisputeInfoId()));

        return Result.ok(commodityDisputeCommentConvert.convert(commodityDisputeCommentList));
    }

    @Override
    public <T> Result<T> reply(CommodityDisputeCommentParam commodityDisputeCommentParam) {
        CommodityDisputeComment commodityDisputeComment = commodityDisputeCommentMapper.selectOne(new QueryWrapper<CommodityDisputeComment>().eq("commodity_dispute_info_id", commodityDisputeCommentParam.getId()).orderByDesc("create_time").last("limit 1"));
        // 回复成功
        int i = commodityDisputeCommentMapper.updateById(commodityDisputeComment.setReplyContext(commodityDisputeCommentParam.getReplyContext()).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            // 子订单修改为已回复
            i = commodityDisputeInfoMapper.updateById(new CommodityDisputeInfo().setId(Long.parseLong(commodityDisputeCommentParam.getId())).setDisputeStatus(1));
            if (i == 1) {
                return Result.ok(BusinessCode.UPDATE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }
}
