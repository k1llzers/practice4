import models.Product;
import models.ProductStorage;
import processors.ValueProcessor;

public class Main {
    private final static ValueProcessor valueProcessor = new ValueProcessor("src/main/resources/application.properties");

    public static void main(String[] args) {
        ProductStorage storage = new ProductStorage();
        Product product = new Product("Картопля", 100.0);
        valueProcessor.process(product);
        storage.add(product);
        System.out.println(product);
    }
}
