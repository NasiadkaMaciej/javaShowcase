package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.*;
import src.Discounts.*;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    private ShoppingCart cart;
    private Product p1, p2, p3, p4;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        p1 = new Product("P1", "Product A", 100.0);
        p2 = new Product("P2", "Product B", 200.0);
        p3 = new Product("P3", "Product C", 50.0);
        p4 = new Product("P4", "Product D", 150.0);
    }

    @Test
    void testAddAndRemoveProduct() {
        cart.addProduct(p1);
        cart.addProduct(p2);
        assertEquals(2, cart.getProducts().length);

        cart.removeProduct("P1");
        assertEquals(1, cart.getProducts().length);
        assertEquals(p2, cart.getProducts()[0]);
    }

    @Test
    void testSortingStrategy() {
        cart.addProduct(p3); // 50.0
        cart.addProduct(p1); // 100.0
        cart.addProduct(p4); // 150.0
        cart.addProduct(p2); // 200.0

        // Default sorting is BY_PRICE_DESC
        Product[] products = cart.getProducts();
        assertEquals(p2, products[0]);
        assertEquals(p4, products[1]);
        assertEquals(p1, products[2]);
        assertEquals(p3, products[3]);

        // Test sorting by name
        cart.setSortingStrategy(ProductComparators.BY_NAME);
        products = cart.getProducts();
        assertEquals("Product A", products[0].getName());
        assertEquals("Product B", products[1].getName());
        assertEquals("Product C", products[2].getName());
        assertEquals("Product D", products[3].getName());
    }

    @Test
    void testSortingByDiscountPrice() {
        // Create products with different prices
        cart.addProduct(p3); // 50.0
        cart.addProduct(p1); // 100.0
        cart.addProduct(p4); // 150.0
        cart.addProduct(p2); // 200.0

        // Apply a promotion that changes discount prices
        cart.addPromotion(new OneTimeCouponPromotion("P1", 50.0)); // P1 becomes 50.0 after 50% discount
        cart.calculateTotal(); // This applies the promotions

        // Switch to discount price sorting
        cart.setSortingStrategy(ProductComparators.BY_DISCOUNT_PRICE_DESC);

        Product[] products = cart.getProducts();
        assertEquals(p2, products[0]); // 200.0
        assertEquals(p4, products[1]); // 150.0
        assertEquals(p1, products[2]); // 50.0 after discount
        assertEquals(p3, products[3]); // 50.0
    }

    @Test
    void testSortingTieBreakerByName() {
        Product p1a = new Product("P1A", "Apple", 100.0);
        Product p1b = new Product("P1B", "Banana", 100.0);
        Product p1c = new Product("P1C", "Cherry", 100.0);

        // Add products with the same price
        cart.addProduct(p1c);
        cart.addProduct(p1a);
        cart.addProduct(p1b);

        // They should be sorted by name when price is the same
        Product[] products = cart.getProducts();
        assertEquals("Apple", products[0].getName());
        assertEquals("Banana", products[1].getName());
        assertEquals("Cherry", products[2].getName());
    }

    @Test
    void testFindCheapestAndMostExpensive() {
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);
        cart.addProduct(p4);

        assertEquals(p3, cart.findCheapestProduct());
        assertEquals(p2, cart.findMostExpensiveProduct());

        Product[] cheapest2 = cart.findNCheapestProducts(2);
        assertEquals(p3, cheapest2[0]);
        assertEquals(p1, cheapest2[1]);

        Product[] expensive2 = cart.findNMostExpensiveProducts(2);
        assertEquals(p2, expensive2[0]);
        assertEquals(p4, expensive2[1]);
    }
}