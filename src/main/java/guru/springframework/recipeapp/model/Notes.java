package guru.springframework.recipeapp.model;

import lombok.*;

@Getter
@Setter
public class Notes {

    private String id;
    private String recipeNotes;
//    private Recipe recipe;

    public Notes() {
    }

    public Notes(String recipeNotes) {
        this.recipeNotes = recipeNotes;
    }

//    public Notes(String recipeNotes, Recipe recipe) {
//        this.recipeNotes = recipeNotes;
//        this.recipe = recipe;
//    }

}
