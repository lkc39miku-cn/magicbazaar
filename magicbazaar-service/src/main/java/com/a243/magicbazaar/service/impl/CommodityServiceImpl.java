package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.CommodityService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.CommodityConvert;
import com.a243.magicbazaar.util.convert.impl.CommodityVerifyConvert;
import com.a243.magicbazaar.util.convert.impl.StoreHouseConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.CommodityParam;
import com.a243.magicbazaar.view.vo.CommodityVerifyVo;
import com.a243.magicbazaar.view.vo.CommodityVo;
import com.a243.magicbazaar.view.vo.StoreHouseVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    private final CommodityMapper commodityMapper;
    private final CommodityConvert commodityConvert;
    private CommodityTypeMapper commodityTypeMapper;
    private CommodityVerifyMapper commodityVerifyMapper;
    private StoreHouseMapper storeHouseMapper;
    private StoreHouseConvert storeHouseConvert;
    private StoreHouseOutMapper storeHouseOutMapper;
    private CommodityVerifyConvert commodityVerifyConvert;
    private OrderMapper orderMapper;
    private OrderInfoMapper orderInfoMapper;
    private PurchaseMapper purchaseMapper;

    @Autowired
    public CommodityServiceImpl(CommodityMapper commodityMapper,
                                CommodityConvert commodityConvert) {
        this.commodityMapper = commodityMapper;
        this.commodityConvert = commodityConvert;
    }

    @Autowired
    public void setCommodityTypeMapper(CommodityTypeMapper commodityTypeMapper) {
        this.commodityTypeMapper = commodityTypeMapper;
    }


    @Autowired
    public void setCommodityVerifyMapper(CommodityVerifyMapper commodityVerifyMapper) {
        this.commodityVerifyMapper = commodityVerifyMapper;
    }

    @Autowired
    public void setStoreHouseMapper(StoreHouseMapper storeHouseMapper) {
        this.storeHouseMapper = storeHouseMapper;
    }

    @Autowired
    public void setStoreHouseConvert(StoreHouseConvert storeHouseConvert) {
        this.storeHouseConvert = storeHouseConvert;
    }

    @Autowired
    public void setStoreHouseOutMapper(StoreHouseOutMapper storeHouseOutMapper) {
        this.storeHouseOutMapper = storeHouseOutMapper;
    }

    @Autowired
    public void setCommodityVerifyConvert(CommodityVerifyConvert commodityVerifyConvert) {
        this.commodityVerifyConvert = commodityVerifyConvert;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public Result<List<CommodityVo>> list(CommodityParam commodityParam) {
        if (commodityParam.getPageParam() != null) {
            List<String> list = new ArrayList<>();
            if (!StrUtil.isBlank(commodityParam.getVerifyStatus())) {
                if (commodityParam.getVerifyStatus().equals("1")) {
                    list.add("1");
                } else {
                    list.add("0");
                    list.add("2");
                }
            }
            List<Long> verify_status = commodityVerifyMapper.selectList(new QueryWrapper<CommodityVerify>().in(list.size() != 0, "verify_status", list))
                    .stream().map(CommodityVerify::getCommodityId).collect(Collectors.toList());
            IPage<Commodity> iPage = commodityMapper.selectPage(new Page<>(
                    commodityParam.getPageParam().getPage(), commodityParam.getPageParam().getPageSize()), new QueryWrapper<Commodity>()
                    .eq(!StrUtil.isBlank(commodityParam.getCommodityTypeId()), "commodity_type_id", commodityParam.getCommodityTypeId())
                    .in(StrUtil.isBlank(commodityParam.getCommodityTypeId()) && (!StrUtil.isBlank(commodityParam.getCommodityTypeParentId()))
                            , "commodity_type_id", commodityTypeMapper.selectList(new QueryWrapper<CommodityType>()
                                    .eq("parent_id", commodityParam.getCommodityTypeParentId())).stream().map(CommodityType::getId).collect(Collectors.toList()))
                    .like(!StrUtil.isBlank(commodityParam.getTitle()), "title", commodityParam.getTitle())
                    .eq(!StrUtil.isBlank(commodityParam.getCommodityStatus()), "commodity_status", commodityParam.getCommodityStatus())
                    .eq(!StrUtil.isBlank(commodityParam.getPublishStatus()), "publish_status", commodityParam.getPublishStatus())
                    .eq(!StrUtil.isBlank(commodityParam.getPromotionType()), "promotion_type", commodityParam.getPromotionType())
                    .eq(!StrUtil.isBlank(commodityParam.getDeleteStatus()), "delete_status", commodityParam.getDeleteStatus())
                    .in(verify_status.size() != 0, "id", verify_status));

            if (!StrUtil.isBlank(commodityParam.getVerifyStatus()) && verify_status.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), commodityConvert.convert(iPage.getRecords()));
        } else {
            List<CommodityVo> convert = commodityConvert.convert(commodityMapper.selectList(new QueryWrapper<Commodity>()
                    .like(!StrUtil.isBlank(commodityParam.getTitle()), "title", commodityParam.getTitle())));
            return Result.ok(convert.size(), convert);
        }
    }

    @Override
    public <T> Result<T> add(Commodity commodity) {
        int i = commodityMapper.insert(commodity.setPromotionType(0).setPublishStatus(0).setDeleteStatus(0).setCommodityNumber(0L));
        if (i == 1) {
            i = commodityVerifyMapper.insert(new CommodityVerify().setCommodityId(commodity.getId()).setVerifyStatus(0).setTargetId(StaffThreadLocal.get().getId()));
            if (i == 1) {

                int insert = storeHouseMapper.insert(new StoreHouse().setCommodityId(commodity.getId()).setStock(0L).setLowStock(0L).setDeleteStatus(0));

                if (insert == 1) {
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
    public <T> Result<T> update(Commodity commodity) {
        int i = commodityMapper.updateById(commodity);

        if (commodity.getCommodityStatus() != null) {
            if (commodity.getCommodityStatus() == 1) {
                UpdateWrapper<Commodity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("preview_end_time", null).set("preview_info", null).set("sale_start_time", null).eq("id", commodity.getId());
                update(updateWrapper);
            }
        }
        if (commodity.getPromotionType() != null) {
            if (commodity.getPromotionType() == 0) {
                UpdateWrapper<Commodity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("promotion_price", null).set("promotion_start_time", null).set("promotion_end_time", null).eq("id", commodity.getId());
                update(updateWrapper);
            }
        }
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(CommodityParam commodityParam) {

        Commodity commodity = commodityMapper.selectById(commodityParam.getId());
        if (commodity.getPublishStatus() == 1) {
            return Result.fail("该商品上架中，请先下架");
        }

        int i = commodityMapper.updateById(new Commodity().setId(Long.parseLong(commodityParam.getId())).setDeleteStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> addNum(CommodityParam commodityParam) {
        StoreHouse commodity_id = storeHouseMapper.selectOne(new QueryWrapper<StoreHouse>().eq("commodity_id", commodityParam.getId()));
        if (commodity_id.getStock() < Long.parseLong(commodityParam.getNum())) {
            return Result.fail("当前仓库货物不足");
        }
        int i = storeHouseOutMapper.insert(new StoreHouseOut().setStoreHouseId(storeHouseMapper.selectOne(new QueryWrapper<StoreHouse>().eq("commodity_id", commodityParam.getId())).getId()).setOutNumber(Long.parseLong(commodityParam.getNum())).setStoreHouseOutStatus(0).setDeleteStatus(0).setTargetId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            storeHouseMapper.updateById(commodity_id.setStock(commodity_id.getStock() - Long.parseLong(commodityParam.getNum())));
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(CommodityParam commodityParam) {
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("commodity_id", commodityParam.getId()).eq("order_type_id", 1));
        if (orderInfoList.size() != 0) {
            return Result.fail("现在还有订单运行中，请等候订单完成后再进行下架");
        }
        int i = commodityMapper.updateById(new Commodity().setId(Long.parseLong(commodityParam.getId())).setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> on(CommodityParam commodityParam) {
        int i = commodityMapper.updateById(new Commodity().setId(Long.parseLong(commodityParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<StoreHouseVo> storeNum(CommodityParam commodityParam) {
        return Result.ok(storeHouseConvert.convert(storeHouseMapper.selectOne(new QueryWrapper<StoreHouse>().eq("commodity_id", commodityParam.getId()))));
    }

    @Override
    public Result<CommodityVerifyVo> failInfo(CommodityParam commodityParam) {
        return Result.ok(commodityVerifyConvert.convert(commodityVerifyMapper.selectOne(new QueryWrapper<CommodityVerify>().eq("commodity_id", commodityParam.getId()))));
    }

    @Override
    public <T> Result<T> again(CommodityParam commodityParam) {
        int i = commodityVerifyMapper.update(new CommodityVerify().setVerifyStatus(0).setTargetId(StaffThreadLocal.get().getId()), new QueryWrapper<CommodityVerify>().eq("commodity_id", commodityParam.getId()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> checkName(CommodityParam commodityParam) {
        if (commodityParam.getCheck()) {
            Commodity commodity = commodityMapper.selectOne(new QueryWrapper<Commodity>().eq("title", commodityParam.getTitle()).ne("title", commodityParam.getTitle()));
            if (commodity == null) {
                return Result.ok();
            } else {
                return Result.fail("商品名称重复");
            }
        } else {
            Commodity one = commodityMapper.selectOne(new QueryWrapper<Commodity>().eq("title", commodityParam.getTitle()));
            if (one == null) {
                return Result.ok();
            } else {
                return Result.fail("商品名称重复");
            }
        }
    }

    @Override
    public <T> Result<T> checkTime(CommodityParam commodityParam) {
        Commodity commodity = commodityMapper.selectOne(new QueryWrapper<Commodity>()
                .le("preview_end_time", LocalDate.now())
                .gt("sale_start_time", LocalDate.now())
                .eq("id", commodityParam.getId())
                .eq("commodity_status", 0));

        if (commodity == null) {
            return Result.ok();
        } else {
            return Result.fail("请更改截止时间或发售时间再次进行上架");
        }
    }

    @Override
    public Result<String> getMoney(LocalDate startTime, LocalDate endTime) {
        StringBuilder date = new StringBuilder();
        if (startTime != null && endTime != null) {
            do {
                List<Order> orders = orderMapper.selectList(new QueryWrapper<Order>().eq("Date(create_time)", startTime));

                if (orders.size() != 0) {
                    BigDecimal bigDecimal = new BigDecimal(0);
                    for (Order order : orders) {

                        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

                        if (orderInfoList.size() != 0) {
                            for(OrderInfo orderInfo : orderInfoList) {
                                if (orderInfo.getOrderTypeId() == 4 || order.getOrderTypeId() == 5) {
                                    bigDecimal = bigDecimal.add(orderInfo.getAllPrice());
                                }
                            }
                        }

                    }

                    if (bigDecimal.equals(new BigDecimal(0))) {
                        date.append("0,");
                    } else {
                        date.append(bigDecimal.toString()).append(",");
                    }
                } else {
                    date.append("0,");
                }
                startTime = startTime.plusDays(1);
            } while (startTime.isBefore(endTime) || startTime.isEqual(endTime));

        } else {
            LocalDate localDate = LocalDate.now().plusDays(-6);
            do {
                List<Order> orders = orderMapper.selectList(new QueryWrapper<Order>().eq("Date(create_time)", localDate));

                if (orders.size() != 0) {
                    BigDecimal bigDecimal = new BigDecimal(0);
                    for (Order order : orders) {

                        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

                        if (orderInfoList.size() != 0) {
                            for(OrderInfo orderInfo : orderInfoList) {
                                if (orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5) {
                                    bigDecimal = bigDecimal.add(orderInfo.getAllPrice());
                                }
                            }
                        }

                    }

                    if (bigDecimal.equals(new BigDecimal(0))) {
                        date.append("0,");
                    } else {
                        date.append(bigDecimal.toString()).append(",");
                    }
                } else {
                    date.append("0,");
                }
                localDate = localDate.plusDays(1);
            } while (localDate.isBefore(LocalDate.now()) || localDate.isEqual(LocalDate.now()));

        }
        return Result.ok(date.toString());
    }

    @Override
    public Result<String> outMoney(LocalDate startTime, LocalDate endTime) {
        StringBuilder date = new StringBuilder();
        if (startTime != null && endTime != null) {
            do {
                List<Purchase> purchaseList = purchaseMapper.selectList(new QueryWrapper<Purchase>().eq("Date(create_time)", startTime).eq("purchase_status", 3));

                if (purchaseList.size() != 0) {
                    BigDecimal bigDecimal = new BigDecimal(0);
                    for (Purchase purchase : purchaseList) {
                        bigDecimal = bigDecimal.add(purchase.getRealCost());
                    }

                    if (bigDecimal.equals(new BigDecimal(0))) {
                        date.append("0,");
                    } else {
                        date.append(bigDecimal.toString()).append(",");
                    }
                } else {
                    date.append("0,");
                }
                startTime = startTime.plusDays(1);
            } while (startTime.isBefore(endTime) || startTime.isEqual(endTime));

        } else {
            LocalDate localDate = LocalDate.now().plusDays(-6);
            do {
                List<Purchase> purchaseList = purchaseMapper.selectList(new QueryWrapper<Purchase>().eq("Date(create_time)", localDate).eq("purchase_status", 3));

                if (purchaseList.size() != 0) {
                    BigDecimal bigDecimal = new BigDecimal(0);
                    for (Purchase purchase : purchaseList) {
                        bigDecimal = bigDecimal.add(purchase.getRealCost());
                    }

                    if (bigDecimal.equals(new BigDecimal(0))) {
                        date.append("0,");
                    } else {
                        date.append(bigDecimal.toString()).append(",");
                    }
                } else {
                    date.append("0,");
                }
                localDate = localDate.plusDays(1);
            } while (localDate.isBefore(LocalDate.now()) || localDate.isEqual(LocalDate.now()));

        }
        return Result.ok(date.toString());
    }
}
