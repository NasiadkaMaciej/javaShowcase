package src.Discounts;

import java.util.Arrays;
import src.Product;
import src.ProductArray;
import src.PromotionCommand;

/**
 * The OrderValuePercentageDiscount class implements a promotion where a
 * percentage discount is applied to the total order value if it exceeds
 * a specified threshold.
 */

public class OrderValuePercentageDiscount implements PromotionCommand {
	private final double threshold;
	private final double discountPercentage;

	public OrderValuePercentageDiscount(double threshold, double discountPercentage) {
		this.threshold = threshold;
		this.discountPercentage = discountPercentage;
	}

	@Override
	public void apply(ProductArray productArray) {
		Product[] products = productArray.getProducts();
		double total = Arrays.stream(products).mapToDouble(Product::getPrice).sum();

		if (total > threshold) {
			double discountFactor = 1 - (discountPercentage / 100);
			for (Product product : products) {
				product.setDiscountPrice(product.getDiscountPrice() * discountFactor);
			}
		}
	}

	@Override
	public void reset() {}
}