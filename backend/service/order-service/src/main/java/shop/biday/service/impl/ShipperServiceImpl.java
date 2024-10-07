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
        getUserInfoModel(userInfo);

        PaymentEntity payment = paymentService.findById(shipper.getPaymentId());

        return shipperRepository.save(ShipperEntity.builder()
                .payment(payment)
                .carrier(shipper.getCarrier())
                .trackingNumber(shipper.getTrackingNumber())
                .shipmentDate(shipper.getShipmentDate())
                .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                .deliveryAddress(shipper.getDeliveryAddress())
                .status("준비중")
                .deliveryAddress(shipper.getDeliveryAddress())
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Override
    public ShipperEntity update(String userInfo, ShipperModel shipper) {
        log.info("Update shipper started");
        getUserInfoModel(userInfo);

        PaymentEntity payment = paymentService.findById(shipper.getPaymentId());

        if (!shipperRepository.existsById(shipper.getId())) {
            log.error("Not found shipper: {}", shipper.getId());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        return shipperRepository.save(ShipperEntity.builder()
                .id(shipper.getId())
                .payment(payment)
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
        getUserInfoModel(userInfo);

        if (!shipperRepository.existsById(id)) {
            log.error("배송 정보를 찾을수 없습니다: {}", id);
            return "배송지 삭제 실패";
        }

        shipperRepository.deleteById(id);
        return "배송지 삭제 성공";
    }

    private void getUserInfoModel(String userInfo) {
        UserInfoModel userInfoModel = userInfoUtils.extractUserInfo(userInfo);
        if (!userInfoModel.getUserRole().equalsIgnoreCase("ROLE_SELLER")) {
            throw new IllegalArgumentException("사용자 정보가 올바르지 않습니다.");
        }
    }
}
