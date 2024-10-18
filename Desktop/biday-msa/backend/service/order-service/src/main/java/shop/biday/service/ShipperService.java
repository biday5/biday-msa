package shop.biday.service;

import org.springframework.http.ResponseEntity;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.entity.ShipperEntity;

import java.util.List;

public interface ShipperService {

    ResponseEntity<List<ShipperModel>> findAll();

    ResponseEntity<ShipperModel> findById(Long id);

    ResponseEntity<ShipperEntity> save(String token, ShipperModel brand);

    ResponseEntity<ShipperEntity> update(String token, ShipperModel brand);

    ResponseEntity<String> deleteById(String token, Long id);
}
