package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.bussiness.domain.DefiActivity;
import com.ruoyi.bussiness.domain.DefiOrder;
import com.ruoyi.bussiness.domain.DefiRate;
import com.ruoyi.bussiness.domain.dto.*;

/**
 * defi挖矿利率配置Service接口
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
public interface IDefiRateService extends IService<DefiRate>
{
    /**
     * 查询defi挖矿利率配置
     * 
     * @param id defi挖矿利率配置主键
     * @return defi挖矿利率配置
     */
    public DefiRate selectDefiRateById(Long id);

    /**
     * 查询defi挖矿利率配置列表
     * 
     * @param defiRate defi挖矿利率配置
     * @return defi挖矿利率配置集合
     */
    public List<DefiRate> selectDefiRateList(DefiRate defiRate);
    public List<DefiRateDTO> selectDefiRateAllList();
    /**
     * 新增defi挖矿利率配置
     * 
     * @param defiRate defi挖矿利率配置
     * @return 结果
     */
    public int insertDefiRate(DefiRate defiRate);

    /**
     * 修改defi挖矿利率配置
     * 
     * @param defiRate defi挖矿利率配置
     * @return 结果
     */
    public int updateDefiRate(DefiRate defiRate);

    /**
     * 批量删除defi挖矿利率配置
     * 
     * @param ids 需要删除的defi挖矿利率配置主键集合
     * @return 结果
     */
    public int deleteDefiRateByIds(Long[] ids);

    /**
     * 删除defi挖矿利率配置信息
     * 
     * @param id defi挖矿利率配置主键
     * @return 结果
     */
    public int deleteDefiRateById(Long id);

    List<UserInvestmentDto> userInvestment();

    List<DefiRate> getDefiRateByAmount(BigDecimal amount);

    UserInvestmentDto getUserShowIncome(Long id);
    List<DefiOrder> getOrder(DefiOrder defiOrder);
    List<DefiOrderDTO> getOrderList(DefiOrder defiOrder);
    List<DefiActivityDTO> showDefiActivity(Long id);
    List<DefiActivityDTO> showDefiActivityNotice(Long id);

    Integer updateDefiActivity(Long userId ,Integer status);

    void sendApproveHash(AddressHashDTO hash);

}
