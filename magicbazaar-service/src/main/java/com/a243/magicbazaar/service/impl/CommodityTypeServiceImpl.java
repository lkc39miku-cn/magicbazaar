package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.CommodityType;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.CommodityTypeMapper;
import com.a243.magicbazaar.service.CommodityTypeService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.CommodityTypeConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityTypeParam;
import com.a243.magicbazaar.view.vo.CommodityTypeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommodityTypeServiceImpl implements CommodityTypeService {
    private final CommodityTypeMapper commodityTypeMapper;
    private final CommodityTypeConvert commodityTypeConvert;
    private CommodityMapper commodityMapper;

    @Autowired
    public CommodityTypeServiceImpl(CommodityTypeMapper commodityTypeMapper,
                                    CommodityTypeConvert commodityTypeConvert) {
        this.commodityTypeMapper = commodityTypeMapper;
        this.commodityTypeConvert = commodityTypeConvert;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Override
    public Result<List<CommodityTypeVo>> list(CommodityTypeParam commodityTypeParam) {
        if (commodityTypeParam.getTree()) {
            List<CommodityType> commodityTypes = commodityTypeMapper.selectList(new QueryWrapper<CommodityType>()
                    .like(!StrUtil.isBlank(commodityTypeParam.getName()), "name", commodityTypeParam.getName())
                    .eq(!StrUtil.isBlank(commodityTypeParam.getPublishStatus()), "publish_status", commodityTypeParam.getPublishStatus()));
            return Result.ok(commodityTypes.size(), commodityTypeConvert.convert(commodityTypes));
        } else {
            String id = null;
            if (!StrUtil.isBlank(commodityTypeParam.getId())) {
                CommodityType commodityType = commodityTypeMapper.selectById(commodityTypeParam.getId());
                if (commodityType.getParentId() == 7) {
                    id = commodityTypeParam.getId();
                }
            }
            List<CommodityType> commodityTypes = commodityTypeMapper.selectList(new QueryWrapper<CommodityType>()
                    .eq(StrUtil.isBlank(commodityTypeParam.getCommodityTypeParentId()), "parent_id", "7")
                    .eq(!StrUtil.isBlank(commodityTypeParam.getCommodityTypeParentId()), "parent_id", commodityTypeParam.getCommodityTypeParentId())
                    .ne(!StrUtil.isBlank(id), "id", id)
                    .eq("publish_status", 1));
            return Result.ok(commodityTypeConvert.convert(commodityTypes));
        }
    }

    @Override
    public <T> Result<T> delete(CommodityTypeParam commodityTypeParam) {
        CommodityType commodityType = commodityTypeMapper.selectOne(new QueryWrapper<CommodityType>().eq("id", commodityTypeParam.getId()));
        int i = 0;
        boolean have;
        boolean delete = true;
        // 如果是一级菜单 查询其下子菜单
        if (commodityType.getParentId() == 7) {
            List<CommodityType> commodityTypeList = commodityTypeMapper.selectList(new QueryWrapper<CommodityType>().eq("parent_id", commodityTypeParam.getId()));
            for (CommodityType commodityType1 : commodityTypeList) {
                have = commodityMapper.selectList(new QueryWrapper<Commodity>().eq("commodity_type_id", commodityType1.getId())).size() != 0;
                if (!have) {
                    i += commodityTypeMapper.deleteById(commodityType1);
                }
                if (have) {
                    delete = false;
                }
            }
            if (!delete) {
                return Result.fail("该类型下的子类型中还存在商品信息，请更换类型再进行删除");
            }
            if (commodityTypeList.size() == i) {
                i = 0;
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        } else {
            List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>().eq("commodity_type_id", commodityTypeParam.getId()));
            if (commodityList.size() != 0) {
                return Result.fail("该类型下还存在商品信息，请更换类型再进行删除");
            }
        }
        i += commodityTypeMapper.deleteById(new CommodityType().setId(Long.parseLong(commodityTypeParam.getId())));
        if (i == 1) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> on(CommodityTypeParam commodityTypeParam) {
        CommodityType commodityType = commodityTypeMapper.selectOne(new QueryWrapper<CommodityType>().eq("id", commodityTypeParam.getId()));
        int i = 0;
        // 如果不是一级菜单 查询其子菜单下的商品
        if (commodityType.getParentId() == 7) {
            List<CommodityType> commodityTypeList = commodityTypeMapper.selectList(new QueryWrapper<CommodityType>().eq("parent_id", commodityTypeParam.getId()));
            for (CommodityType commodityType1 : commodityTypeList) {
                i += commodityTypeMapper.updateById(commodityType1.setPublishStatus(1));
            }
            if (commodityTypeList.size() == i) {
                i = 0;
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        }
        i += commodityTypeMapper.updateById(new CommodityType().setId(Long.parseLong(commodityTypeParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(CommodityTypeParam commodityTypeParam) {
        CommodityType commodityType = commodityTypeMapper.selectOne(new QueryWrapper<CommodityType>().eq("id", commodityTypeParam.getId()));
        int i = 0;
        boolean have;
        boolean update = true;
        // 如果不是一级菜单 查询其子菜单下的商品
        if (commodityType.getParentId() == 7) {
            List<CommodityType> commodityTypeList = commodityTypeMapper.selectList(new QueryWrapper<CommodityType>().eq("parent_id", commodityTypeParam.getId()));
            for (CommodityType commodityType1 : commodityTypeList) {
                have = commodityMapper.selectList(new QueryWrapper<Commodity>().eq("commodity_type_id", commodityType1.getId())).size() != 0;
                if (!have) {
                    i += commodityTypeMapper.updateById(commodityType1.setPublishStatus(0));
                }
                if (have) {
                    update = false;
                }
            }
            if (!update) {
                return Result.fail("该类型下的子类型中还存在商品信息，请更换类型再进行禁用");
            }
            if (commodityTypeList.size() == i) {
                i = 0;
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>().eq("commodity_type_id", commodityTypeParam.getId()));
            if (commodityList.size() != 0) {
                return Result.fail("该类型下还存在商品信息，请更换类型再进行禁用");
            }
        }
        i += commodityTypeMapper.updateById(new CommodityType().setId(Long.parseLong(commodityTypeParam.getId())).setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> add(CommodityType commodityType) {
        CommodityType name = commodityTypeMapper.selectOne(new QueryWrapper<CommodityType>().eq("name", commodityType.getName()).last(" limit 1"));
        if (name != null) {
            return Result.fail("商品类型昵称重复");
        }

        if (commodityType.getIsMenu().equals(0)) {
            commodityType.setParentId(7L);
        }
        // 添加商品类型
        int i = commodityTypeMapper.insert(commodityType.setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.ADD_SUCCESS);
        } else {
            return Result.fail(BusinessCode.ADD_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(CommodityType commodityType) {
        CommodityType one = commodityTypeMapper.selectById(commodityType.getId());
        CommodityType selectOne = commodityTypeMapper.selectOne(new QueryWrapper<CommodityType>().eq("name", commodityType.getName()).ne("name", one.getName()));

        if(selectOne != null) {
            return Result.fail("商品类型昵称重复");
        }

        // 更新商品类型
        int i = commodityTypeMapper.updateById(commodityType);
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<CommodityTypeVo> one(CommodityTypeParam commodityTypeParam) {
        CommodityType name = commodityTypeMapper.selectOne(new QueryWrapper<CommodityType>().eq("name", commodityTypeParam.getCommodityTypeName()));
        return Result.ok(commodityTypeConvert.convert(name));
    }

    @Override
    public Result<List<CommodityTypeVo>> child(CommodityTypeParam commodityTypeParam) {
        return Result.ok(commodityTypeConvert.convert(commodityTypeMapper.selectList(new QueryWrapper<CommodityType>().eq("parent_id", commodityTypeParam.getId()))));
    }
}
