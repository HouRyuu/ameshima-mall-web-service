package com.ameshima.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.ameshima.common.constants.CommonErrResult;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.user.entity.dto.AddressDTO;
import com.ameshima.user.entity.po.AddressPO;
import com.ameshima.user.mapper.AddressMapper;
import com.ameshima.user.service.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Resource
    private AddressMapper addressMapper;

    @Override
    public PublicResult<Integer> save(AddressDTO address, int accountId) {
        this.validAddress(address);
        address.setAccountId(accountId);
        try {
            if (address.getId() == null) {
                if (addressMapper.createAddr(address) > 0) {
                    return PublicResult.success(address.getId());
                } else {
                    return PublicResult.error(CommonErrResult.ERR_REQUEST);
                }
            } else {
                AddressPO addressPO = convertDtoToPo(address);
                Example example = new Example(AddressPO.class);
                example.and().andEqualTo("id", address.getId())
                        .andEqualTo("accountId", accountId)
                        .andEqualTo("isDelete", MallConstant.NO);
                if (addressMapper.updateByExampleSelective(addressPO, example) == 1) {
                    return PublicResult.success(address.getId());
                } else {
                    return PublicResult.error(CommonErrResult.ERR_REQUEST);
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("AccountId=>%1$dはAddress=>%2$sの保存がエラーになった", accountId, JSON.toJSONString(address)), e);
        }
        return PublicResult.error();
    }

    @Override
    public PublicResult<?> remove(int id, int accountId) {
        Example example = new Example(AddressPO.class);
        example.createCriteria().andEqualTo("id", id)
                .andEqualTo("accountId", accountId)
                .andEqualTo("isDefault", MallConstant.NO)
                .andEqualTo("isDelete", MallConstant.NO);
        AddressPO addressPO = new AddressPO();
        addressPO.setIsDelete(MallConstant.YES);
        try {
            if (addressMapper.updateByExampleSelective(addressPO, example) == 1) {
                return PublicResult.success();
            } else {
                return PublicResult.error(CommonErrResult.ERR_REQUEST);
            }
        } catch (
                Exception e) {
            LOGGER.error(String.format("AccountId=>%1$dはAddressId=>%2$dの削除がエラーになった", accountId, id), e);
        }
        return PublicResult.error();
    }

    @Override
    public List<AddressDTO> findList(int accountId) {
        return addressMapper.findAll(accountId);
    }

    @Override
    public PublicResult<?> setDefault(int id, int accountId) {
        try {
            if (addressMapper.setDefault(id, accountId) > 0) {
                return PublicResult.success();
            }
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        } catch (
                Exception e) {
            LOGGER.error(String.format("AccountId=>%1$dはAddressId=>%2$dをデフォルトにするのがエラーになった", accountId, id), e);
        }
        return PublicResult.error();
    }

    private void validAddress(AddressDTO address) {
        Assert.isTrue(address != null && !StringUtils.isAnyBlank(address.getProvince(), address.getProvinceCode(),
                address.getCity(), address.getCityCode(), address.getDistrict(), address.getDistrictCode(),
                address.getDetailedAddress()), MallConstant.PARAM_ERR_MSG);
    }

    private AddressPO convertDtoToPo(AddressDTO address) {
        AddressPO addressPO = new AddressPO();
        addressPO.setAccountId(address.getAccountId());
        addressPO.setName(address.getName());
        addressPO.setPhone(address.getPhone());
        addressPO.setProvinceCode(address.getProvinceCode());
        addressPO.setProvince(address.getProvince());
        addressPO.setCityCode(address.getCityCode());
        addressPO.setCity(address.getCity());
        addressPO.setDistrictCode(address.getDistrictCode());
        addressPO.setDistrict(address.getDistrict());
        addressPO.setDetailedAddress(address.getDetailedAddress());
        return addressPO;
    }
}
