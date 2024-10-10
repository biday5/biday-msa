package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.PaymentEntity;
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
        log.info("Find shipper by id: {}", id);
        return ShipperModel.of(shipperRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다.")));
    }

    @Override
    public ShipperEntity save(String userInfo, ShipperModel shipper) {
        log.info("Save shipper started");
        return validateUser(userInfo)
                .map(t-> {
                    ShipperEntity savedShipper = createShipperEntity(shipper);
                    log.debug("Shipper saved successfully: {}", savedShipper.getId());
                    return shipperRepository.save(savedShipper);
        })
    }

    @Override
    public ShipperEntity update(String userInfo, ShipperModel shipper) {
        log.info("Update shipper started");
        validateUser(userInfo);

        if (!shipperRepository.existsById(shipper.getId())) {
            log.error("Not found shipper: {}", shipper.getId());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        return shipperRepository.save(ShipperEntity.builder()
                .id(shipper.getId())
                .payment(paymentService.findById(shipper.getPaymentId()))
                .carrier(shipper.getCarrier())
                .trackingNumber(shipper.getTrackingNumber())
                .shipmentDate(shipper.getShipmentDate())
                .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                .deliveryAddress(shipper.getDeliveryAddress())
                .status(shipper.getStatus())
                .deliveryAddress(shipper.getDeliveryAddress())
                .createdAt(shipper.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Override
    public String deleteById(String userInfo, Long id) {
        log.info("Delete shipper started for id: {}", id);
        validateUser(userInfo);

        if (!shipperRepository.existsById(id)) {
            log.error("배송 정보를 찾을수 없습니다: {}", id);
            return "배송지 삭제 실패";
        }

        shipperRepository.deleteById(id);
        return "배송지 삭제 성공";
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

    private ShipperEntity ShipperEntity.builder()
                .payment(paymentService.findById(shipper.getPaymentId()))
            .carrier(shipper.getCarrier())
            .trackingNumber(shipper.getTrackingNumber())
            .shipmentDate(shipper.getShipmentDate())
            .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
            .deliveryAddress(shipper.getDeliveryAddress())
            .status("준비중")
                .deliveryAddress(shipper.getDeliveryAddress())
            .createdAt(LocalDateTime.now())
            .build()
}
