package src.Discounts;

import src.Product;
import src.ProductArray;
import src.PromotionCommand;

import java.util.Arrays;

/**
 * The FreeGiftPromotion class implements a promotion where gift is given
 * if the total order value exceeds a specified threshold.
 */
public class FreeGiftPromotion implements PromotionCommand {
	private final double threshold;
	private boolean giftAdded = false;
	private final Product giftProduct;

	public FreeGiftPromotion(double threshold, String giftName) {
		this.threshold = threshold;
		// Create the gift product with a special gift code
		this.giftProduct = new Product("GIFT-" + System.currentTimeMillis(), giftName, 0);
		// Set the discount price to 0 since it's free
		this.giftProduct.setDiscountPrice(0);
	}

	@Override
	public void apply(ProductArray productArray) {
		Product[] products = productArray.getProducts();

		// Check if the total order value exceeds the threshold
		double total = Arrays.stream(products)
				.mapToDouble(Product::getPrice)
				.sum();

		if (total >= threshold && !giftAdded) {
			// Add the gift product to the array using the wrapper
			productArray.addProduct(giftProduct);
			giftAdded = true;
			System.out.println("Free gift added: " + giftProduct.getName());
		}
	}

	@Override
	public void reset() {
		giftAdded = false;
	}
}