package src.Discounts;

import java.util.Arrays;
import src.Product;
import src.ProductArray;
import src.PromotionCommand;

/**
 * The FreeGiftPromotion class implements a promotion where gift is given
 * if the total order value exceeds a specified threshold.
 */
public class FreeGiftPromotion implements PromotionCommand {
	private final double threshold;
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
		double total = Arrays.stream(products).mapToDouble(Product::getPrice).sum();

		// Check if the gift is already present in the cart
		boolean hasGift = Arrays.stream(products).anyMatch(p -> p.getCode().startsWith("GIFT-"));

		if (total >= threshold && !hasGift) {
			productArray.addProduct(giftProduct);
			System.out.println("Free gift added: " + giftProduct.getName());
		}
	}

	@Override
	public void reset() {}
}