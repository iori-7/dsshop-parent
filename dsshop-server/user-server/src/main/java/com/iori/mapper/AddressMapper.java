package com.iori.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iori.bean.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}
