package com.a243.magicbazaar.service.impl.reception;

import com.a243.magicbazaar.dao.entity.CommodityType;
import com.a243.magicbazaar.dao.mapper.CommodityTypeMapper;
import com.a243.magicbazaar.service.reception.ReCommodityTypeService;
import com.a243.magicbazaar.util.convert.impl.CommodityTypeConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityTypeParam;
import com.a243.magicbazaar.view.vo.CommodityTypeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReCommodityTypeServiceImpl implements ReCommodityTypeService {
    private final CommodityTypeMapper commodityTypeMapper;
    private final CommodityTypeConvert commodityTypeConvert;

    @Autowired
    public ReCommodityTypeServiceImpl(CommodityTypeMapper commodityTypeMapper, CommodityTypeConvert commodityTypeConvert) {
        this.commodityTypeMapper = commodityTypeMapper;
        this.commodityTypeConvert = commodityTypeConvert;
    }

    @Override
    public Result<List<CommodityTypeVo>> list(CommodityTypeParam commodityTypeParam) {
        return Result.ok(commodityTypeConvert.convert(commodityTypeMapper.selectList(new QueryWrapper<CommodityType>()
                .eq("parent_id", 7)
                .eq("publish_status", 1)
                .last(" limit 2"))));
    }
}
