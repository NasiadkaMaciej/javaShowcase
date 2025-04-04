package src;

public interface PromotionCommand {
	void apply(ProductArray productArray);

	void reset();
}