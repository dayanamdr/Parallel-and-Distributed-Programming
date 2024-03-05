import java.util.HashMap;
import java.util.Set;

public class Inventory {
    private  HashMap<Product, Integer> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public int getQuantity(Product product) {
        return this.products.getOrDefault(product, 0);
    }

    public Set<Product> getProducts() {
        return this.products.keySet();
    }

    public void add(Product product, int quantity) {
        if (this.products.containsKey(product)) {
            this.products.replace(product, this.products.get(product) + quantity);
        } else {
            this.products.put(product, quantity);
        }
    }

    public void remove(Product product, int quantity) {
        if (!this.products.containsKey(product)) {
            throw new RuntimeException("Can't remove quantity of an unknown product from inventory.");
        }

        if (this.getQuantity(product) < quantity) {
            throw new RuntimeException("Can't remove the selected quantity from inventory. Existing quantity is not enough");
        }
        this.products.replace(product, this.products.get(product) - quantity);
        if (this.getQuantity(product) == 0) {
            this.products.remove(product);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Product product: this.getProducts()) {
            sb.append("{").append(product.getName()).append(", ").append(this.getQuantity(product)).append("}\n");
        }
        return sb.toString();
    }
}
