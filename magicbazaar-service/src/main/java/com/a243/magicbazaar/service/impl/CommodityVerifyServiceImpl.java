package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.CommodityVerify;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.CommodityVerifyMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.service.CommodityVerifyService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.CommodityVerifyConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.CommodityVerifyParam;
import com.a243.magicbazaar.view.vo.CommodityVerifyVo;
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
public class CommodityVerifyServiceImpl implements CommodityVerifyService {
    private final CommodityVerifyMapper commodityVerifyMapper;
    private final CommodityVerifyConvert commodityVerifyConvert;
    private CommodityMapper commodityMapper;
    private StaffMapper staffMapper;

    @Autowired
    public CommodityVerifyServiceImpl(CommodityVerifyMapper commodityVerifyMapper,
                                      CommodityVerifyConvert commodityVerifyConvert) {
        this.commodityVerifyMapper = commodityVerifyMapper;
        this.commodityVerifyConvert = commodityVerifyConvert;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Override
    public Result<List<CommodityVerifyVo>> list(CommodityVerifyParam commodityVerifyParam) {
        if (commodityVerifyParam.getPageParam() != null) {
            List<Long> staffId = new ArrayList<>();
            List<Long> targetId = new ArrayList<>();
            List<Long> commodityId = new ArrayList<>();
            if (!StrUtil.isBlank(commodityVerifyParam.getCommodityName())) {
                commodityId = commodityMapper.selectList(new QueryWrapper<Commodity>().like("title", commodityVerifyParam.getCommodityName())).stream().map(Commodity::getId).collect(Collectors.toList());
            }
            if (!StrUtil.isBlank(commodityVerifyParam.getStaffName())) {
                staffId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", commodityVerifyParam.getStaffName())).stream().map(Staff::getId).collect(Collectors.toList());
            }
            if (!StrUtil.isBlank(commodityVerifyParam.getTargetName())) {
                targetId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", commodityVerifyParam.getTargetName())).stream().map(Staff::getId).collect(Collectors.toList());
            }
            IPage<CommodityVerify> iPage = commodityVerifyMapper.selectPage(new Page<>(commodityVerifyParam.getPageParam().getPage(), commodityVerifyParam.getPageParam().getPageSize()), new QueryWrapper<CommodityVerify>()
                    .eq(!StrUtil.isBlank(commodityVerifyParam.getVerifyStatus()) && "1".equals(commodityVerifyParam.getVerifyStatus()), "verify_status", commodityVerifyParam.getVerifyStatus())
                    .in(!StrUtil.isBlank(commodityVerifyParam.getVerifyStatus()) && !"1".equals(commodityVerifyParam.getVerifyStatus()), "verify_status", 0, 2)
                    .in(commodityId.size() != 0, "commodity_id", commodityId)
                    .in(staffId.size() != 0, "staff_id", staffId)
                    .in(targetId.size() != 0, "target_id", targetId)
                    .orderByAsc("verify_status"));

            if (!StrUtil.isBlank(commodityVerifyParam.getCommodityName()) && commodityId.size() == 0 || !StrUtil.isBlank(commodityVerifyParam.getStaffName()) && staffId.size() == 0 || !StrUtil.isBlank(commodityVerifyParam.getTargetName()) && targetId.size() == 0) {
                return Result.ok(0, null);
            }
            return Result.ok(Math.toIntExact(iPage.getTotal()), commodityVerifyConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(commodityVerifyConvert.convert(commodityVerifyMapper.selectList(new QueryWrapper<CommodityVerify>()
                    .orderByAsc("verify_status"))));
        }
    }

    @Override
    public <T> Result<T> success(CommodityVerifyParam commodityVerifyParam) {
        int i = commodityVerifyMapper.updateById(new CommodityVerify().setId(Long.parseLong(commodityVerifyParam.getId())).setVerifyStatus(1).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> fail(CommodityVerifyParam commodityVerifyParam) {
        int i = commodityVerifyMapper.updateById(new CommodityVerify().setId(Long.parseLong(commodityVerifyParam.getId())).setVerifyStatus(2).setVerifyInfo(commodityVerifyParam.getMessage()).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(CommodityVerifyParam commodityVerifyParam) {
        Long id = commodityVerifyMapper.selectOne(new QueryWrapper<CommodityVerify>().eq("id", commodityVerifyParam.getId())).getCommodityId();
        Commodity commodity = commodityMapper.selectById(id);
        if (commodity == null) {
            int i = commodityVerifyMapper.deleteById(commodityVerifyParam.getId());
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        } else {
            return Result.fail("该商品审核下还存在商品信息，无法进行删除");
        }
    }
}
