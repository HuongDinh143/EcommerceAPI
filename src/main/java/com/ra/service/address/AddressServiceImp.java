package com.ra.service.address;

import com.ra.exception.CustomException;
import com.ra.model.dto.response.AddressResponseDto;
import com.ra.model.entity.Address;
import com.ra.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImp implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Override
    public void deleteAddressById(Long id,Long userId) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy địa chỉ"));

        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException("Bạn không có quyền xóa địa chỉ này");
        }

        addressRepository.delete(address);

    }

    @Override
    public List<AddressResponseDto> getAddressByUserId(Long userId) {
        List<Address> addressList = addressRepository.findByUserId(userId);
        return addressList.stream().map(this::toDto).toList();
    }

    @Override
    public AddressResponseDto getAddressById(Long id, Long userId) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy địa chỉ"));

        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException("Bạn không c quyền truy cập địa chỉ này");
        }
        return toDto(address);
    }

    private AddressResponseDto toDto(Address address) {
        return AddressResponseDto.builder()
                .addressId(address.getId())
                .fullAddress(address.getFullAddress())
                .phone(address.getPhone())
                .receiveName(address.getReceiveName())
                .build();
    }
}
