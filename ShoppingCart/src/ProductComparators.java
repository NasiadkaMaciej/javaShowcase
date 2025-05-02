package src;

import java.util.Comparator;

/**
 * The ProductComparators class provides various comparators for sorting Product
 * objects.
 */
public class ProductComparators {
	/**
	 * Comparator for sorting products by price in descending order.
	 * If two products have the same price, they are sorted by name in ascending
	 * order.
	 */
	public static final Comparator<Product> BY_PRICE_DESC = (p1, p2) -> {
		int priceComparison = Double.compare(p2.getPrice(), p1.getPrice());
		return priceComparison != 0 ? priceComparison : p1.getName().compareTo(p2.getName());
	};

	/**
	 * Comparator for sorting products by price in ascending order.
	 * If two products have the same price, they are sorted by name in ascending
	 * order.
	 */
	public static final Comparator<Product> BY_PRICE_ASC = (p1, p2) -> {
		int priceComparison = Double.compare(p1.getPrice(), p2.getPrice());
		return priceComparison != 0 ? priceComparison : p1.getName().compareTo(p2.getName());
	};

	/**
	 * Comparator for sorting products by name in alphabetical order.
	 */
	public static final Comparator<Product> BY_NAME = Comparator.comparing(Product::getName);

	/**
	 * Comparator for sorting products by discount price in descending order.
	 * If two products have the same discount price, they are sorted by name in
	 * ascending order.
	 */
	public static final Comparator<Product> BY_DISCOUNT_PRICE_DESC = (p1, p2) -> {
		int priceComparison = Double.compare(p2.getDiscountPrice(), p1.getDiscountPrice());
		return priceComparison != 0 ? priceComparison : p1.getName().compareTo(p2.getName());
	};
}