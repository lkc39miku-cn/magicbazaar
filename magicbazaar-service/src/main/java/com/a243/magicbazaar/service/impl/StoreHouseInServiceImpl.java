package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.StoreHouseInService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.StoreHouseInConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.StoreHouseInParam;
import com.a243.magicbazaar.view.vo.StoreHouseInVo;
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
public class StoreHouseInServiceImpl implements StoreHouseInService {
    private final StoreHouseInMapper storeHouseInMapper;
    private final StoreHouseInConvert storeHouseInConvert;
    private PurchaseMapper purchaseMapper;
    private CommodityMapper commodityMapper;
    private StaffMapper staffMapper;
    private StoreHouseMapper storeHouseMapper;

    @Autowired
    public StoreHouseInServiceImpl(StoreHouseInMapper storeHouseInMapper, StoreHouseInConvert storeHouseInConvert) {
        this.storeHouseInMapper = storeHouseInMapper;
        this.storeHouseInConvert = storeHouseInConvert;
    }

    @Autowired
    public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Autowired
    public void setStoreHouseMapper(StoreHouseMapper storeHouseMapper) {
        this.storeHouseMapper = storeHouseMapper;
    }

    @Override
    public Result<List<StoreHouseInVo>> list(StoreHouseInParam storeHouseInParam) {
        if (storeHouseInParam.getPageParam() != null) {
            List<Long> staffId = new ArrayList<>();
            List<Long> targetId = new ArrayList<>();
            List<Long> storeId = new ArrayList<>();
            if (!StrUtil.isBlank(storeHouseInParam.getCommodityName())) {
                List<Long> title = commodityMapper.selectList(new QueryWrapper<Commodity>().like("title", storeHouseInParam.getCommodityName())).stream().map(Commodity::getId).collect(Collectors.toList());
                if (title.size() != 0) {
                    storeId = purchaseMapper.selectList(new QueryWrapper<Purchase>().in("commodity_id", title)).stream().map(Purchase::getId).collect(Collectors.toList());
                }
            }
            if (!StrUtil.isBlank(storeHouseInParam.getStaffName())) {
                staffId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", storeHouseInParam.getStaffName())).stream().map(Staff::getId).collect(Collectors.toList());
            }
            if (!StrUtil.isBlank(storeHouseInParam.getTargetName())) {
                targetId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", storeHouseInParam.getTargetName())).stream().map(Staff::getId).collect(Collectors.toList());
            }

            IPage<StoreHouseIn> iPage = storeHouseInMapper.selectPage(new Page<>(storeHouseInParam.getPageParam().getPage(), storeHouseInParam.getPageParam().getPageSize()), new QueryWrapper<StoreHouseIn>()
                    .eq(!StrUtil.isBlank(storeHouseInParam.getStoreHouseInStatus()), "store_house_in_status", storeHouseInParam.getStoreHouseInStatus())
                    .eq(!StrUtil.isBlank(storeHouseInParam.getDeleteStatus()), "delete_status", storeHouseInParam.getDeleteStatus())
                    .in(storeId.size() != 0, "purchase_id", storeId)
                    .in(staffId.size() != 0, "staff_id", staffId)
                    .in(targetId.size() != 0, "target_id", targetId));

            if (!StrUtil.isBlank(storeHouseInParam.getCommodityName()) && storeId.size() == 0 || !StrUtil.isBlank(storeHouseInParam.getStaffName()) && staffId.size() == 0 || !StrUtil.isBlank(storeHouseInParam.getTargetName()) && targetId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), storeHouseInConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(storeHouseInConvert.convert(storeHouseInMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> update(StoreHouseIn storeHouseIn) {
        // 入库信息修改
        int i = storeHouseInMapper.updateById(storeHouseIn.setStaffId(StaffThreadLocal.get().getId()).setStoreHouseInStatus(1));
        if (i == 1) {
            // 仓库信息修改
            Long purchaseId = storeHouseInMapper.selectById(storeHouseIn.getId()).getPurchaseId();
            Long commodityId = purchaseMapper.selectById(purchaseId).getCommodityId();
            StoreHouse storeHouse = storeHouseMapper.selectOne(new QueryWrapper<StoreHouse>().eq("commodity_id", commodityId));
            i = storeHouseMapper.updateById(storeHouse.setStock(storeHouse.getStock() + storeHouseIn.getRealInNumber()));
            if (i == 1) {
                return Result.ok(BusinessCode.UPDATE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(StoreHouseInParam storeHouseInParam) {
        // 如果存在审核单 无法删除入库记录
        Long purchaseId = storeHouseInMapper.selectById(storeHouseInParam.getId()).getPurchaseId();
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if (purchase == null) {
            int i = storeHouseInMapper.updateById(new StoreHouseIn().setId(Long.parseLong(storeHouseInParam.getId())).setDeleteStatus(1));
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        } else {
            return Result.fail("该记录下还存在审核信息，无法删除");
        }
    }
}
