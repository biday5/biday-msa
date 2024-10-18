package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.entity.ShipperEntity;
import shop.biday.model.repository.ShipperRepository;
import shop.biday.service.PaymentService;
import shop.biday.service.ShipperService;
import shop.biday.utils.UserInfoUtils;

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
    public ResponseEntity<List<ShipperModel>> findAll() {
        log.info("Find all shippers");
        List<ShipperModel> shippers = shipperRepository.findAll()
                .stream()
                .map(ShipperModel::of)
                .toList();

        return shippers.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.ok(shippers);
    }

    @Override
    public ResponseEntity<ShipperModel> findById(Long id) {
        log.info("Finding shipper by id: {}", id);
        return shipperRepository.findById(id)
                .map(shipper -> ResponseEntity.ok(ShipperModel.of(shipper)))
                .orElseGet(() -> {
                    log.error("Shipper not found for id: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    @Override
    public ResponseEntity<ShipperEntity> save(String userInfo, ShipperModel shipper) {
        log.info("Saving shipper started");
        return validateUser(userInfo)
                .map(user -> {
                    ShipperEntity savedShipper = createShipperEntity(shipper);
                    log.debug("Shipper saved successfully: {}", savedShipper.getId());
                    return ResponseEntity.ok(shipperRepository.save(savedShipper));
                })
                .orElseGet(() -> {
                    log.error("Save Shipper failed: User does not have permission");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(null);
                });
    }

    @Override
    public ResponseEntity<ShipperEntity> update(String userInfo, ShipperModel shipper) {
        log.info("Update shipper started");

        return validateUser(userInfo)
                .map(user -> {
                    boolean exists = shipperRepository.existsById(shipper.getId());
                    if (!exists) {
                        log.error("Not found shipper: {}", shipper.getId());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).<ShipperEntity>body(null);
                    }

                    ShipperEntity updatedShipper = createShipperEntity(shipper);
                    updatedShipper.setId(shipper.getId());
                    log.debug("Shipper updated successfully: {}", updatedShipper.getId());
                    return ResponseEntity.ok(updatedShipper);
                })
                .orElseGet(() -> {
                    log.error("Update Shipper failed: User does not have permission");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).<ShipperEntity>body(null);
                });
    }

    @Override
    public ResponseEntity<String> deleteById(String userInfo, Long id) {
        log.info("Deleting shipper started for id: {}", id);
        return validateUser(userInfo)
                .filter(user -> shipperRepository.existsById(id))
                .map(user -> {
                    shipperRepository.deleteById(id);
                    log.debug("Shipper deleted successfully: {}", id);
                    return ResponseEntity.ok("배송지 삭제 성공"); // 200 반환
                })
                .orElseGet(() -> {
                    if (!shipperRepository.existsById(id)) {
                        log.error("Delete Shipper failed: Shipper not found for id: {}", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("배송을 찾을 수 없습니다."); // 404 반환
                    }
                    log.error("Delete Shipper failed: User does not have permission");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 사용자: 판매자 권한이 필요합니다"); // 403 반환
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
