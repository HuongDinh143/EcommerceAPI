package com.ra.service.address;

import com.ra.model.dto.response.AddressResponseDto;
import com.ra.model.entity.Address;

import java.util.List;

public interface AddressService {
    void deleteAddressById(Long id,Long userId);
    List<AddressResponseDto> getAddressByUserId(Long userId);
    AddressResponseDto getAddressById(Long id,Long userId);
}
