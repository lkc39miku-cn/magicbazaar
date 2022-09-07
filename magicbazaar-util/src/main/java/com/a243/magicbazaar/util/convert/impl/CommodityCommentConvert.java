package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.CommodityComment;
import com.a243.magicbazaar.dao.mapper.CommodityCommentMapper;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityCommentVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityCommentConvert implements Convert<CommodityComment, CommodityCommentVo> {
    private CommodityMapper commodityMapper;
    private UserMapper userMapper;
    private CommodityCommentMapper commodityCommentMapper;

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Autowired
    public void setCommodityCommentMapper(CommodityCommentMapper commodityCommentMapper) {
        this.commodityCommentMapper = commodityCommentMapper;
    }

    public abstract CommodityCommentVo convert(CommodityComment commodityComment);
    public abstract List<CommodityCommentVo> convert(List<CommodityComment> commodityCommentList);

    @AfterMapping
    public void convert(CommodityComment commodityComment, @MappingTarget CommodityCommentVo commodityCommentVo) {
        commodityCommentVo.setCommodityName(commodityMapper.selectById(commodityComment.getCommodityId()).getTitle());
        commodityCommentVo.setUserName(userMapper.selectById(commodityComment.getUserId()).getNickname());
        commodityCommentVo.setCommodityPhoto(commodityMapper.selectById(commodityComment.getCommodityId()).getPhoto());
        commodityCommentVo.setUserPhoto(userMapper.selectById(commodityComment.getUserId()).getAvatar());
        if (commodityComment.getToUserId() != null) {
            commodityCommentVo.setToUserName(userMapper.selectById(commodityComment.getToUserId()).getNickname());
        } else {
            commodityCommentVo.setToUserName(null);
        }
    }

    @AfterMapping
    public void convert(List<CommodityComment> commodityCommentList, @MappingTarget List<CommodityCommentVo> commodityCommentVoList) {
        for (int i = 0; i < commodityCommentList.size(); i++) {
            convert(commodityCommentList.get(i), commodityCommentVoList.get(i));
        }
    }

    public List<CommodityCommentVo> childChange(Long id) {
        List<CommodityComment> commodityComments = commodityCommentMapper.selectList(new QueryWrapper<CommodityComment>().eq("parent_id", 0)
        .eq("commodity_id", id)
        .eq("publish_status", 1)
        .eq("delete_status", 0));
        List<CommodityCommentVo> commentVoList = new ArrayList<>();
        if (commodityComments.size() == 0) {
            return commentVoList;
        }

        commentVoList = convert(commodityComments);

        List<CommodityComment> listChild = commodityCommentMapper.selectList(new QueryWrapper<CommodityComment>().ne("parent_id", 0)
        .eq("commodity_id", id)
                .eq("publish_status", 1)
                .eq("delete_status", 0));
        if (listChild.size() == 0) {
            return commentVoList;
        }

        List<CommodityCommentVo> list = convert(listChild);

        for (CommodityCommentVo commentVo : commentVoList) {
            List<CommodityCommentVo> commodityCommentVos = new ArrayList<>();
            commentVo.setChild(commodityCommentVos);
            for (CommodityCommentVo commodityCommentVo : list) {
                if (commodityCommentVo.getParentId().equals(commentVo.getId())) {
                    commentVo.getChild().add(commodityCommentVo);
                }
            }
        }

        for (CommodityCommentVo commentVo : commentVoList) {
            commentVo.setChild(commentVo.getChild().stream().sorted(Comparator.comparing(CommodityCommentVo::getCreateTime)).collect(Collectors.toList()));
        }

        System.out.println(commentVoList);

        return commentVoList;
    }
}
