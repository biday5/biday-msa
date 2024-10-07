package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.SellerPaymentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller-payments")
@Tag(name = "sellerPayments", description = "SellerPayment Controller")
public class SellerPaymentController {

    private final SellerPaymentService sellerPaymentService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("sellerPaymentService.findAll()");
        return ResponseEntity.ok("ok");
    }
}
