package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.ShipperEntity;
import shop.biday.model.repository.ShipperRepository;
import shop.biday.service.PaymentService;
import shop.biday.service.ShipperService;
import shop.biday.utils.UserInfoUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipperServiceImpl implements ShipperService {

    private final PaymentService paymentService;
    private final ShipperRepository shipperRepository;
    private final UserInfoUtils userInfoUtils;

    @Override
    public List<ShipperModel> findAll() {
        log.info("Find all shippers");
        return shipperRepository.findAll()
                .stream()
                .map(ShipperModel::of)
                .toList();
    }

    @Override
    public ShipperModel findById(Long id) {
        log.info("Finding shipper by id: {}", id);
        return shipperRepository.findById(id)
                .map(ShipperModel::of)
                .orElseThrow(() -> {
                    log.error("Shipper not found for id: {}", id);
                    return new IllegalArgumentException("잘못된 요청입니다.");
                });
    }

    @Override
    public ShipperEntity save(String userInfo, ShipperModel shipper) {
        log.info("Saving shipper started");
        return validateUser(userInfo)
                .map(user -> {
                    ShipperEntity savedShipper = createShipperEntity(shipper);
                    log.debug("Shipper saved successfully: {}", savedShipper.getId());
                    return shipperRepository.save(savedShipper);
                })
                .orElseThrow(() -> {
                    log.error("Save Shipper failed: User does not have permission");
                    return new RuntimeException("Save Shipper failed: User does not have permission");
                });
    }

    @Override
    public ShipperEntity update(String userInfo, ShipperModel shipper) {
        log.info("Update shipper started");
        return validateUser(userInfo)
                .filter(t->{
                    boolean exists = shipperRepository.existsById(shipper.getId());
                    if(!exists) {
                        log.error("Not found shipper: {}", shipper.getId());
                        throw new IllegalArgumentException("Not found shipper");
                    }
                    return exists;
                })
                .map(t->{
                    ShipperEntity updatedShipper = createShipperEntity(shipper);
                    updatedShipper.setId(shipper.getId());
                    log.debug("Shipper updated successfully: {}", updatedShipper.getId());
                    return shipperRepository.save(updatedShipper);
                })
                .orElseThrow(()-> new RuntimeException("Update Shipper failed: Shipper not found or user does not have permission"));
    }

    @Override
    public String deleteById(String userInfo, Long id) {
        log.info("Deleting shipper started for id: {}", id);
        return validateUser(userInfo)
                .filter(user -> shipperRepository.existsById(id))
                .map(user -> {
                    shipperRepository.deleteById(id);
                    log.debug("Shipper deleted successfully: {}", id);
                    return "배송지 삭제 성공";
                })
                .orElseGet(() -> {
                    log.error("Delete Shipper failed: User does not have permission or shipper not found");
                    return "유효하지 않은 사용자: 판매자 권한이 필요합니다";
                });
    }

    private Optional<String> validateUser(String userInfoHeader) {
        log.info("Validating user: {}", userInfoHeader);
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfoHeader);
        return Optional.ofNullable(userInfoModel.getUserRole())
                .filter(role -> role.equalsIgnoreCase("ROLE_SELLER"))
                .or(() -> {
                    log.error("User does not have role SELLER: {}", userInfoModel.getUserRole());
                    return Optional.empty();
                });
    }

    private ShipperEntity createShipperEntity(ShipperModel shipper) {
        return ShipperEntity.builder()
                .payment(paymentService.findById(shipper.getPaymentId()))
                .carrier(shipper.getCarrier())
                .trackingNumber(shipper.getTrackingNumber())
                .shipmentDate(shipper.getShipmentDate())
                .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                .deliveryAddress(shipper.getDeliveryAddress())
                .status(shipper.getStatus())
                .deliveryAddress(shipper.getDeliveryAddress())
                .build();
    }
}
