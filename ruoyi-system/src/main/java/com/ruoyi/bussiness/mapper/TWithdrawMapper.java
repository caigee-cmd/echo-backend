package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TWithdraw;
import com.ruoyi.bussiness.domain.vo.WithdrawFreezeVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 用户提现Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
public interface TWithdrawMapper extends BaseMapper<TWithdraw>
{
    /**
     * 查询用户提现
     * 
     * @param id 用户提现主键
     * @return 用户提现
     */
    public TWithdraw selectTWithdrawById(Long id);

    /**
     * 查询用户提现列表
     * 
     * @param tWithdraw 用户提现
     * @return 用户提现集合
     */
    public List<TWithdraw> selectTWithdrawList(TWithdraw tWithdraw);

    /**
     * 新增用户提现
     * 
     * @param tWithdraw 用户提现
     * @return 结果
     */
    public int insertTWithdraw(TWithdraw tWithdraw);

    /**
     * 修改用户提现
     * 
     * @param tWithdraw 用户提现
     * @return 结果
     */
    public int updateTWithdraw(TWithdraw tWithdraw);

    /**
     * 删除用户提现
     * 
     * @param id 用户提现主键
     * @return 结果
     */
    public int deleteTWithdrawById(Long id);

    /**
     * 批量删除用户提现
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTWithdrawByIds(Long[] ids);

    List<TWithdraw> selectTWithdrawVoice(String parentId);

    BigDecimal getAllWithdraw(@Param("parentId") String parentId, @Param("type")Integer type);

    List<WithdrawFreezeVO> selectFreezeList(TWithdraw appWithdraw);

    List<Map<String, Object>>  getWeekWithdraw(String parentId);

    List<Map<String, Object>> getWeekFailedWithdraw(String parentId);

    Integer getCountByUserId(Long userid);
}
