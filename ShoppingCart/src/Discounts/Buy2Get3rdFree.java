package src.Discounts;

import src.Product;
import src.ProductArray;
import src.PromotionCommand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The Buy2Get3rdFree class implements a promotion where if a customer buys
 * multiple sets of 3 products, they get the cheapest product from each set for free.
 */
public class Buy2Get3rdFree implements PromotionCommand {

	@Override
	public void apply(ProductArray productArray) {
		Product[] products = productArray.getProducts();
		if (products.length < 3)
			return;

		// Filter out FreeGiftPromotion products
		List<IndexedProduct> eligibleProducts = new ArrayList<>();
		for (int i = 0; i < products.length; i++) {
			if (!(products[i].getCode().startsWith("GIFT-"))) {
				eligibleProducts.add(new IndexedProduct(products[i], i));
			}
		}

		// If we don't have at least 3 eligible products, exit
		if (eligibleProducts.size() < 3)
			return;

		// Calculate how many sets of 3 products we have from eligible products
		int sets = eligibleProducts.size() / 3;

		// Sort by price ascending to find the cheapest products
		eligibleProducts.sort(Comparator.comparingDouble(p -> p.product.getPrice()));

		// Make the cheapest 'sets' products free in the original array
		for (int i = 0; i < sets; i++) {
			products[eligibleProducts.get(i).index].setDiscountPrice(0);
		}
	}

	private static class IndexedProduct {
		Product product;
		int index;

		IndexedProduct(Product product, int index) {
			this.product = product;
			this.index = index;
		}
	}

	@Override
	public void reset() {
	}
}