package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.repository.WishRepository;
import shop.biday.service.WishService;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
@Tag(name = "wishes", description = "Wish Controller")
public class WishController {
    private final WishService wishService;
    private final WishRepository wishRepository;


    @GetMapping("/user")
    public ResponseEntity<List<?>> findByUser(@RequestHeader String userId) {
        List<?> wishList = wishRepository.findByUserId(userId);

        return (wishList == null || wishList.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(wishList);

    }

    @GetMapping
    public ResponseEntity<?> toggleWish(@RequestHeader String userId, @RequestParam Long productId) {
        return wishService.toggleWish(userId, productId)
                ? ResponseEntity.status(HttpStatus.CREATED).body("위시 생성 성공")
                : ResponseEntity.ok("위시 삭제 성공");

    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestHeader String userId, @RequestParam Long id) {
        return wishRepository.findById(id)
                .filter(wish -> wish.getUserId().equals(userId))
                .map(wish -> {
                    wishRepository.deleteById(id);
                    return ResponseEntity.ok("위시 삭제 성공");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 wishId: " + id));

    }
}