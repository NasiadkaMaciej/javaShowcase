package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The ShoppingCart class represents a shopping cart that holds products and
 * applies promotions.
 */
public class ShoppingCart {
	private final ProductArray productArray;
	private final List<PromotionCommand> promotions;
	private Comparator<Product> sortingStrategy;

	/**
	 * Constructs an empty ShoppingCart with default sorting strategy by price
	 * descending.
	 */
	public ShoppingCart() {
		this.productArray = new ProductArray(new Product[0]);
		this.promotions = new ArrayList<>();
		this.sortingStrategy = ProductComparators.BY_PRICE_DESC;
	}

	/**
	 * Adds a product to the shopping cart and sorts the products.
	 *
	 * @param product the product to add
	 */
	public void addProduct(Product product) {
		productArray.addProduct(product);
		sortProducts();
	}

	/**
	 * Removes a product from the shopping cart by its code.
	 *
	 * @param code the code of the product to remove
	 */
	public void removeProduct(String code) {
		Product[] products = productArray.getProducts();
		int index = -1;
		for (int i = 0; i < products.length; i++) {
			if (products[i].getCode().equals(code)) {
				index = i;
				break;
			}
		}

		if (index != -1) {
			Product[] newProducts = new Product[products.length - 1];
			System.arraycopy(products, 0, newProducts, 0, index);
			System.arraycopy(products, index + 1, newProducts, index, products.length - index - 1);
			productArray.setProducts(newProducts);
		}
	}

	/**
	 * Sets the sorting strategy for the products and sorts them.
	 *
	 * @param sortingStrategy the comparator to use for sorting products
	 */
	public void setSortingStrategy(Comparator<Product> sortingStrategy) {
		this.sortingStrategy = sortingStrategy;
		sortProducts();
	}

	/**
	 * Sorts the products using the current sorting strategy.
	 */
	private void sortProducts() {
		Product[] products = productArray.getProducts();
		Arrays.sort(products, sortingStrategy);
		productArray.setProducts(products);
	}

	/**
	 * Adds a promotion to the shopping cart.
	 *
	 * @param promotion the promotion to add
	 */
	public void addPromotion(PromotionCommand promotion) {
		promotions.add(promotion);
	}

	/**
	 * Removes a promotion from the shopping cart and recalculates the total.
	 *
	 * @param promotion the promotion to remove
	 */
	public void removePromotion(PromotionCommand promotion) {
		promotions.remove(promotion);
		calculateTotal();
	}

	/**
	 * Calculates the total price of the products in the cart after applying
	 * promotions.
	 *
	 * @return the total price after discounts
	 */
	public double calculateTotal() {
		Product[] products = productArray.getProducts();
		if (products.length == 0)
			return 0.0;
		resetDiscounts();

		// Reset all promotions
		for (PromotionCommand promotion : promotions) {
			promotion.reset();
		}

		// Apply promotions in the order they were added
		for (PromotionCommand promotion : promotions) {
			promotion.apply(productArray);
		}

		return Arrays.stream(products)
				.mapToDouble(Product::getDiscountPrice)
				.sum();
	}

	/**
	 * Resets the discount prices of all products to their original prices.
	 */
	private void resetDiscounts() {
		for (Product product : productArray.getProducts()) {
			product.resetDiscountPrice();
		}
	}

	/**
	 * Calculates the total price of the products before applying any promotions.
	 *
	 * @return the total price before discounts
	 */
	public double getTotalBeforePromotions() {
		return Arrays.stream(productArray.getProducts())
				.mapToDouble(Product::getPrice)
				.sum();
	}

	/**
	 * Finds the cheapest product in the shopping cart.
	 *
	 * @return the cheapest product, or null if the cart is empty
	 */
	public Product findCheapestProduct() {
		Product[] products = productArray.getProducts();
		if (products.length == 0)
			return null;
		return Arrays.stream(products)
				.min(Comparator.comparingDouble(Product::getPrice))
				.orElse(null);
	}

	/**
	 * Finds the most expensive product in the shopping cart.
	 *
	 * @return the most expensive product, or null if the cart is empty
	 */
	public Product findMostExpensiveProduct() {
		Product[] products = productArray.getProducts();
		if (products.length == 0)
			return null;
		return Arrays.stream(products)
				.max(Comparator.comparingDouble(Product::getPrice))
				.orElse(null);
	}

	/**
	 * Finds the specified number of cheapest products in the shopping cart.
	 *
	 * @param n the number of cheapest products to find
	 * @return an array of the n cheapest products
	 */
	public Product[] findNCheapestProducts(int n) {
		Product[] products = productArray.getProducts();
		if (products.length == 0)
			return new Product[0];
		return Arrays.stream(products)
				.sorted(Comparator.comparingDouble(Product::getPrice))
				.limit(n)
				.toArray(Product[]::new);
	}

	/**
	 * Finds the specified number of most expensive products in the shopping cart.
	 *
	 * @param n the number of most expensive products to find
	 * @return an array of the n most expensive products
	 */
	public Product[] findNMostExpensiveProducts(int n) {
		Product[] products = productArray.getProducts();
		if (products.length == 0)
			return new Product[0];
		return Arrays.stream(products)
				.sorted(Comparator.comparingDouble(Product::getPrice).reversed())
				.limit(n)
				.toArray(Product[]::new);
	}

	/**
	 * Returns the products in the shopping cart.
	 *
	 * @return an array of the products in the cart
	 */
	public Product[] getProducts() {
		return productArray.getProducts();
	}
}