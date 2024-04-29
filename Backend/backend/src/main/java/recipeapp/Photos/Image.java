package recipeapp.Photos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity @Getter @Setter
class Image {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String location;

    @Lob
    byte[] content;

    public Image() {
    }

    public Image(Long id) {
        this.id = id;
    }

    public Image(String name, String location) {
        this.name = name;
        this.location = location;
    }

}
