package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.THomeSetter;

/**
 * 规则说明Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-19
 */
public interface THomeSetterMapper extends BaseMapper<THomeSetter>
{
    /**
     * 查询规则说明
     * 
     * @param id 规则说明主键
     * @return 规则说明
     */
    public THomeSetter selectTHomeSetterById(Long id);

    /**
     * 查询规则说明列表
     * 
     * @param tHomeSetter 规则说明
     * @return 规则说明集合
     */
    public List<THomeSetter> selectTHomeSetterList(THomeSetter tHomeSetter);

    /**
     * 新增规则说明
     * 
     * @param tHomeSetter 规则说明
     * @return 结果
     */
    public int insertTHomeSetter(THomeSetter tHomeSetter);

    /**
     * 修改规则说明
     * 
     * @param tHomeSetter 规则说明
     * @return 结果
     */
    public int updateTHomeSetter(THomeSetter tHomeSetter);

    /**
     * 删除规则说明
     * 
     * @param id 规则说明主键
     * @return 结果
     */
    public int deleteTHomeSetterById(Long id);

    /**
     * 批量删除规则说明
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTHomeSetterByIds(Long[] ids);
}
