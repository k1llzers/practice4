package models;

import annotations.GetValue;

public class Product {
    private String name;
    private Double price;

    @GetValue("sale")
    private Integer saleInPercent;

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", saleInPercent=" + saleInPercent +
                '}';
    }
}
