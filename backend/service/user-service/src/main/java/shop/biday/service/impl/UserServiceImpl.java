//package shop.biday.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import shop.biday.model.document.UserDocument;
//import shop.biday.model.domain.UserInfoModel;
//import shop.biday.model.domain.UserModel;
//import shop.biday.model.domain.UserRequest;
//import shop.biday.model.enums.Role;
//import shop.biday.model.repository.MUserRepository;
//import shop.biday.service.UserService;
//import shop.biday.utils.UserInfoUtils;
//
//import java.util.Collections;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//    private final MUserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final UserInfoUtils userInfoUtils;
//
//    @Override
//    public Flux<UserModel> findAll() {
//        return userRepository.findAll()
//                .map(UserModel::fromDocument);
//    }
//
//    @Override
//    public Mono<UserModel> findById(String id) {
//        return userRepository.findById(id)
//                .map(UserModel::fromDocument);
//    }
//
//    @Override
//    public Mono<UserDocument> save(UserRequest userRequest) {
//        log.info("직접 회원가입: {}", userRequest);
//        return Mono.just(userRequest)
//                .map(req -> UserDocument.builder()
//                        .name(req.name())
//                        .email(req.email())
//                        .password(passwordEncoder.encode(req.password()))
//                        .phone(req.phoneNum())
//                        .role(Collections.singletonList(Role.ROLE_USER))
//                        .status(true)
//                        .totalRating(2.0)
//                        .build())
//                .flatMap(userRepository::save)
//                .onErrorResume(e -> Mono.error(new RuntimeException("사용자 등록 중 오류 발생: " + e.getMessage())));
//    }
//
//
//    @Override
//    public Mono<Boolean> existsById(String id) {
//        return userRepository.existsById(id);
//    }
//
//    @Override
//    public Mono<Long> count() {
//        return userRepository.count();
//    }
//
//    @Override
//    public Mono<Void> deleteById(String userInfoHeader) {
//        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);
//        return userRepository.deleteById(userInfo.getUserId());
//    }
//
//    public Mono<UserDocument> findByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .doOnNext(user -> log.info("findByEmail: {}", user))
//                .switchIfEmpty(Mono.defer(() -> {
//                    log.error("사용자를 찾을 수 없습니다: {}", email);
//                    return Mono.empty();
//                }));
//    }
//
//    public Mono<Boolean> checkEmail(UserModel userModel) {
//        return userRepository.existsByEmail(userModel.getEmail());
//    }
//
//    public Mono<Boolean> checkPhone(UserModel userModel) {
//        return userRepository.existsByPhone(userModel.getPhoneNum());
//    }
//
//    public Mono<Boolean> existsByPasswordAndEmail(String userInfoHeader) {
//        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);
//
//        return userRepository.findById(userInfo.getUserId())
//                .flatMap(user -> {
//                    String emailFromDb = user.getEmail();
//                    String passwordFromDb = user.getPassword();
//                    boolean isPasswordEmailSame = passwordEncoder.matches(emailFromDb, passwordFromDb);
//                    return Mono.just(isPasswordEmailSame);
//                })
//                .defaultIfEmpty(false);
//    }
//
//    public Mono<String> getEmailByPhone(UserModel userModel) {
//        return userRepository.findEmailByPhone(userModel.getPhoneNum())
//                .switchIfEmpty(Mono.error(new RuntimeException("해당 전화번호로 사용자를 찾을 수 없습니다.: " + userModel.getPhoneNum())));
//    }
//
//    public Mono<String> changePassword(String userInfoHeader,UserModel userModel) {
//        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);
//
//        return  userRepository.findById(userInfo.getUserId())
//                .flatMap(user -> {
//                    if (passwordEncoder.matches(userModel.getPassword(), user.getPassword())) { // 비밀번호 비교
//                        String encodedNewPassword = passwordEncoder.encode(userModel.getNewPassword()); // 새 비밀번호 해시
//                        user.setPassword(encodedNewPassword);
//                        return userRepository.save(user)
//                                .then(Mono.just("비밀번호 변경이 완료했습니다."));
//                    } else {
//                        return Mono.just("예전 비밀번호가 틀렸습니다.");
//                    }
//                })
//                .switchIfEmpty(Mono.just("유저 대상이 없습니다."));
//    }
//
//    public Mono<UserDocument> register(UserModel userModel) {
//        return userRepository.findByEmail(userModel.getEmail())
//                .flatMap(user -> {
//                    user.setOauthUser(userModel.getOauthName());
//                    user.setPhone(userModel.getPhoneNum());
//                    return userRepository.save(user);
//                })
//                .switchIfEmpty(Mono.defer(() -> {
//                    UserDocument userDocument = UserDocument.builder()
//                            .email(userModel.getEmail())
//                            .oauthUser(userModel.getOauthName())
//                            .name(userModel.getName())
//                            .phone(userModel.getPhoneNum())
//                            .password(passwordEncoder.encode(userModel.getEmail()))
//                            .role(Collections.singletonList(Role.ROLE_USER))
//                            .status(true)
//                            .totalRating(2.0)
//                            .build();
//                    return userRepository.save(userDocument);
//                }))
//                .onErrorResume(e -> Mono.error(new RuntimeException("사용자 등록 중 오류 발생: " + e.getMessage())));
//    }
//}

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

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final MUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoUtils userInfoUtils;

    @Override
    public Flux<UserModel> findAll() {
        log.info("findAll: 모든 사용자 가져오기 시작");
        return userRepository.findAll()
                .map(UserModel::fromDocument)
                .doOnComplete(() -> log.info("findAll: 모든 사용자 가져오기 완료"))
                .doOnError(e -> log.error("findAll: 사용자 가져오는 중 오류 발생", e));
    }

    @Override
    public Mono<UserModel> findById(String id) {
        log.info("findById: ID {}로 사용자 조회 시작", id);
        return userRepository.findById(id)
                .map(UserModel::fromDocument)
                .doOnSuccess(user -> log.info("findById: 사용자 조회 성공: {}", user))
                .doOnError(e -> log.error("findById: ID {}로 사용자 조회 중 오류 발생", id, e));
    }

    @Override
    public Mono<UserDocument> save(UserRequest userRequest) {
        log.info("save: 신규 사용자 등록 요청: {}", userRequest);
        return Mono.just(userRequest)
                .map(req -> {
                    log.debug("save: 사용자 {}의 비밀번호 암호화 중", req.email());
                    return UserDocument.builder()
                            .name(req.name())
                            .email(req.email())
                            .password(passwordEncoder.encode(req.password()))
                            .phone(req.phoneNum())
                            .role(Collections.singletonList(Role.ROLE_USER))
                            .status(true)
                            .totalRating(2.0)
                            .build();
                })
                .flatMap(userRepository::save)
                .doOnSuccess(user -> log.info("save: 사용자 저장 완료: {}", user))
                .doOnError(e -> log.error("save: 사용자 등록 중 오류 발생", e))
                .onErrorResume(e -> Mono.error(new RuntimeException("사용자 등록 중 오류 발생: " + e.getMessage())));
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        log.info("existsById: ID {}의 사용자 존재 여부 확인", id);
        return userRepository.existsById(id)
                .doOnSuccess(exists -> log.info("existsById: 사용자 존재 여부: {}", exists))
                .doOnError(e -> log.error("existsById: ID {}의 사용자 존재 여부 확인 중 오류 발생", id, e));
    }

    @Override
    public Mono<Long> count() {
        log.info("count: 전체 사용자 수 확인 시작");
        return userRepository.count()
                .doOnSuccess(count -> log.info("count: 전체 사용자 수: {}", count))
                .doOnError(e -> log.error("count: 사용자 수 확인 중 오류 발생", e));
    }

    @Override
    public Mono<Void> deleteById(String userInfoHeader) {
        log.info("deleteById: 헤더에서 사용자 정보 추출 중");
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);
        log.info("deleteById: ID {}의 사용자 삭제 시작", userInfo.getUserId());
        return userRepository.deleteById(userInfo.getUserId())
                .doOnSuccess(unused -> log.info("deleteById: 사용자 삭제 완료"))
                .doOnError(e -> log.error("deleteById: ID {}의 사용자 삭제 중 오류 발생", userInfo.getUserId(), e));
    }

    public Mono<UserDocument> findByEmail(String email) {
        log.info("findByEmail: 이메일 {}로 사용자 조회 시작", email);
        return userRepository.findByEmail(email)
                .doOnNext(user -> log.info("findByEmail: 사용자 조회 성공: {}", user))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("findByEmail: 이메일 {}로 사용자를 찾을 수 없음", email);
                    return Mono.empty();
                }))
                .doOnError(e -> log.error("findByEmail: 이메일 {}로 사용자 조회 중 오류 발생", email, e));
    }

    public Mono<Boolean> checkEmail(UserModel userModel) {
        log.info("checkEmail: 이메일 {}의 존재 여부 확인 시작", userModel.getEmail());
        return userRepository.existsByEmail(userModel.getEmail())
                .doOnSuccess(exists -> log.info("checkEmail: 이메일 존재 여부: {}", exists))
                .doOnError(e -> log.error("checkEmail: 이메일 존재 여부 확인 중 오류 발생", e));
    }

    public Mono<Boolean> checkPhone(UserModel userModel) {
        log.info("checkPhone: 전화번호 {}의 존재 여부 확인 시작", userModel.getPhoneNum());
        return userRepository.existsByPhone(userModel.getPhoneNum())
                .doOnSuccess(exists -> log.info("checkPhone: 전화번호 존재 여부: {}", exists))
                .doOnError(e -> log.error("checkPhone: 전화번호 존재 여부 확인 중 오류 발생", e));
    }

    public Mono<Boolean> existsByPasswordAndEmail(String userInfoHeader) {
        log.info("existsByPasswordAndEmail: 헤더에서 사용자 정보 추출 중");
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);
        log.info("existsByPasswordAndEmail: ID {}의 비밀번호와 이메일 일치 여부 확인", userInfo.getUserId());

        return userRepository.findById(userInfo.getUserId())
                .flatMap(user -> {
                    boolean isPasswordEmailSame = passwordEncoder.matches(user.getEmail(), user.getPassword());
                    log.info("existsByPasswordAndEmail: 비밀번호와 이메일 일치 여부: {}", isPasswordEmailSame);
                    return Mono.just(isPasswordEmailSame);
                })
                .defaultIfEmpty(false)
                .doOnError(e -> log.error("existsByPasswordAndEmail: 비밀번호와 이메일 일치 여부 확인 중 오류 발생", e));
    }

    public Mono<String> getEmailByPhone(UserModel userModel) {
        log.info("getEmailByPhone: 전화번호 {}로 이메일 조회 시작", userModel.getPhoneNum());
        return userRepository.findEmailByPhone(userModel.getPhoneNum())
                .doOnSuccess(email -> log.info("getEmailByPhone: 이메일 조회 성공: {}", email))
                .switchIfEmpty(Mono.error(new RuntimeException("해당 전화번호로 사용자를 찾을 수 없습니다.: " + userModel.getPhoneNum())))
                .doOnError(e -> log.error("getEmailByPhone: 전화번호로 이메일 조회 중 오류 발생", e));
    }

    public Mono<String> changePassword(String userInfoHeader, UserModel userModel) {
        log.info("changePassword: 헤더에서 사용자 정보 추출 중");
        UserInfoModel userInfo = userInfoUtils.extractUserInfo(userInfoHeader);

        return userRepository.findById(userInfo.getUserId())
                .flatMap(user -> {
                    if (passwordEncoder.matches(userModel.getPassword(), user.getPassword())) {
                        log.info("changePassword: 비밀번호 일치 확인, 비밀번호 업데이트 중");

                        if (passwordEncoder.matches(userModel.getNewPassword(), user.getPassword())) {
                            log.warn("changePassword: 새 비밀번호가 기존 비밀번호와 동일함");
                            return Mono.just("새 비밀번호가 기존 비밀번호와 동일합니다.");
                        }
                        user.setPassword(passwordEncoder.encode(userModel.getNewPassword()));
                        return userRepository.save(user)
                                .then(Mono.just("비밀번호 변경이 완료했습니다."));
                    } else {
                        log.warn("changePassword: 이전 비밀번호가 일치하지 않음");
                        return Mono.just("예전 비밀번호가 틀렸습니다.");
                    }
                })
                .switchIfEmpty(Mono.just("유저 대상이 없습니다."))
                .doOnError(e -> log.error("changePassword: 비밀번호 변경 중 오류 발생", e));
    }

    public Mono<UserDocument> register(UserModel userModel) {
        log.info("register: 이메일 {}로 사용자 등록 시작", userModel.getEmail());
        return userRepository.findByEmail(userModel.getEmail())
                .flatMap(user -> {
                    log.info("register: 사용자 이미 존재, OAuth 정보 업데이트 중");
                    user.setOauthUser(userModel.getOauthName());
                    user.setPhone(userModel.getPhoneNum());
                    return userRepository.save(user);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("register: 새로운 사용자 생성 중");
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
                .doOnSuccess(user -> log.info("register: 사용자 등록 완료: {}", user))
                .doOnError(e -> log.error("register: 사용자 등록 중 오류 발생", e))
                .onErrorResume(e -> Mono.error(new RuntimeException("사용자 등록 중 오류 발생: " + e.getMessage())));
    }

    public Mono<UserDocument> resetPassword(UserModel userModel) {
        log.info("메일과 전화번호를 통한 User 검증 : Email {} Phone {}", userModel.getEmail(), userModel.getPhoneNum());
        return userRepository.findByEmailAndPhone(userModel.getEmail(), userModel.getPhoneNum())
                .flatMap(user -> {
                    log.debug("사용자 조회 성공: {}", user);
                    String newPassword = generateRandomPassword();
                    log.debug("새로운 비밀번호 생성 완료: {}", newPassword);
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return userRepository.save(user)
                            .doOnSuccess(updatedUser -> log.info("비밀번호 변경 완료: {}", updatedUser.getEmail())) // 비밀번호 변경 성공 로그
                            .then(Mono.just(user));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("사용자를 찾을 수 없습니다: Email {} Phone {}", userModel.getEmail(), userModel.getPhoneNum());
                    return Mono.empty();
                }));
    }

    private String generateRandomPassword() {
        log.info("새로운 비밀번호 8자 생성 시작");
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[6];
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}