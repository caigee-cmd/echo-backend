package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.DefiActivity;
import org.apache.ibatis.annotations.Param;

/**
 * 空投活动Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
public interface DefiActivityMapper extends BaseMapper<DefiActivity>
{
    /**
     * 查询空投活动
     * 
     * @param id 空投活动主键
     * @return 空投活动
     */
    public DefiActivity selectDefiActivityById(Long id);

    /**
     * 查询空投活动列表
     * 
     * @param defiActivity 空投活动
     * @return 空投活动集合
     */
    public List<DefiActivity> selectDefiActivityList(DefiActivity defiActivity);

    /**
     * 新增空投活动
     * 
     * @param defiActivity 空投活动
     * @return 结果
     */
    public int insertDefiActivity(DefiActivity defiActivity);

    /**
     * 修改空投活动
     * 
     * @param defiActivity 空投活动
     * @return 结果
     */
    public int updateDefiActivity(DefiActivity defiActivity);

    /**
     * 删除空投活动
     * 
     * @param id 空投活动主键
     * @return 结果
     */
    public int deleteDefiActivityById(Long id);

    /**
     * 批量删除空投活动
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDefiActivityByIds(Long[] ids);

    List<DefiActivity> showDefiActivity(@Param("userId") Long userId, @Param("status") Integer status);

}
