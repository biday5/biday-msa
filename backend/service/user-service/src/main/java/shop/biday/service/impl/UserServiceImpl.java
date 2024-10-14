package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.biday.model.document.UserDocument;
import shop.biday.model.domain.UserInfoModel;
import shop.biday.model.domain.UserModel;
import shop.biday.model.domain.UserRequest;
import shop.biday.model.enums.Role;
import shop.biday.model.repository.MUserRepository;
import shop.biday.service.UserService;
import shop.biday.utils.UserInfoUtils;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final MUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoUtils userInfoUtils;

    @Override
    public Flux<UserDocument> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<UserDocument> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<UserDocument> save(UserRequest userRequest) {
        return Mono.just(userRequest)
                .map(req -> UserDocument.builder()
                        .name(req.name())
                        .email(req.email())
                        .password(passwordEncoder.encode(req.password()))
                        .phone(req.phoneNum())
                        .role(Collections.singletonList(Role.ROLE_USER))
                        .status(true)
                        .totalRating(2.0)
                        .build())
                .flatMap(userRepository::save)
                .onErrorResume(e -> Mono.error(new RuntimeException("사용자 등록 중 오류 발생: " + e.getMessage())));
    }


    @Override
    public Mono<Boolean> existsById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public Mono<Long> count() {
        return userRepository.count();
    }

    @Override
    public Mono<Void> deleteById(String userInfoHeader) {
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);
        return userRepository.deleteById(userInfo.getUserId());
    }

    public Mono<UserDocument> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .doOnNext(user -> log.info("findByEmail: {}", user))
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("사용자를 찾을 수 없습니다: {}", email);
                    return Mono.empty();
                }));
    }

    public Mono<Boolean> checkEmail(UserModel userModel) {
        return userRepository.existsByEmail(userModel.getEmail());
    }

    public Mono<Boolean> checkPhone(UserModel userModel) {
        return userRepository.existsByPhone(userModel.getPhoneNum());
    }

    public Mono<Boolean> existsByPasswordAndEmail(String userInfoHeader, UserModel userModel) {
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);

        return userRepository.findById(userInfo.getUserId())
                .flatMap(user -> Mono.just(passwordEncoder.matches(userModel.getPassword(), user.getPassword()))) // 비밀번호 비교
                .defaultIfEmpty(false);
    }

    public Mono<String> getEmailByPhone(UserModel userModel) {
        return userRepository.findEmailByPhone(userModel.getPhoneNum())
                .switchIfEmpty(Mono.error(new RuntimeException("해당 전화번호로 사용자를 찾을 수 없습니다.: " + userModel.getPhoneNum())));
    }

    public Mono<String> changePassword(String userInfoHeader,UserModel userModel) {
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);

        return  userRepository.findById(userInfo.getUserId())
                .flatMap(user -> {
                    if (passwordEncoder.matches(userModel.getPassword(), user.getPassword())) { // 비밀번호 비교
                        String encodedNewPassword = passwordEncoder.encode(userModel.getNewPassword()); // 새 비밀번호 해시
                        user.setPassword(encodedNewPassword);
                        return userRepository.save(user)
                                .then(Mono.just("비밀번호 변경이 완료했습니다."));
                    } else {
                        return Mono.just("예전 비밀번호가 틀렸습니다.");
                    }
                })
                .switchIfEmpty(Mono.just("유저 대상이 없습니다."));
    }

    public Mono<UserDocument> register(UserModel userModel) {
        return userRepository.findByEmail(userModel.getEmail())
                .flatMap(user -> {
                    user.setOauthUser(userModel.getOauthName());
                    user.setPhone(userModel.getPhoneNum());
                    return userRepository.save(user);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    UserDocument userDocument = UserDocument.builder()
                            .email(userModel.getEmail())
                            .oauthUser(userModel.getOauthName())
                            .name(userModel.getName())
                            .phone(userModel.getPhoneNum())
                            .password(passwordEncoder.encode(userModel.getEmail()))
                            .role(Collections.singletonList(Role.ROLE_USER))
                            .status(true)
                            .totalRating(2.0)
                            .build();
                    return userRepository.save(userDocument);
                }))
                .onErrorResume(e -> Mono.error(new RuntimeException("사용자 등록 중 오류 발생: " + e.getMessage())));
    }
}