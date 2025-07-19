package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TOwnCoin;

/**
 * 发币Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-18
 */
public interface TOwnCoinMapper extends BaseMapper<TOwnCoin>
{
    /**
     * 查询发币
     * 
     * @param id 发币主键
     * @return 发币
     */
    public TOwnCoin selectTOwnCoinById(Long id);

    /**
     * 查询发币列表
     * 
     * @param tOwnCoin 发币
     * @return 发币集合
     */
    public List<TOwnCoin> selectTOwnCoinList(TOwnCoin tOwnCoin);

    /**
     * 新增发币
     * 
     * @param tOwnCoin 发币
     * @return 结果
     */
    public int insertTOwnCoin(TOwnCoin tOwnCoin);

    /**
     * 修改发币
     * 
     * @param tOwnCoin 发币
     * @return 结果
     */
    public int updateTOwnCoin(TOwnCoin tOwnCoin);

    /**
     * 删除发币
     * 
     * @param id 发币主键
     * @return 结果
     */
    public int deleteTOwnCoinById(Long id);

    /**
     * 批量删除发币
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOwnCoinByIds(Long[] ids);
}
