package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.entity.CommodityDisputeInfo;
import com.a243.magicbazaar.dao.mapper.CommodityDisputeInfoMapper;
import com.a243.magicbazaar.service.CommodityDisputeInfoService;
import com.a243.magicbazaar.util.convert.impl.CommodityDisputeInfoConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeInfoParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommodityDisputeInfoServiceImpl implements CommodityDisputeInfoService {

    private final CommodityDisputeInfoMapper commodityDisputeInfoMapper;
    private final CommodityDisputeInfoConvert commodityDisputeInfoConvert;

    @Autowired
    public CommodityDisputeInfoServiceImpl(CommodityDisputeInfoMapper commodityDisputeInfoMapper, CommodityDisputeInfoConvert commodityDisputeInfoConvert) {
        this.commodityDisputeInfoMapper = commodityDisputeInfoMapper;
        this.commodityDisputeInfoConvert = commodityDisputeInfoConvert;
    }

    @Override
    public Result<List<CommodityDisputeInfoVo>> list(CommodityDisputeInfoParam commodityDisputeInfoParam) {
        if (commodityDisputeInfoParam.getPageParam() != null) {
            IPage<CommodityDisputeInfo> iPage = commodityDisputeInfoMapper.selectPage(new Page<>(commodityDisputeInfoParam.getPageParam().getPage(), commodityDisputeInfoParam.getPageParam().getPageSize()), new QueryWrapper<CommodityDisputeInfo>()
            .eq("commodity_dispute_id", commodityDisputeInfoParam.getCommodityDisputeId()));

            return Result.ok(Math.toIntExact(iPage.getTotal()), commodityDisputeInfoConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(commodityDisputeInfoConvert.convert(commodityDisputeInfoMapper.selectList(new QueryWrapper<>())));
        }
    }
}
