package com.ruoyi.bussiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.THelpCenter;

import java.util.List;

/**
 * 帮助中心Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
public interface THelpCenterMapper extends BaseMapper<THelpCenter>
{
    /**
     * 查询帮助中心
     * 
     * @param id 帮助中心主键
     * @return 帮助中心
     */
    public THelpCenter selectTHelpCenterById(Long id);

    /**
     * 查询帮助中心列表
     * 
     * @param tHelpCenter 帮助中心
     * @return 帮助中心集合
     */
    public List<THelpCenter> selectTHelpCenterList(THelpCenter tHelpCenter);

    /**
     * 新增帮助中心
     * 
     * @param tHelpCenter 帮助中心
     * @return 结果
     */
    public int insertTHelpCenter(THelpCenter tHelpCenter);

    /**
     * 修改帮助中心
     * 
     * @param tHelpCenter 帮助中心
     * @return 结果
     */
    public int updateTHelpCenter(THelpCenter tHelpCenter);

    /**
     * 删除帮助中心
     * 
     * @param id 帮助中心主键
     * @return 结果
     */
    public int deleteTHelpCenterById(Long id);

    /**
     * 批量删除帮助中心
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTHelpCenterByIds(Long[] ids);

    List<THelpCenter> getCenterList(THelpCenter tHelpCenter);
}
