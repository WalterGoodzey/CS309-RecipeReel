package recipeapp.Tags;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipeapp.Recipes.Recipe;

@RestController
public class TagController {

    @Autowired
    TagRepository tagRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/tags")
    List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

    @GetMapping(path = "/tags/{id}")
    Tag getTagById(@PathVariable int id) {
        return tagRepository.findById(id);
    }

    @PostMapping(path = "/tags")
    String createTag(@RequestBody Tag tag){
        if(tag == null)
            return failure;
        tagRepository.save(tag);
        return success;
    }

    @PutMapping(path = "/tags/{id}")
    Tag updateTag(@PathVariable int id, @RequestBody Tag request){
        Tag tag = tagRepository.findById(id);
        if(tag == null)
            return null;
        tagRepository.save(request);
        return tagRepository.findById(id);
    }

    @DeleteMapping(path = "/tags/{id}")
    String deleteTag(@PathVariable int id){
        Tag tag = tagRepository.findById(id);
        if(tag == null)
            return failure;
        for(Recipe recipe : tag.getRecipes()){
            recipe.removeTag(tag);
        }
        tagRepository.delete(tag);
        return success;
    }

}
