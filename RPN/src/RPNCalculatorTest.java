import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RPNCalculatorTest {

    private RPNCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RPNCalculator();
    }

    @Test
    void addition() {
        assertEquals(3, calculator.evaluate("1 2 +"));
    }

    @Test
    void substraction() {
        assertEquals(-1, calculator.evaluate("3 4 -"));
    }

    @Test
    void multiplication() {
        assertEquals(30, calculator.evaluate("5 6 *"));
    }

    @Test
    void complexExpression() {
        // (7 + 8) * 9 = 135
        assertEquals(135, calculator.evaluate("7 8 + 9 *"));
    }

    @Test
    void moreComplexExpression() {
        // 1 + (2 * 3) = 7
        assertEquals(7, calculator.evaluate("1 2 3 * +"));
    }

    @Test
    void invalidExpressions() {
        assertThrows(IllegalArgumentException.class, () -> calculator.evaluate("4 5"));
        assertThrows(IllegalArgumentException.class, () -> calculator.evaluate("6 +"));
        assertThrows(IllegalArgumentException.class, () -> calculator.evaluate("3 4 $"));

    }

    @Test
    void emptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> calculator.evaluate(""));
    }
}