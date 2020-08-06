package guru.springframework.recipeapp.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Ingredient {

    private String id = UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;
    private UnitOfMeasure uom;

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
