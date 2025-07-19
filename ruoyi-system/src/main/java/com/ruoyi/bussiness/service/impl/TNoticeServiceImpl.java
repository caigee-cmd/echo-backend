package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

import com.ruoyi.common.enums.NoticeTypeEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TNoticeMapper;
import com.ruoyi.bussiness.domain.TNotice;
import com.ruoyi.bussiness.service.ITNoticeService;

/**
 * 通知公告Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-20
 */
@Service
public class TNoticeServiceImpl extends ServiceImpl<TNoticeMapper,TNotice> implements ITNoticeService
{
    @Autowired
    private TNoticeMapper tNoticeMapper;

    /**
     * 查询通知公告
     * 
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    @Override
    public TNotice selectTNoticeByNoticeId(Long noticeId)
    {
        return tNoticeMapper.selectTNoticeByNoticeId(noticeId);
    }

    /**
     * 查询通知公告列表
     * 
     * @param tNotice 通知公告
     * @return 通知公告
     */
    @Override
    public List<TNotice> selectTNoticeList(TNotice tNotice)
    {
        return tNoticeMapper.selectTNoticeList(tNotice);
    }

    /**
     * 新增通知公告
     * 
     * @param tNotice 通知公告
     * @return 结果
     */
    @Override
    public int insertTNotice(TNotice tNotice)
    {
        tNotice.setNoticeType(NoticeTypeEnum.valueOf(tNotice.getKey()).getCode());

        if (StringUtils.isNotBlank(tNotice.getModelKey())){
            tNotice.setModelType(NoticeTypeEnum.ChildrenEnum.valueOf(tNotice.getModelKey()).getCode());
        }
        tNotice.setCreateTime(DateUtils.getNowDate());
        return tNoticeMapper.insertTNotice(tNotice);
    }

    /**
     * 修改通知公告
     * 
     * @param tNotice 通知公告
     * @return 结果
     */
    @Override
    public int updateTNotice(TNotice tNotice)
    {
        tNotice.setNoticeType(NoticeTypeEnum.valueOf(tNotice.getKey()).getCode());

        if (StringUtils.isNotBlank(tNotice.getModelKey())){
            tNotice.setModelType(NoticeTypeEnum.ChildrenEnum.valueOf(tNotice.getModelKey()).getCode());
        }
        tNotice.setUpdateTime(DateUtils.getNowDate());
        return tNoticeMapper.updateTNotice(tNotice);
    }

    /**
     * 批量删除通知公告
     * 
     * @param noticeIds 需要删除的通知公告主键
     * @return 结果
     */
    @Override
    public int deleteTNoticeByNoticeIds(Long[] noticeIds)
    {
        return tNoticeMapper.deleteTNoticeByNoticeIds(noticeIds);
    }

    /**
     * 删除通知公告信息
     * 
     * @param noticeId 通知公告主键
     * @return 结果
     */
    @Override
    public int deleteTNoticeByNoticeId(Long noticeId)
    {
        return tNoticeMapper.deleteTNoticeByNoticeId(noticeId);
    }
}
