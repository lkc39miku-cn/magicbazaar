package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.StoreHouse;
import com.a243.magicbazaar.dao.entity.StoreHouseOut;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.StoreHouseMapper;
import com.a243.magicbazaar.dao.mapper.StoreHouseOutMapper;
import com.a243.magicbazaar.service.StoreHouseService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.StoreHouseConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.StoreHouseParam;
import com.a243.magicbazaar.view.vo.StoreHouseVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StoreHouseServiceImpl implements StoreHouseService {
    private final StoreHouseMapper storeHouseMapper;
    private final StoreHouseConvert storeHouseConvert;
    private CommodityMapper commodityMapper;
    private StoreHouseOutMapper storeHouseOutMapper;

    @Autowired
    public StoreHouseServiceImpl(StoreHouseMapper storeHouseMapper, StoreHouseConvert storeHouseConvert) {
        this.storeHouseMapper = storeHouseMapper;
        this.storeHouseConvert = storeHouseConvert;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setStoreHouseOutMapper(StoreHouseOutMapper storeHouseOutMapper) {
        this.storeHouseOutMapper = storeHouseOutMapper;
    }

    @Override
    public Result<List<StoreHouseVo>> list(StoreHouseParam storeHouseParam) {
        if (storeHouseParam.getPageParam() != null) {
            List<Long> commodityId = new ArrayList<>();
            if (!StrUtil.isBlank(storeHouseParam.getCommodityName())) {
                commodityId = commodityMapper.selectList(new QueryWrapper<Commodity>().like("title", storeHouseParam.getCommodityName())).stream().map(Commodity::getId).collect(Collectors.toList());
            }

            IPage<StoreHouse> iPage = storeHouseMapper.selectPage(new Page<>(storeHouseParam.getPageParam().getPage(), storeHouseParam.getPageParam().getPageSize()), new QueryWrapper<StoreHouse>()
                    .eq(!StrUtil.isBlank(storeHouseParam.getDeleteStatus()), "delete_status", storeHouseParam.getDeleteStatus())
                    .in(commodityId.size() != 0, "commodity_id", commodityId));

            if (!StrUtil.isBlank(storeHouseParam.getCommodityName()) && commodityId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), storeHouseConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(storeHouseConvert.convert(storeHouseMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> out(StoreHouseParam storeHouseParam) {
        StoreHouse storeHouse = storeHouseMapper.selectById(storeHouseParam.getId());
        if (storeHouse.getStock() >= Long.parseLong(storeHouseParam.getNum())) {
            int i = storeHouseOutMapper.insert(new StoreHouseOut().setStoreHouseId(storeHouse.getId()).setOutNumber(Long.parseLong(storeHouseParam.getNum())).setStoreHouseOutStatus(0).setDeleteStatus(0).setTargetId(StaffThreadLocal.get().getId()));
            if (i == 1) {
                i = storeHouseMapper.updateById(storeHouse.setStock(storeHouse.getStock() - Long.parseLong(storeHouseParam.getNum())));
                if (i == 1) {
                    return Result.ok(BusinessCode.UPDATE_SUCCESS);
                } else {
                    return Result.fail(BusinessCode.UPDATE_ERROR);
                }
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail("货物不足");
        }
    }

    @Override
    public <T> Result<T> update(StoreHouse storeHouse) {
        int i = storeHouseMapper.updateById(storeHouse);
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(StoreHouseParam storeHouseParam) {
        // 如果有商品存在 无法删除
        Long commodityId = storeHouseMapper.selectById(storeHouseParam.getId()).getCommodityId();
        Commodity commodity = commodityMapper.selectById(commodityId);
        if (commodity != null) {
            return Result.fail("该仓库下还存在商品，无法删除");
        } else {
            int i = storeHouseMapper.updateById(new StoreHouse().setId(Long.parseLong(storeHouseParam.getId())).setDeleteStatus(1));
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        }
    }
}
