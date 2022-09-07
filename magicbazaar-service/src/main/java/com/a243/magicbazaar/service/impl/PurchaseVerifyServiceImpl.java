package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.Purchase;
import com.a243.magicbazaar.dao.entity.PurchaseVerify;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.PurchaseMapper;
import com.a243.magicbazaar.dao.mapper.PurchaseVerifyMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.service.PurchaseVerifyService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.PurchaseVerifyConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.PurchaseVerifyParam;
import com.a243.magicbazaar.view.vo.PurchaseVerifyVo;
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
public class PurchaseVerifyServiceImpl implements PurchaseVerifyService {

    private final PurchaseVerifyMapper purchaseVerifyMapper;
    private final PurchaseVerifyConvert purchaseVerifyConvert;
    private StaffMapper staffMapper;
    private CommodityMapper commodityMapper;
    private PurchaseMapper purchaseMapper;

    @Autowired
    public PurchaseVerifyServiceImpl(PurchaseVerifyMapper purchaseVerifyMapper, PurchaseVerifyConvert purchaseVerifyConvert) {
        this.purchaseVerifyMapper = purchaseVerifyMapper;
        this.purchaseVerifyConvert = purchaseVerifyConvert;
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
    public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public Result<List<PurchaseVerifyVo>> list(PurchaseVerifyParam purchaseVerifyParam) {
        if (purchaseVerifyParam.getPageParam() != null) {
            List<Long> staffId = new ArrayList<>();
            List<Long> targetId = new ArrayList<>();
            List<Long> purchaseId = new ArrayList<>();
            if (!StrUtil.isBlank(purchaseVerifyParam.getCommodityName())) {
                List<Long> title = commodityMapper.selectList(new QueryWrapper<Commodity>().like("title", purchaseVerifyParam.getCommodityName())).stream().map(Commodity::getId).collect(Collectors.toList());
                if (title.size() != 0) {
                    purchaseId = purchaseMapper.selectList(new QueryWrapper<Purchase>().in("commodity_id", title)).stream().map(Purchase::getId).collect(Collectors.toList());
                }
            }
            if (!StrUtil.isBlank(purchaseVerifyParam.getStaffName())) {
                staffId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", purchaseVerifyParam.getStaffName())).stream().map(Staff::getId).collect(Collectors.toList());
            }
            if (!StrUtil.isBlank(purchaseVerifyParam.getTargetName())) {
                targetId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", purchaseVerifyParam.getTargetName())).stream().map(Staff::getId).collect(Collectors.toList());
            }

            IPage<PurchaseVerify> iPage = purchaseVerifyMapper.selectPage(new Page<>(purchaseVerifyParam.getPageParam().getPage(), purchaseVerifyParam.getPageParam().getPageSize()), new QueryWrapper<PurchaseVerify>()
                    .eq(!StrUtil.isBlank(purchaseVerifyParam.getVerifyStatus()) && "1".equals(purchaseVerifyParam.getVerifyStatus()), "verify_status", purchaseVerifyParam.getVerifyStatus())
                    .in(!StrUtil.isBlank(purchaseVerifyParam.getVerifyStatus()) && !"1".equals(purchaseVerifyParam.getVerifyStatus()), "verify_status", 0, 2)
                    .in(purchaseId.size() != 0, "purchase_id", purchaseId)
                    .in(staffId.size() != 0, "staff_id", staffId)
                    .in(targetId.size() != 0, "target_id", targetId)
                    .orderByAsc("verify_status"));

            if (!StrUtil.isBlank(purchaseVerifyParam.getCommodityName()) && purchaseId.size() == 0 || !StrUtil.isBlank(purchaseVerifyParam.getStaffName()) && staffId.size() == 0 || !StrUtil.isBlank(purchaseVerifyParam.getTargetName()) && targetId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), purchaseVerifyConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(purchaseVerifyConvert.convert(purchaseVerifyMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> success(PurchaseVerifyParam purchaseVerifyParam) {
        int i = purchaseVerifyMapper.updateById(new PurchaseVerify().setId(Long.parseLong(purchaseVerifyParam.getId())).setVerifyStatus(1).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> fail(PurchaseVerifyParam purchaseVerifyParam) {
        int i = purchaseVerifyMapper.updateById(new PurchaseVerify().setId(Long.parseLong(purchaseVerifyParam.getId())).setVerifyStatus(2).setStaffId(StaffThreadLocal.get().getId()).setVerifyInfo(purchaseVerifyParam.getInfoName()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(PurchaseVerifyParam purchaseVerifyParam) {
        // 查看是否存在采购单
        Long id = purchaseMapper.selectById(purchaseVerifyMapper.selectById(purchaseVerifyParam.getId()).getPurchaseId()).getId();
        Purchase purchase = purchaseMapper.selectById(id);
        if (purchase == null) {
            int i = purchaseVerifyMapper.deleteById(purchaseVerifyParam.getId());
            if (i == 0) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        } else {
            return Result.fail("该审核记录下还存在采购信息，无法删除，请先删除采购信息");
        }
    }
}
