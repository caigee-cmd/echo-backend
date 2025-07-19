package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TContractPosition;

/**
 * U本位持仓表Mapper接口
 * 
 * @author michael
 * @date 2023-07-20
 */
public interface TContractPositionMapper extends BaseMapper<TContractPosition>
{
    /**
     * 查询U本位持仓表
     * 
     * @param id U本位持仓表主键
     * @return U本位持仓表
     */
    public TContractPosition selectTContractPositionById(Long id);

    /**
     * 查询U本位持仓表列表
     * 
     * @param tContractPosition U本位持仓表
     * @return U本位持仓表集合
     */
    public List<TContractPosition> selectTContractPositionList(TContractPosition tContractPosition);

    /**
     * 新增U本位持仓表
     * 
     * @param tContractPosition U本位持仓表
     * @return 结果
     */
    public int insertTContractPosition(TContractPosition tContractPosition);

    /**
     * 修改U本位持仓表
     * 
     * @param tContractPosition U本位持仓表
     * @return 结果
     */
    public int updateTContractPosition(TContractPosition tContractPosition);

    /**
     * 删除U本位持仓表
     * 
     * @param id U本位持仓表主键
     * @return 结果
     */
    public int deleteTContractPositionById(Long id);

    /**
     * 批量删除U本位持仓表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTContractPositionByIds(Long[] ids);
}
