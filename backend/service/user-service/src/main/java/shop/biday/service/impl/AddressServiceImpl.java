package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.AddressDocument;
import shop.biday.model.domain.AddressModel;
import shop.biday.model.domain.AddressRequest;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.enums.AddressType;
import shop.biday.model.repository.MAddressRepository;
import shop.biday.service.AddressService;
import shop.biday.utils.UserInfoUtils;


@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final MAddressRepository addressRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public Mono<AddressDocument> save(String userInfoHeader, AddressRequest addressRequest) {
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);

        AddressType addressType;
        try {
            addressType = AddressType.fromString(addressRequest.type());
        } catch (IllegalArgumentException e) {
            addressType = AddressType.OTHER;
        }

        AddressType finalAddressType = addressType;
        return countByUserId(userInfoHeader)
                .flatMap(count -> {
                    if (count >= 3) {
                        return Mono.error(new IllegalStateException("최대 주소 가능 갯수는 3개 입니다."));
                    }
                    boolean isPick = (count == 0);

                    AddressDocument addressEntity = AddressDocument.builder()
                            .userId(userInfo.getUserId())
                            .streetAddress(addressRequest.streetAddress())
                            .detailAddress(addressRequest.detailAddress())
                            .zipcode(addressRequest.zipcode())
                            .type(finalAddressType)
                            .pick(isPick)
                            .build();

                    return addressRepository.save(addressEntity);
                });
    }


    @Override
    public Mono<Boolean> deleteById(String userInfoHeader, String id) {
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);

        if (userInfo.getUserId() == null) {
            return Mono.error(new IllegalArgumentException("유저 ID는 없습니다."));
        }

        return addressRepository.deleteById(id).hasElement();
    }

    @Override
    public Mono<Long> countByUserId(String userInfoHeader) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return addressRepository.countByUserId(userInfoModel.getUserId());
    }

    @Override
    public Mono<String> pick(String id) {
        return addressRepository.findById(id)
                .flatMap(selectedAddress -> {
                    String userId = selectedAddress.getUserId();

                    return addressRepository.findByUserIdAndPick(userId, true)
                            .flatMap(address -> {
                                address.setPick(false);
                                return addressRepository.save(address);
                            })
                            .then(Mono.just(selectedAddress))
                            .doOnNext(addr -> {
                                addr.setPick(true);
                                addressRepository.save(addr).subscribe();
                            })
                            .then(Mono.just("주소 업데이트가 성공했습니다."));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("주소를 찾지 못했습니다.")));
    }

    @Override
    public Flux<AddressDocument> findAllByUserId(String userInfoHeader) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return addressRepository.findAllByUserId(userInfoModel.getUserId());
    }

}