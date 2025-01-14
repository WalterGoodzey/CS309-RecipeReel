package recipeapp.Photos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileSystemImageController {

    @Autowired
    FileLocationService fileLocationService;

    @PostMapping("/image")
    Long uploadImage(@RequestParam MultipartFile image) throws Exception {
        return fileLocationService.save(image.getBytes(), image.getOriginalFilename());
    }
    @GetMapping(value = "/image/{imageID}", produces = MediaType.IMAGE_JPEG_VALUE)
    FileSystemResource downloadImage(@PathVariable Long imageID) throws Exception {
        return fileLocationService.find(imageID);
    }
}
