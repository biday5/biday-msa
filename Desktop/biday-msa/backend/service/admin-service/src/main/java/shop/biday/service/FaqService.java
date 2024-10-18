package shop.biday.service;

import shop.biday.model.domain.FaqModel;

import java.util.List;

public interface FaqService {

    List<FaqModel> findAll();

    FaqModel save(String userInfo, FaqModel questionModel);

    FaqModel findById(Long id);

    boolean deleteById(Long id, String userInfo);

    boolean existsById(Long id);
}
