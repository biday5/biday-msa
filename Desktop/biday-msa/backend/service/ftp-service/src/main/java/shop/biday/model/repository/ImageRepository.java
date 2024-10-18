package shop.biday.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.document.ImageDocument;
import shop.biday.model.domain.ImageModel;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<ImageDocument, String> {
    ImageDocument save(ImageModel imageModel);

    ImageModel findByType(String type);

    ImageModel findByOriginalNameAndType(String name, String type);

    ImageModel findByTypeAndUploadPath(String type, String uploadPath);

    ImageModel findByOriginalNameAndTypeAndReferencedId(String name, String type, Long referencedId);

    ImageModel findByTypeAndReferencedIdAndUploadPath(String type, String referencedId, String uploadPath);

    List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId);
}
