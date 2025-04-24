package src;

import java.util.Arrays;

/**
 * Wrapper class for Product array to allow modification of the array content
 * while maintaining the same reference.
 */
public class ProductArray {
	private Product[] products;

	public ProductArray(Product[] products) { this.products = products; }

	public Product[] getProducts() { return products; }

	public void setProducts(Product[] products) { this.products = products; }

	public void addProduct(Product product) {
		Product[] newProducts = Arrays.copyOf(products, products.length + 1);
		newProducts[products.length] = product;
		products = newProducts;
	}
}