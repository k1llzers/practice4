import models.Product;
import processors.ValueProcessor;

public class Main {
    private final static ValueProcessor valueProcessor = new ValueProcessor("src/main/resources/application.properties");

    public static void main(String[] args) {
        Product product = new Product("Картопля", 100.0);
        valueProcessor.process(product);
        System.out.println(product);
    }
}