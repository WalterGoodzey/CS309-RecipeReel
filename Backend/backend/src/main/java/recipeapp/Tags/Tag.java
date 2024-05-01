package recipeapp.Tags;

import jakarta.persistence.*;
import lombok.NonNull;
import recipeapp.Recipes.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Tag {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String tagName;

    @OneToMany(mappedBy ="tag")
    Set<TagRecipeConnector> recipeConnectorSet;
}
