package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.THomeSetterMapper;
import com.ruoyi.bussiness.domain.THomeSetter;
import com.ruoyi.bussiness.service.ITHomeSetterService;

/**
 * 规则说明Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-19
 */
@Service
public class THomeSetterServiceImpl extends ServiceImpl<THomeSetterMapper,THomeSetter> implements ITHomeSetterService
{
    @Autowired
    private THomeSetterMapper tHomeSetterMapper;

    /**
     * 查询规则说明
     * 
     * @param id 规则说明主键
     * @return 规则说明
     */
    @Override
    public THomeSetter selectTHomeSetterById(Long id)
    {
        return tHomeSetterMapper.selectTHomeSetterById(id);
    }

    /**
     * 查询规则说明列表
     * 
     * @param tHomeSetter 规则说明
     * @return 规则说明
     */
    @Override
    public List<THomeSetter> selectTHomeSetterList(THomeSetter tHomeSetter)
    {
        return tHomeSetterMapper.selectTHomeSetterList(tHomeSetter);
    }

    /**
     * 新增规则说明
     * 
     * @param tHomeSetter 规则说明
     * @return 结果
     */
    @Override
    public int insertTHomeSetter(THomeSetter tHomeSetter)
    {
        tHomeSetter.setCreateTime(DateUtils.getNowDate());
        tHomeSetter.setHomeType(1);
        return tHomeSetterMapper.insertTHomeSetter(tHomeSetter);
    }

    /**
     * 修改规则说明
     * 
     * @param tHomeSetter 规则说明
     * @return 结果
     */
    @Override
    public int updateTHomeSetter(THomeSetter tHomeSetter)
    {
        return tHomeSetterMapper.updateTHomeSetter(tHomeSetter);
    }

    /**
     * 批量删除规则说明
     * 
     * @param ids 需要删除的规则说明主键
     * @return 结果
     */
    @Override
    public int deleteTHomeSetterByIds(Long[] ids)
    {
        return tHomeSetterMapper.deleteTHomeSetterByIds(ids);
    }

    /**
     * 删除规则说明信息
     * 
     * @param id 规则说明主键
     * @return 结果
     */
    @Override
    public int deleteTHomeSetterById(Long id)
    {
        return tHomeSetterMapper.deleteTHomeSetterById(id);
    }
}
