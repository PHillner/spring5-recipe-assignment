package guru.springframework.recipeapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private String id;
    private String recipeId;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasureCommand uom;

    public String toString() {
        String unitString = " ";
        if (uom != null && uom.getDescription() != null) {
            switch (uom.getDescription()) {
                case "each":
                case "pcs":
                    unitString = "";
                    break;
                default:
                    unitString += uom.getDescription();
                    break;
            }
        }
        String amountString = "";
        if (amount != null) {
            switch (amount.remainder(BigDecimal.ONE).toString()) {
                case "0.00":
                    amountString = amount.toBigInteger().toString();
                    break;
                default:
                    amountString = amount.toString();
                    break;
            }
        }
        return String.format("%s%s %s", amountString, unitString, description);
    }
}
