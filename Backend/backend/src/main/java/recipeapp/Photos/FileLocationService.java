package recipeapp.Photos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FileLocationService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    ImageDBRepository imageDBRepository;

    Long save(byte[] bytes, String imageName) throws Exception {
        String location = fileSystemRepository.save(bytes,imageName);

        return imageDBRepository.save(new Image(imageName, location)).getId();
    }
    FileSystemResource find(Long imageId) {
        Image image = imageDBRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return fileSystemRepository.findInFileSystem(image.getLocation());
    }
}
