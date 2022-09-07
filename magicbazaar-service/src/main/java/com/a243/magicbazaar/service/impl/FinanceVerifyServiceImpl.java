package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.FinanceVerifyService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.FinanceVerifyConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.FinanceVerifyParam;
import com.a243.magicbazaar.view.vo.FinanceVerifyVo;
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
public class FinanceVerifyServiceImpl implements FinanceVerifyService {
    private final FinanceVerifyMapper financeVerifyMapper;
    private final FinanceVerifyConvert financeVerifyConvert;
    private CommodityMapper commodityMapper;
    private PurchaseMapper purchaseMapper;
    private PurchaseVerifyMapper purchaseVerifyMapper;
    private StaffMapper staffMapper;

    @Autowired
    public FinanceVerifyServiceImpl(FinanceVerifyMapper financeVerifyMapper, FinanceVerifyConvert financeVerifyConvert) {
        this.financeVerifyMapper = financeVerifyMapper;
        this.financeVerifyConvert = financeVerifyConvert;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Autowired
    public void setPurchaseVerifyMapper(PurchaseVerifyMapper purchaseVerifyMapper) {
        this.purchaseVerifyMapper = purchaseVerifyMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Override
    public Result<List<FinanceVerifyVo>> list(FinanceVerifyParam financeVerifyParam) {
        if (financeVerifyParam.getPageParam() != null) {
            List<Long> staffId = new ArrayList<>();
            List<Long> targetId = new ArrayList<>();
            List<Long> purchaseVerifyId = new ArrayList<>();
            if (!StrUtil.isBlank(financeVerifyParam.getCommodityName())) {
                List<Long> title = commodityMapper.selectList(new QueryWrapper<Commodity>().like("title", financeVerifyParam.getCommodityName())).stream().map(Commodity::getId).collect(Collectors.toList());
                if (title.size() != 0) {
                    List<Long> purchase = purchaseMapper.selectList(new QueryWrapper<Purchase>().in("commodity_id", title)).stream().map(Purchase::getId).collect(Collectors.toList());
                    if (purchase.size() != 0) {
                        purchaseVerifyId = purchaseVerifyMapper.selectList(new QueryWrapper<PurchaseVerify>().in("purchase_id", purchase)).stream().map(PurchaseVerify::getId).collect(Collectors.toList());
                    }
                }
            }
            if (!StrUtil.isBlank(financeVerifyParam.getStaffName())) {
                staffId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", financeVerifyParam.getStaffName())).stream().map(Staff::getId).collect(Collectors.toList());
            }
            if (!StrUtil.isBlank(financeVerifyParam.getTargetName())) {
                targetId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", financeVerifyParam.getTargetName())).stream().map(Staff::getId).collect(Collectors.toList());
            }

            IPage<FinanceVerify> iPage = financeVerifyMapper.selectPage(new Page<>(financeVerifyParam.getPageParam().getPage(), financeVerifyParam.getPageParam().getPageSize()), new QueryWrapper<FinanceVerify>()
                    .eq(!StrUtil.isBlank(financeVerifyParam.getVerifyStatus()) && "1".equals(financeVerifyParam.getVerifyStatus()), "verify_status", financeVerifyParam.getVerifyStatus())
                    .in(!StrUtil.isBlank(financeVerifyParam.getVerifyStatus()) && !"1".equals(financeVerifyParam.getVerifyStatus()), "verify_status", 0, 2)
                    .in(purchaseVerifyId.size() != 0, "purchase_verify_id", purchaseVerifyId)
                    .in(staffId.size() != 0, "staff_id", staffId)
                    .in(targetId.size() != 0, "target_id", targetId)
                    .orderByAsc("verify_status"));

            if (!StrUtil.isBlank(financeVerifyParam.getCommodityName()) && purchaseVerifyId.size() == 0 || !StrUtil.isBlank(financeVerifyParam.getStaffName()) && staffId.size() == 0 || !StrUtil.isBlank(financeVerifyParam.getTargetName()) && targetId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), financeVerifyConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(financeVerifyConvert.convert(financeVerifyMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> success(FinanceVerifyParam financeVerifyParam) {
        int i = financeVerifyMapper.updateById(new FinanceVerify().setId(Long.parseLong(financeVerifyParam.getId())).setVerifyStatus(1).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(FinanceVerifyParam financeVerifyParam) {
        // 查看是否存在采购单
        Long id = purchaseVerifyMapper.selectById(financeVerifyMapper.selectById(financeVerifyParam.getId()).getPurchaseVerifyId()).getId();
        PurchaseVerify purchaseVerify = purchaseVerifyMapper.selectById(id);
        if (purchaseVerify != null) {
            return Result.fail("该审核记录下还存在采购审核信息，无法删除，请先删除采购信息");
        } else {
            int i = financeVerifyMapper.deleteById(financeVerifyParam.getId());
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        }
    }

    @Override
    public <T> Result<T> fail(FinanceVerifyParam financeVerifyParam) {
        int i = financeVerifyMapper.updateById(new FinanceVerify().setId(Long.parseLong(financeVerifyParam.getId())).setVerifyStatus(2).setStaffId(StaffThreadLocal.get().getId()).setVerifyInfo(financeVerifyParam.getInfoName()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }
}
