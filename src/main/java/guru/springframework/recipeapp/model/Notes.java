package guru.springframework.recipeapp.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class Notes {

    private String id;
    private String recipeNotes;

    public Notes() {
    }

    public Notes(String recipeNotes) {
        this.recipeNotes = recipeNotes;
    }

}
