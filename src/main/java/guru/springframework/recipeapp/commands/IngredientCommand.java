package guru.springframework.recipeapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private String id;
    private String recipeId;

    @NotBlank
    @Size(max = 255)
    private String description;

    @NotNull
    @DecimalMin("0.1")
    private BigDecimal amount;

    @NotNull
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
