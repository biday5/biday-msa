package shop.biday.service;

import shop.biday.model.domain.ShipperModel;
import shop.biday.model.entity.ShipperEntity;

import java.util.List;

public interface ShipperService {

    List<ShipperModel> findAll();

    ShipperModel findById(Long id);

    ShipperEntity save(String token, ShipperModel brand);

    ShipperEntity update(String token, ShipperModel brand);

    String deleteById(String token, Long id);
}
