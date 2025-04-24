package src.Discounts;

import java.util.Arrays;
import src.Product;
import src.ProductArray;
import src.PromotionCommand;

/**
 * The OrderValuePercentageDiscount class implements a promotion where a
 * percentage discount
 * is applied to the total order value if it exceeds a specified threshold.
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
		double total = Arrays.stream(products)
						 .mapToDouble(Product::getPrice) // Use original price for threshold check
						 .sum();

		if (total > threshold) {
			for (Product product : products) {
				double currentPrice = product.getDiscountPrice();
				double discountAmount = currentPrice * (discountPercentage / 100);
				product.setDiscountPrice(currentPrice - discountAmount);
			}
		}
	}

	@Override
	public void reset() {}
}