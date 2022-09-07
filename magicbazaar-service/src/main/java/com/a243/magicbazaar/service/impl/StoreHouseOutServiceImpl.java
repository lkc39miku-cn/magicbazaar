package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.entity.StoreHouse;
import com.a243.magicbazaar.dao.entity.StoreHouseOut;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.dao.mapper.StoreHouseMapper;
import com.a243.magicbazaar.dao.mapper.StoreHouseOutMapper;
import com.a243.magicbazaar.service.StoreHouseOutService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.StoreHouseOutConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.StoreHouseOutParam;
import com.a243.magicbazaar.view.vo.StoreHouseOutVo;
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
public class StoreHouseOutServiceImpl implements StoreHouseOutService {
    private final StoreHouseOutMapper storeHouseOutMapper;
    private final StoreHouseOutConvert storeHouseOutConvert;
    private StaffMapper staffMapper;
    private CommodityMapper commodityMapper;
    private StoreHouseMapper storeHouseMapper;

    @Autowired
    public StoreHouseOutServiceImpl(StoreHouseOutMapper storeHouseOutMapper, StoreHouseOutConvert storeHouseOutConvert) {
        this.storeHouseOutMapper = storeHouseOutMapper;
        this.storeHouseOutConvert = storeHouseOutConvert;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setStoreHouseMapper(StoreHouseMapper storeHouseMapper) {
        this.storeHouseMapper = storeHouseMapper;
    }

    @Override
    public Result<List<StoreHouseOutVo>> list(StoreHouseOutParam storeHouseOutParam) {
        if (storeHouseOutParam.getPageParam() != null) {
            List<Long> staffId = new ArrayList<>();
            List<Long> targetId = new ArrayList<>();
            List<Long> houseId = new ArrayList<>();
            if (!StrUtil.isBlank(storeHouseOutParam.getCommodityName())) {
                List<Long> title = commodityMapper.selectList(new QueryWrapper<Commodity>().like("title", storeHouseOutParam.getCommodityName())).stream().map(Commodity::getId).collect(Collectors.toList());
                if (title.size() != 0) {
                    houseId = storeHouseMapper.selectList(new QueryWrapper<StoreHouse>().in("commodity_id", title)).stream().map(StoreHouse::getId).collect(Collectors.toList());
                }
            }
            if (!StrUtil.isBlank(storeHouseOutParam.getStaffName())) {
                staffId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", storeHouseOutParam.getStaffName())).stream().map(Staff::getId).collect(Collectors.toList());
            }
            if (!StrUtil.isBlank(storeHouseOutParam.getTargetName())) {
                targetId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", storeHouseOutParam.getTargetName())).stream().map(Staff::getId).collect(Collectors.toList());
            }

            IPage<StoreHouseOut> iPage = storeHouseOutMapper.selectPage(new Page<>(storeHouseOutParam.getPageParam().getPage(), storeHouseOutParam.getPageParam().getPageSize()), new QueryWrapper<StoreHouseOut>()
                    .eq(!StrUtil.isBlank(storeHouseOutParam.getStoreHouseOutStatus()), "store_house_out_status", storeHouseOutParam.getStoreHouseOutStatus())
                    .eq(!StrUtil.isBlank(storeHouseOutParam.getDeleteStatus()), "delete_status", storeHouseOutParam.getDeleteStatus())
                    .in(houseId.size() != 0, "store_house_id", houseId)
                    .in(staffId.size() != 0, "staff_id", staffId)
                    .in(targetId.size() != 0, "target_id", targetId));

            if (!StrUtil.isBlank(storeHouseOutParam.getCommodityName()) && houseId.size() == 0 || !StrUtil.isBlank(storeHouseOutParam.getStaffName()) && staffId.size() == 0 || !StrUtil.isBlank(storeHouseOutParam.getTargetName()) && targetId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), storeHouseOutConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(storeHouseOutConvert.convert(storeHouseOutMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> out(StoreHouseOut storeHouseOut) {
        int i = storeHouseOutMapper.updateById(storeHouseOut.setStoreHouseOutStatus(1).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            Long storeHouseId = storeHouseOutMapper.selectById(storeHouseOut.getId()).getStoreHouseId();
            Long commodityId = storeHouseMapper.selectById(storeHouseId).getCommodityId();
            Commodity commodity = commodityMapper.selectById(commodityId);
            i = commodityMapper.updateById(commodity.setCommodityNumber(commodity.getCommodityNumber() + storeHouseOut.getRealOutNumber()));
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
    public <T> Result<T> delete(StoreHouseOutParam storeHouseOutParam) {
        Long storeHouseId = storeHouseOutMapper.selectById(storeHouseOutParam.getId()).getStoreHouseId();
        StoreHouse storeHouse = storeHouseMapper.selectById(storeHouseId);
        if (storeHouse != null) {
            return Result.fail("该记录下还存在仓库信息，无法删除");
        } else {
            int i = storeHouseOutMapper.updateById(new StoreHouseOut().setId(Long.parseLong(storeHouseOutParam.getId())).setDeleteStatus(1));
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        }
    }
}
