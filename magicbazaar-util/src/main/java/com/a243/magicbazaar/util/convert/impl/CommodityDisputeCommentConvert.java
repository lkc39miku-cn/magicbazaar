package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.CommodityDisputeComment;
import com.a243.magicbazaar.dao.mapper.CommodityDisputeInfoMapper;
import com.a243.magicbazaar.dao.mapper.CommodityDisputeMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityDisputeCommentVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityDisputeCommentConvert implements Convert<CommodityDisputeComment, CommodityDisputeCommentVo> {
    private UserMapper userMapper;
    private StaffMapper staffMapper;
    private CommodityDisputeMapper commodityDisputeMapper;
    private CommodityDisputeInfoMapper commodityDisputeInfoMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Autowired
    public void setCommodityDisputeMapper(CommodityDisputeMapper commodityDisputeMapper) {
        this.commodityDisputeMapper = commodityDisputeMapper;
    }

    @Autowired
    public void setCommodityDisputeInfoMapper(CommodityDisputeInfoMapper commodityDisputeInfoMapper) {
        this.commodityDisputeInfoMapper = commodityDisputeInfoMapper;
    }

    public abstract CommodityDisputeCommentVo convert(CommodityDisputeComment commodityDisputeComment);

    public abstract List<CommodityDisputeCommentVo> convert(List<CommodityDisputeComment> commodityDisputeCommentList);

    @AfterMapping
    public void convert(CommodityDisputeComment commodityDisputeComment, @MappingTarget CommodityDisputeCommentVo commodityDisputeCommentVo) {
        commodityDisputeCommentVo.setUserName(userMapper.selectById(commodityDisputeComment.getUserId()).getNickname());
        commodityDisputeCommentVo.setUserAvatar(userMapper.selectById(commodityDisputeComment.getUserId()).getAvatar());
        if (commodityDisputeComment.getStaffId() != null) {
            commodityDisputeCommentVo.setStaffName(staffMapper.selectById(commodityDisputeComment.getStaffId()).getUsername());
        }
        commodityDisputeCommentVo.setCommodityDisputeInfoNumber(commodityDisputeMapper.selectById(commodityDisputeInfoMapper.selectById(commodityDisputeComment.getCommodityDisputeInfoId()).getCommodityDisputeId()).getDisputeNumber());
    }

    @AfterMapping
    public void convert(List<CommodityDisputeComment> commodityDisputeCommentList, @MappingTarget List<CommodityDisputeCommentVo> commodityDisputeCommentVoList) {
        for (int i = 0; i < commodityDisputeCommentList.size(); i++) {
            convert(commodityDisputeCommentList.get(i), commodityDisputeCommentVoList.get(i));
        }
    }
}
