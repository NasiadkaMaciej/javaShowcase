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

	public void removeProduct(String code) {
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
			products = newProducts;
		}
	}

	public boolean isEmpty() { return products.length == 0; }
}