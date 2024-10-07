package shop.biday.service;

public interface WishService {
    boolean toggleWish(String email, Long productId);

    void deleteWish(String email, Long productId);

    void insertWish(String email, Long productId);

    boolean isWish(String email, Long productId);

}
