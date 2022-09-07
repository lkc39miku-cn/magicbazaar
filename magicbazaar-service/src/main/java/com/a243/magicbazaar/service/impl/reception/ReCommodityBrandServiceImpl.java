package com.a243.magicbazaar.service.impl.reception;

import com.a243.magicbazaar.dao.entity.CommodityBrand;
import com.a243.magicbazaar.dao.mapper.CommodityBrandMapper;
import com.a243.magicbazaar.service.reception.ReCommodityBrandService;
import com.a243.magicbazaar.util.convert.impl.CommodityBrandConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityBrandParam;
import com.a243.magicbazaar.view.vo.CommodityBrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReCommodityBrandServiceImpl implements ReCommodityBrandService {
    private final CommodityBrandMapper commodityBrandMapper;
    private final CommodityBrandConvert commodityBrandConvert;

    @Autowired
    public ReCommodityBrandServiceImpl(CommodityBrandMapper commodityBrandMapper, CommodityBrandConvert commodityBrandConvert) {
        this.commodityBrandMapper = commodityBrandMapper;
        this.commodityBrandConvert = commodityBrandConvert;
    }

    @Override
    public Result<List<CommodityBrandVo>> list(CommodityBrandParam commodityBrandParam) {
        return Result.ok(commodityBrandConvert.convert(commodityBrandMapper.selectList(new QueryWrapper<CommodityBrand>()
            .eq("publish_status", 1)
            .last(" limit 8"))));
    }
}
