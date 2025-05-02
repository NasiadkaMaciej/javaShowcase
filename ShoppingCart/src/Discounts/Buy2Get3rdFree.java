package src.Discounts;

import java.util.Arrays;
import src.Product;
import src.ProductArray;
import src.ProductComparators;
import src.PromotionCommand;

/**
 * Implements a "Buy 2 Get 3rd Free" promotion where for every 3 products,
 * the cheapest one is free.
 */
public class Buy2Get3rdFree implements PromotionCommand {

	@Override
	public void apply(ProductArray productArray) {
		Product[] products = productArray.getProducts();
		if (products.length < 3) return;

		// Filter out gift products and create a copy array
		Product[] eligibleProducts =
		  Arrays.stream(products).filter(p -> !p.getCode().startsWith("GIFT-")).toArray(Product[] ::new);

		if (eligibleProducts.length < 3) return;

		// Sort products by price (cheapest first) using the ProductComparators class
		Arrays.sort(eligibleProducts, ProductComparators.BY_PRICE_ASC);

		// Calculate how many free products to give (one per every 3 products)
		int freeCount = eligibleProducts.length / 3;

		// Make the cheapest products free
		for (int i = 0; i < freeCount; i++) {
			Product freeProduct = eligibleProducts[i];
			// Find this product in the original array and set its discount price to 0
			for (Product product : products) {
				if (product.getCode().equals(freeProduct.getCode())) {
					product.setDiscountPrice(0);
					break;
				}
			}
		}
	}

	@Override
	public void reset() {}
}