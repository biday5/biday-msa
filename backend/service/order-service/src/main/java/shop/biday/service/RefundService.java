package shop.biday.service;

import shop.biday.model.dto.RefundRequest;

public interface RefundService {

    boolean cancel(Long id, RefundRequest cancelRequest);
}
