package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeCommentParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeCommentVo;

import java.util.List;

public interface CommodityDisputeCommentService {
    /**
     * 查询
     *
     * @param commodityDisputeCommentParam
     * @return
     */
    Result<List<CommodityDisputeCommentVo>> info(CommodityDisputeCommentParam commodityDisputeCommentParam);

    /**
     * 回复
     *
     * @param commodityDisputeCommentParam
     * @param <T>
     * @return
     */
    <T> Result<T> reply(CommodityDisputeCommentParam commodityDisputeCommentParam);
}
