package com.tmall.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.PublicResult;
import com.tmall.user.entity.dto.AddressDTO;
import com.tmall.user.entity.po.AddressPO;
import com.tmall.user.mapper.AddressMapper;
import com.tmall.user.service.AddressService;

import tk.mybatis.mapper.entity.Example;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    public PublicResult save(AddressDTO address, int accountId) {
        this.validAddress(address);
        AddressPO addressPO = convertDtoToPo(address);
        Example example = new Example(AddressPO.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("accountId", accountId).andEqualTo("isDelete",
                TmallConstant.YES);
        if (address.getId() == null) {
            if (addressMapper.selectCountByExample(example) == 0) {
                addressPO.setIsDefault(TmallConstant.YES);
            }
            addressPO.setAccountId(accountId);
            addressMapper.insertSelective(addressPO);
            return PublicResult.success();
        }
        criteria.andEqualTo("id", address.getId());
        addressMapper.updateByExampleSelective(addressPO, example);
        return PublicResult.success();
    }

    @Override
    public PublicResult remove(int id, int accountId) {
        Example example = new Example(AddressPO.class);
        example.createCriteria().andEqualTo("id", id).andEqualTo("accountId", accountId).andEqualTo("isDelete",
                TmallConstant.YES);
        AddressPO addressPO = new AddressPO();
        addressPO.setIsDelete(TmallConstant.YES);
        addressMapper.updateByExampleSelective(addressPO, example);
        return PublicResult.success();
    }

    @Override
    public List<AddressDTO> findList(int accountId) {
        return addressMapper.findAll(accountId);
    }

    private void validAddress(AddressDTO address) {
        Assert.isTrue(address != null && !StringUtils.isAnyBlank(address.getProvince(), address.getProvinceCode(),
                address.getCity(), address.getCityCode(), address.getDistrict(), address.getDistrictCode(),
                address.getDetailedAddress()), TmallConstant.PARAM_ERR_MSG);
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
