package recipeapp.Photos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * THIS SAVES IMAGE TO DB
 */

@RestController
public class ImageController {
    @Autowired
    ImageDBRepository imageDBRepository;

    @PostMapping("/image")
    Long uploadIamge (@RequestParam MultipartFile multipartImage) throws Exception {
        Image dbImage = new Image();
        dbImage.setName(multipartImage.getName());
        dbImage.setContent(multipartImage.getBytes());

        return imageDBRepository.save(dbImage).getId();
    }

    @GetMapping(value ="/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource downloadImage(@PathVariable Long imageId) {
        byte[] image = imageDBRepository.findById(imageId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getContent();

        return new ByteArrayResource(image);

    }
}
