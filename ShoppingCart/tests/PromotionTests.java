package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.*;
import src.Discounts.*;

import static org.junit.jupiter.api.Assertions.*;

class PromotionTest {
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
    void testOrderValuePercentageDiscount() {
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        // Total: 100 + 200 + 50 = 350
        cart.addPromotion(new OrderValuePercentageDiscount(300.0, 5.0));

        // 350 * 0.95 = 332.5
        assertEquals(332.5, cart.calculateTotal());
    }

    @Test
    void testBuy2Get3rdFreePromotion() {
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        cart.addPromotion(new Buy2Get3rdFree());

        // p3 is free: 100 + 200 + 0 = 300
        assertEquals(300.0, cart.calculateTotal());
    }

    @Test
    void testBuy2Get3rdFreePromotionWithMultipleSets() {
        // Create 6 products with different prices
        Product p5 = new Product("P5", "Product E", 75.0);
        Product p6 = new Product("P6", "Product F", 25.0);

        // Add 6 products to the cart (2 complete sets of 3)
        cart.addProduct(p1); // 100.0
        cart.addProduct(p2); // 200.0
        cart.addProduct(p3); // 50.0
        cart.addProduct(p4); // 150.0
        cart.addProduct(p5); // 75.0
        cart.addProduct(p6); // 25.0

        // Add the promotion
        cart.addPromotion(new Buy2Get3rdFree());

        // Calculate the expected total:
        // - Without discount: 100 + 200 + 50 + 150 + 75 + 25 = 600
        // - The 2 cheapest products (25 and 50) should be free
        // - Expected: 600 - 25 - 50 = 525
        assertEquals(525.0, cart.calculateTotal());
    }

    @Test
    void testBuy2Get3rdFreePromotionWithIncompleteSets() {
        // Create 5 products (1 complete set of 3 + 2 more)
        cart.addProduct(p1); // 100.0
        cart.addProduct(p2); // 200.0
        cart.addProduct(p3); // 50.0
        cart.addProduct(p4); // 150.0
        cart.addProduct(new Product("P5", "Product E", 75.0)); // 75.0

        // Add the promotion
        cart.addPromotion(new Buy2Get3rdFree());

        // Calculate the expected total:
        // - Without discount: 100 + 200 + 50 + 150 + 75 = 575
        // - Only 1 cheapest product (50) should be free
        // - Expected: 575 - 50 = 525
        assertEquals(525.0, cart.calculateTotal());
    }

    @Test
    void testBuy2Get3rdFreePromotionWithNineSets() {
        // Create 9 products with different prices
        Product p5 = new Product("P5", "Product E", 75.0);
        Product p6 = new Product("P6", "Product F", 25.0);
        Product p7 = new Product("P7", "Product G", 35.0);
        Product p8 = new Product("P8", "Product H", 45.0);
        Product p9 = new Product("P9", "Product I", 15.0);

        // Add 9 products to the cart (3 complete sets of 3)
        cart.addProduct(p1); // 100.0
        cart.addProduct(p2); // 200.0
        cart.addProduct(p3); // 50.0
        cart.addProduct(p4); // 150.0
        cart.addProduct(p5); // 75.0
        cart.addProduct(p6); // 25.0
        cart.addProduct(p7); // 35.0
        cart.addProduct(p8); // 45.0
        cart.addProduct(p9); // 15.0

        // Add the promotion
        cart.addPromotion(new Buy2Get3rdFree());

        // Calculate the expected total:
        // - Without discount: 100 + 200 + 50 + 150 + 75 + 25 + 35 + 45 + 15 = 695
        // - The 3 cheapest products (15, 25, and 35) should be free
        // - Expected: 695 - 15 - 25 - 35 = 620
        assertEquals(620.0, cart.calculateTotal());
    }

    @Test
    void testFreeGiftPromotion() {
        cart.addProduct(p1);
        cart.addProduct(p2);

        // Total: 100 + 200 = 300
        cart.addPromotion(new FreeGiftPromotion(200.0, "JavaMarkt Mug"));

        // Apply promotions
        double total = cart.calculateTotal();

        // Check that a third product (gift) was added to the cart
        assertEquals(3, cart.getProducts().length);

        // The gift should be free (0 price)
        boolean hasGiftProduct = false;
        for (Product product : cart.getProducts()) {
            if (product.getCode().startsWith("GIFT-")) {
                hasGiftProduct = true;
                assertEquals(0.0, product.getDiscountPrice());
            }
        }

        // Verify that the gift was added
        assertTrue(hasGiftProduct);

        // Original products still total 300.0
        assertEquals(300.0, total);
    }

    @Test
    void testOneTimeCouponPromotion() {
        cart.addProduct(p1);
        cart.addProduct(p2);

        cart.addPromotion(new OneTimeCouponPromotion("P2", 30.0));

        // P2 with 30% discount: 100 + (200 * 0.7) = 240
        assertEquals(240.0, cart.calculateTotal());
    }

    @Test
    void testOptimalPromotionOrder() {
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        cart.addPromotion(new OrderValuePercentageDiscount(300.0, 5.0));
        cart.addPromotion(new Buy2Get3rdFree());

        // If Buy2Get3rdFree first: 100 + 200 + 0 = 300
        // Then 5% off: 300 * 0.95 = 285
        assertEquals(285.0, cart.calculateTotal());
    }

    @Test
    void testMultiplePromotions() {
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);
        cart.addProduct(p4);

        cart.addPromotion(new OrderValuePercentageDiscount(300.0, 5.0));
        cart.addPromotion(new Buy2Get3rdFree());
        cart.addPromotion(new OneTimeCouponPromotion("P2", 30.0));

        // The optimal order will be calculated by the cart
        double total = cart.calculateTotal();
        assertTrue(total < 500.0); // Total without discounts would be 500
    }

    @Test
    void testRemovePromotion() {
        cart.addProduct(p1);
        cart.addProduct(p2);

        // Total: 100.0 + 200.0 = 300.0

        // Add a promotion
        PromotionCommand discount = new OrderValuePercentageDiscount(200.0, 10.0);
        cart.addPromotion(discount);

        // With 10% discount: 300.0 * 0.9 = 270.0
        assertEquals(270.0, cart.calculateTotal());

        // Remove the promotion
        cart.removePromotion(discount);

        // Total should be back to 300.0
        assertEquals(300.0, cart.calculateTotal());
    }

    @Test
    void testFreeGiftAndDiscountPromotion() {
        cart.addProduct(p1); // 100.0
        cart.addProduct(p2); // 200.0
        cart.addProduct(p3); // 50.0

        // Total: 350.0

        // Add gift and percentage discount
        cart.addPromotion(new FreeGiftPromotion(300.0, "JavaMarkt Mug"));
        cart.addPromotion(new OrderValuePercentageDiscount(300.0, 10.0));

        // Calculate total (should apply both promotions)
        double total = cart.calculateTotal();

        // Check that the gift was added
        assertEquals(4, cart.getProducts().length);

        // Total should be 350.0 - 10% = 315.0
        assertEquals(315.0, total);
    }
}