package guru.springframework.recipeapp.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;

    @OneToOne
    private UnitOfMeasure uom;

    @ManyToOne
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom, Recipe recipe) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
        this.recipe = recipe;
    }

    public String toString() {
        String unitString = " ";
        switch (uom.getDescription()) {
            case "each":
            case "pcs":
                unitString = "";
                break;
            default:
                unitString += uom.getDescription();
                break;
        }
        String amountString = "";
        switch (amount.remainder(BigDecimal.ONE).toString()) {
            case "0.00":
                amountString = amount.toBigInteger().toString();
                break;
            default:
                amountString = amount.toString();
                break;
        }
        return String.format("%s%s %s", amountString, unitString, description);
    }

}
