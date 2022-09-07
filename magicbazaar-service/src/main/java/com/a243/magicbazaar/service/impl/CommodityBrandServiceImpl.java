package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.CommodityBrand;
import com.a243.magicbazaar.dao.mapper.CommodityBrandMapper;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.service.CommodityBrandService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.CommodityBrandConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityBrandParam;
import com.a243.magicbazaar.view.vo.CommodityBrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommodityBrandServiceImpl implements CommodityBrandService {
    private final CommodityBrandMapper commodityBrandMapper;
    private final CommodityBrandConvert commodityBrandConvert;
    private CommodityMapper commodityMapper;

    @Autowired
    public CommodityBrandServiceImpl(CommodityBrandMapper commodityBrandMapper,
                                     CommodityBrandConvert commodityBrandConvert) {
        this.commodityBrandMapper = commodityBrandMapper;
        this.commodityBrandConvert = commodityBrandConvert;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Override
    public Result<List<CommodityBrandVo>> list(CommodityBrandParam commodityBrandParam) {
        if (commodityBrandParam.getPageParam() != null) {
            IPage<CommodityBrand> iPage = commodityBrandMapper.selectPage(new Page<>(commodityBrandParam.getPageParam().getPage(), commodityBrandParam.getPageParam().getPageSize()), new QueryWrapper<CommodityBrand>()
                    .like(!StrUtil.isBlank(commodityBrandParam.getName()), "name", commodityBrandParam.getName())
                    .eq(!StrUtil.isBlank(commodityBrandParam.getPublishStatus()), "publish_status", commodityBrandParam.getPublishStatus()));
            return Result.ok(Math.toIntExact(iPage.getTotal()), commodityBrandConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(commodityBrandConvert.convert(commodityBrandMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> add(CommodityBrand commodityBrand) {
        int i = commodityBrandMapper.insert(commodityBrand.setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.ADD_SUCCESS);
        } else {
            return Result.fail(BusinessCode.ADD_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(CommodityBrand commodityBrand) {
        int i = commodityBrandMapper.updateById(commodityBrand);
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(CommodityBrandParam commodityBrandParam) {
        List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>().eq("commodity_brand_id", commodityBrandParam.getId()));
        if (commodityList.size() != 0) {
            return Result.fail("此商品品牌下还有商品存在，请先更换该品牌下的商品");
        }
        int i = commodityBrandMapper.deleteById(new CommodityBrand().setId(Long.parseLong(commodityBrandParam.getId())));
        if (i == 1) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> on(CommodityBrandParam commodityBrandParam) {
        int i = commodityBrandMapper.updateById(new CommodityBrand().setId(Long.parseLong(commodityBrandParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(CommodityBrandParam commodityBrandParam) {
        List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>().eq("commodity_brand_id", commodityBrandParam.getId()));
        if (commodityList.size() != 0) {
            return Result.fail("此商品品牌下还有商品存在，请先更换该品牌下的商品");
        }
        int i = commodityBrandMapper.updateById(new CommodityBrand().setId(Long.parseLong(commodityBrandParam.getId())).setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }
}
