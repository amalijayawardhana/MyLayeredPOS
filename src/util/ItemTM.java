package util;

public class ItemTM {
    private String itemCode;
    private String description;
    private String unitPrice;
    private String quantityOnHand;

    public ItemTM() {
    }

    public ItemTM(String itemCode, String description, String unitPrice, String quantityOnHand) {
        this.itemCode = itemCode;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantityOnHand = quantityOnHand;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(String quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    @Override
    public String toString() {
        return itemCode;
    }
}
