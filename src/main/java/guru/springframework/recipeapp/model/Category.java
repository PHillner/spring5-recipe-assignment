package guru.springframework.recipeapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Category {

    @Id
    private String id;
    private String description;

}
