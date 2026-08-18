package com.example.ecommerce.service;

import com.example.ecommerce.dto.address.AddressRequest;
import com.example.ecommerce.dto.address.AddressResponse;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.UserNotFoundException;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    public AddressResponse createAddress (
            UUID userId,
            AddressRequest request) throws RuntimeException{
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent())
        {
            throw new UserNotFoundException("User with given UUID is not found..");
        }
        Address address = Address.builder()
                .user(user.get())
                .addressLine1(request.addressLine1())
                .addressLine2(request.addressLine2())
                .city(request.city())
                .state(request.state())
                .postalCode(request.postalCode())
                .country(request.country())
                .addressType(request.addressType())
                .defaultAddress(request.defaultAddress())
                .build();

        if (request.defaultAddress()) {
            removeExistingDefaultAddress(userId);
        }

        return mapToResponse(
                addressRepository.save(address)
        );
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses (
            UUID userId) throws RuntimeException{

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent())
        {
            throw new UserNotFoundException("User with given UUID is not found..");
        }

        return addressRepository
                .findByUserId(userId)
                .stream()
                .map(address -> mapToResponse(address))
                .toList();
    }

    @Transactional(readOnly = true)
    public AddressResponse getAddress(
            UUID userId,
            UUID addressId) throws RuntimeException {

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent())
        {
            throw new UserNotFoundException("User with given UUID is not found..");
        }
        Address address =
                addressRepository
                        .findByIdAndUserId(addressId, userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"
                                ));

        return mapToResponse(address);
    }

    public AddressResponse updateAddress(
            UUID userId,
            UUID addressId,
            AddressRequest request) throws RuntimeException{
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent())
        {
            throw new UserNotFoundException("User with given UUID is not found..");
        }
        Address address =
                addressRepository
                        .findByIdAndUserId(addressId, userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"
                                ));

        if (request.defaultAddress()) {
            removeExistingDefaultAddress(userId);
        }
        address.setAddressLine1(request.addressLine1());
        address.setAddressLine2(request.addressLine2());
        address.setCity(request.city());
        address.setState(request.state());
        address.setPostalCode(request.postalCode());
        address.setCountry(request.country());
        address.setAddressType(request.addressType());
        address.setDefaultAddress(request.defaultAddress());
        address.setUpdatedAt(Instant.now());
        return mapToResponse(address);
    }

    public void deleteAddress(
            UUID userId,
            UUID addressId) throws RuntimeException{
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent())
        {
            throw new UserNotFoundException("User with given UUID is not found..");
        }
        Address address =
                addressRepository
                        .findByIdAndUserId(addressId, userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"
                                ));

        addressRepository.delete(address);
    }

    public void makeDefault(
            UUID userId,
            UUID addressId) throws RuntimeException{

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent())
        {
            throw new UserNotFoundException("User with given UUID is not found..");
        }
        Address address =
                addressRepository
                        .findByIdAndUserId(addressId, userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"
                                ));

        removeExistingDefaultAddress(userId);

        address.setDefaultAddress(true);
        address.setUpdatedAt(Instant.now());
    }

    private void removeExistingDefaultAddress(
            UUID userId) {

        List<Address> addresses =
                addressRepository.findByUserId(userId);

        addresses.forEach(address ->
                address.setDefaultAddress(false)
        );
    }

    private User getUser(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
    }

    private AddressResponse mapToResponse(Address address) {

        return new AddressResponse(
                address.getId(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry(),
                address.getAddressType(),
                address.isDefaultAddress()
        );
    }
}