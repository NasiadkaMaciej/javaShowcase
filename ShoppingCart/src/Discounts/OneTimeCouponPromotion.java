package src.Discounts;

import src.Product;
import src.ProductArray;
import src.PromotionCommand;

/**
 * The OneTimeCouponPromotion class implements a one-time use coupon promotion
 * that applies a discount to a specific product based on its code.
 */
public class OneTimeCouponPromotion implements PromotionCommand {
	private final String productCode;
	private final double discountPercentage;
	private boolean used = false;

	public OneTimeCouponPromotion(String productCode, double discountPercentage) {
		this.productCode = productCode;
		this.discountPercentage = discountPercentage;
	}

	@Override
	public void apply(ProductArray productArray) {
		if (used) { return; }

		for (Product product : productArray.getProducts()) {
			if (product.getCode().equals(productCode)) {
				double currentPrice = product.getDiscountPrice();
				product.setDiscountPrice(currentPrice * (1 - discountPercentage / 100));
				used = true;
				break;
			}
		}
	}

	@Override
	public void reset() {
		used = false;
	}
}