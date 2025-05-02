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
	 */
	public void addProduct(Product product) {
		productArray.addProduct(product);
		sortProducts();
	}

	/**
	 * Removes a product from the shopping cart by its code.
	 */
	public void removeProduct(String code) {
		productArray.removeProduct(code);
		sortProducts();
	}

	/**
	 * Sets the sorting strategy for the products and sorts them.
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

	public void addPromotion(PromotionCommand promotion) { promotions.add(promotion); }

	public void removePromotion(PromotionCommand promotion) { promotions.remove(promotion); }

	/**
	 * Calculates the total price after applying promotions in the optimal order
	 */
	public double calculateTotal() {
		if (productArray.isEmpty()) return 0.0;
		return findOptimalPromotionApplication();
	}

	/**
	 * Determines the optimal order to apply promotions to minimize the total cost.
	 */
	private double findOptimalPromotionApplication() {
		if (promotions.size() <= 1) {
			// If 0 or 1 promotion, just apply normally
			resetAllDiscounts();
			applyPromotions(promotions);
			return sumDiscountPrices();
		}

		// Try all possible permutations of promotion orders
		List<List<PromotionCommand>> permutations = generatePermutations(promotions);
		double lowestTotal = Double.MAX_VALUE;
		List<PromotionCommand> bestOrder = null;

		for (List<PromotionCommand> permutation : permutations) {
			resetAllDiscounts();

			// Apply promotions in this order
			applyPromotions(permutation);

			double total = sumDiscountPrices();
			if (total < lowestTotal) {
				lowestTotal = total;
				bestOrder = permutation;
			}
		}

		// Apply the best permutation one final time
		resetAllDiscounts();
		if (bestOrder != null) { applyPromotions(bestOrder); }

		return lowestTotal;
	}

	private void applyPromotions(List<PromotionCommand> promotionsToApply) {
		for (PromotionCommand promotion : promotionsToApply) {
			promotion.apply(productArray);
		}
	}

	private void resetAllDiscounts() {
		for (PromotionCommand promotion : promotions) {
			promotion.reset();
		}
		for (Product product : productArray.getProducts()) {
			product.resetDiscountPrice();
		}
	}

	private double sumDiscountPrices() {
		return Arrays.stream(productArray.getProducts()).mapToDouble(Product::getDiscountPrice).sum();
	}

	/**
	 * Generates all possible permutations of the given list.
	 */
	private <T> List<List<T>> generatePermutations(List<T> list) {
		List<List<T>> result = new ArrayList<>();
		permuteHelper(new ArrayList<>(list), 0, result);
		return result;
	}

	private <T> void permuteHelper(List<T> list, int start, List<List<T>> result) {
		if (start == list.size() - 1) {
			result.add(new ArrayList<>(list));
			return;
		}

		for (int i = start; i < list.size(); i++) {
			swap(list, start, i);
			permuteHelper(list, start + 1, result);
			swap(list, start, i); // backtrack
		}
	}

	private <T> void swap(List<T> list, int i, int j) {
		T temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}

	public double getTotalBeforePromotions() {
		return Arrays.stream(productArray.getProducts()).mapToDouble(Product::getPrice).sum();
	}

	public Product findCheapestProduct() {
		Product[] products = productArray.getProducts();
		if (productArray.isEmpty()) return null;
		return Arrays.stream(products).min(ProductComparators.BY_PRICE_ASC).orElse(null);
	}

	public Product findMostExpensiveProduct() {
		Product[] products = productArray.getProducts();
		if (productArray.isEmpty()) return null;
		return Arrays.stream(products).max(ProductComparators.BY_PRICE_ASC).orElse(null);
	}

	public Product[] findNCheapestProducts(int n) {
		Product[] products = productArray.getProducts();
		if (productArray.isEmpty()) return new Product[0];
		return Arrays.stream(products)
		  .sorted(ProductComparators.BY_PRICE_ASC)
		  .limit(n)
		  .toArray(Product[] ::new);
	}

	public Product[] findNMostExpensiveProducts(int n) {
		Product[] products = productArray.getProducts();
		if (productArray.isEmpty()) return new Product[0];
		return Arrays.stream(products)
		  .sorted(ProductComparators.BY_PRICE_DESC)
		  .limit(n)
		  .toArray(Product[] ::new);
	}

	public Product[] getProducts() { return productArray.getProducts(); }
}