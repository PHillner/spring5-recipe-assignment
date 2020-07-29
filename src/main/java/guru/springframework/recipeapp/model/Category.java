package guru.springframework.recipeapp.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Set;

@Getter
@Setter
@Document
public class Category {

    @Id
    private String id;
    private String description;

//    @DBRef
//    private Set<Recipe> recipes;

}
