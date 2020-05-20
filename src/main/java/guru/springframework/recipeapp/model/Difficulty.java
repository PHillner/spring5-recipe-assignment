package guru.springframework.recipeapp.model;

public enum Difficulty {

    EASY, MODERATE, HARD;

    public String formattedName() {
        String difficultyString = this.toString();
        return difficultyString.substring(0,1).toUpperCase() + difficultyString.substring(1).toLowerCase();
    }
}
