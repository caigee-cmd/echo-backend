package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.bussiness.domain.TContractPosition;

/**
 * U本位持仓表Service接口
 * 
 * @author michael
 * @date 2023-07-20
 */
public interface ITContractPositionService extends IService<TContractPosition>
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
     * 批量删除U本位持仓表
     * 
     * @param ids 需要删除的U本位持仓表主键集合
     * @return 结果
     */
    public int deleteTContractPositionByIds(Long[] ids);

    /**
     * 删除U本位持仓表信息
     * 
     * @param id U本位持仓表主键
     * @return 结果
     */
    public int deleteTContractPositionById(Long id);

    /**
     * 全部平仓
     * @param id
     * @return
     */
    String  allClosePosition(Long id);

    /**
     * 调整保证金
     * @param id
     * @param money
     * @param flag  0 增加   1减少
     * @return
     */
    String adjustAmout(Long id, BigDecimal money, String flag);


    /**
     * 平仓交易
     * @param id
     * @return
     */
    String verifyStopPostion(Long id);

    /**
     * 通过
     * @param contractPosition
     * @return
     */
    String pass(TContractPosition  contractPosition);

    /**
     * 拒绝
     * @param contractPosition
     * @return
     */
    String reject(TContractPosition  contractPosition);


    String adjustPositionMargn(Long id,BigDecimal money);

    String adjustPositionAmout(Long id,BigDecimal money);

    String  closePosition(Long id);

    String  stopPosition(Long id);

    String stopAllPosition(Long id);
}
