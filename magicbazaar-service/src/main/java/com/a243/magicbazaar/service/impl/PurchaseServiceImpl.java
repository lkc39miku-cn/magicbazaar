package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.PurchaseService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.PurchaseConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.PurchaseParam;
import com.a243.magicbazaar.view.vo.PurchaseVo;
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
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseMapper purchaseMapper;
    private final PurchaseConvert purchaseConvert;
    private CommodityMapper commodityMapper;
    private StaffMapper staffMapper;
    private PurchaseVerifyMapper purchaseVerifyMapper;
    private FinanceVerifyMapper financeVerifyMapper;
    private StoreHouseInMapper storeHouseInMapper;

    @Autowired
    public PurchaseServiceImpl(PurchaseMapper purchaseMapper,
                               PurchaseConvert purchaseConvert) {
        this.purchaseMapper = purchaseMapper;
        this.purchaseConvert = purchaseConvert;
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
    public void setPurchaseVerifyMapper(PurchaseVerifyMapper purchaseVerifyMapper) {
        this.purchaseVerifyMapper = purchaseVerifyMapper;
    }

    @Autowired
    public void setFinanceVerifyMapper(FinanceVerifyMapper financeVerifyMapper) {
        this.financeVerifyMapper = financeVerifyMapper;
    }

    @Autowired
    public void setStoreHouseInMapper(StoreHouseInMapper storeHouseInMapper) {
        this.storeHouseInMapper = storeHouseInMapper;
    }

    @Override
    public Result<List<PurchaseVo>> list(PurchaseParam purchaseParam) {
        if (purchaseParam.getPageParam() != null) {
            List<Long> staffId = new ArrayList<>();
            List<Long> commodityId = new ArrayList<>();
            if (!StrUtil.isBlank(purchaseParam.getCommodityName())) {
                commodityId = commodityMapper.selectList(new QueryWrapper<Commodity>().like("title", purchaseParam.getCommodityName())).stream().map(Commodity::getId).collect(Collectors.toList());
            }
            if (!StrUtil.isBlank(purchaseParam.getStaffName())) {
                staffId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", purchaseParam.getStaffName())).stream().map(Staff::getId).collect(Collectors.toList());
            }
            List<Long> v_status = new ArrayList<>();
            if (!StrUtil.isBlank(purchaseParam.getVerifyStatus())) {
                if (purchaseParam.getVerifyStatus().equals("1")) {
                    v_status.add(1L);
                } else {
                    v_status.add(0L);
                    v_status.add(2L);
                }
            }
            List<Long> f_status = new ArrayList<>();
            if (!StrUtil.isBlank(purchaseParam.getFinanceStatus())) {
                if (purchaseParam.getFinanceStatus().equals("1")) {
                    f_status.add(1L);
                } else {
                    f_status.add(0L);
                    f_status.add(2L);
                }
            }
            List<Long> verify_status = new ArrayList<>();
            if (f_status.size() != 0) {
                verify_status = financeVerifyMapper.selectList(new QueryWrapper<FinanceVerify>().in("verify_status", f_status)).stream().map(FinanceVerify::getPurchaseVerifyId).collect(Collectors.toList());
            }
            IPage<Purchase> iPage = purchaseMapper.selectPage(new Page<>(purchaseParam.getPageParam().getPage(), purchaseParam.getPageParam().getPageSize()), new QueryWrapper<Purchase>()
                    .eq(!StrUtil.isBlank(purchaseParam.getPurchaseStatus()), "purchase_status", purchaseParam.getPurchaseStatus())
                    .in(commodityId.size() != 0, "commodity_id", commodityId)
                    .in(staffId.size() != 0, "staff_id", staffId)
                    .in(v_status.size() != 0, "id", purchaseVerifyMapper.selectList(new QueryWrapper<PurchaseVerify>().in(v_status.size() != 0, "verify_status", v_status)).stream().map(PurchaseVerify::getPurchaseId).collect(Collectors.toList()))
                    .in(verify_status.size() != 0, "id", purchaseVerifyMapper.selectList(new QueryWrapper<PurchaseVerify>().in(verify_status.size() != 0, "id", verify_status)).stream().map(PurchaseVerify::getPurchaseId).collect(Collectors.toList())));

            if (!StrUtil.isBlank(purchaseParam.getCommodityName()) && commodityId.size() == 0 || !StrUtil.isBlank(purchaseParam.getStaffName()) && staffId.size() == 0) {
                return Result.ok(0, null);
            }

            if (f_status.size() > 0 && verify_status.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), purchaseConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(purchaseConvert.convert(purchaseMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> add(Purchase purchase) {
        int i = purchaseMapper.insert(purchase.setReply("").setPurchaseStatus(0).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            PurchaseVerify purchaseVerify = new PurchaseVerify();
            i = purchaseVerifyMapper.insert(purchaseVerify.setPurchaseId(purchase.getId()).setVerifyStatus(0).setTargetId(StaffThreadLocal.get().getId()));
            if (i == 1) {
                i = financeVerifyMapper.insert(new FinanceVerify().setPurchaseVerifyId(purchaseVerify.getId()).setVerifyStatus(0).setTargetId(StaffThreadLocal.get().getId()));
                if (i == 1) {
                    return Result.ok(BusinessCode.ADD_SUCCESS);
                } else {
                    return Result.fail(BusinessCode.ADD_ERROR);
                }
            } else {
                return Result.fail(BusinessCode.ADD_ERROR);
            }
        } else {
            return Result.fail(BusinessCode.ADD_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(Purchase purchase) {
        int i = purchaseMapper.updateById(purchase);
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> again(PurchaseParam purchaseParam) {
        int i = purchaseMapper.updateById(new Purchase().setId(Long.parseLong(purchaseParam.getId())).setPurchaseStatus(0).setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            PurchaseVerify purchaseVerify = purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchaseParam.getId()));
            if (purchaseVerify.getVerifyStatus() != 1) {
                purchaseVerifyMapper.updateById(purchaseVerify.setVerifyStatus(0));
            }
            FinanceVerify financeVerify = financeVerifyMapper.selectOne(new QueryWrapper<FinanceVerify>().eq("purchase_verify_id", purchaseVerify.getId()));
            if (financeVerify.getVerifyStatus() != 1) {
                financeVerifyMapper.updateById(financeVerify.setVerifyStatus(0));
            }
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }


    @Override
    public <T> Result<T> reUpdate(Purchase purchase) {
        int i = purchaseMapper.updateById(purchase.setPurchaseStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<String> info(PurchaseParam purchaseParam) {
        PurchaseVerify purchaseVerify = purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchaseParam.getId()));
        StringBuilder stringBuilder = new StringBuilder();
        if (purchaseVerify.getVerifyStatus() == 2) {
            stringBuilder.append("订单审核拒绝原因：").append(purchaseVerify.getVerifyInfo());
        }
        if (!stringBuilder.isEmpty()) {
            stringBuilder.append(",");
        }
        FinanceVerify financeVerify = financeVerifyMapper.selectOne(new QueryWrapper<FinanceVerify>().eq("purchase_verify_id", purchaseVerify.getId()));
        if (financeVerify.getVerifyStatus() == 2) {
            stringBuilder.append("财务审核拒绝原因：").append(purchaseVerify.getVerifyInfo());
        }
        return Result.ok(stringBuilder.toString());
    }

    @Override
    public <T> Result<T> yes(PurchaseParam purchaseParam) {
        // 完成订单
        int i = purchaseMapper.updateById(new Purchase().setId(Long.parseLong(purchaseParam.getId())).setPurchaseStatus(3));
        if (i == 1) {
            // 入库信息添加
            Purchase purchase = purchaseMapper.selectById(purchaseParam.getId());
            i = storeHouseInMapper.insert(new StoreHouseIn().setPurchaseId(purchase.getId()).setInNumber(purchase.getRealPurchaseNumber()).setStoreHouseInStatus(0).setDeleteStatus(0).setTargetId(StaffThreadLocal.get().getId()));
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
    public <T> Result<T> no(PurchaseParam purchaseParam) {
        // 放弃订单
        int i = purchaseMapper.updateById(new Purchase().setId(Long.parseLong(purchaseParam.getId())).setPurchaseStatus(2));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(PurchaseParam purchaseParam) {
        // 删除订购单查看其下是否拥有商品
        Purchase purchase = purchaseMapper.selectById(purchaseParam.getId());
        Commodity commodity = commodityMapper.selectById(purchase.getCommodityId());
        if (commodity != null) {
            return Result.fail("该采购单下还存在商品，无法删除");
        } else {
            int i = purchaseMapper.deleteById(purchaseParam.getId());
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        }
    }
}
