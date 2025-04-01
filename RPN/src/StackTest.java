import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StackTest {

    private Stack<String> stack;

    @BeforeEach
    void setUp() { stack = new Stack<>(); }

    @Test
    void newStackIsEmpty() { assertTrue(stack.isEmpty()); }

    @Test
    void pushAndPopSingleElement() {
        stack.push("test");
        assertFalse(stack.isEmpty());
        assertEquals("test", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void pushAndPopMultipleElements() {
        stack.push("first");
        stack.push("second");
        stack.push("third");

        assertEquals("third", stack.pop());
        assertEquals("second", stack.pop());
        assertEquals("first", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void peekReturnsTopElement() {
        stack.push("first");
        stack.push("second");

        assertEquals("second", stack.peek());
        assertEquals("second", stack.peek()); // Verify peek doesn't remove the element
        assertEquals(2, countElements());
    }

    @Test
    void popEmptyStackThrowsException() {
        assertThrows(IllegalStateException.class, () -> stack.pop());
    }

    @Test
    void peekEmptyStackThrowsException() {
        assertThrows(IllegalStateException.class, () -> stack.peek());
    }

    @Test
    void resizeStackWhenFull() {
        // Fill initial capacity (10 elements)
        for (int i = 0; i < 15; i++)
            stack.push("Element " + i);

        // Verify the top element first
        assertEquals("Element 14", stack.peek());

        // Then verify we can add more than initial capacity
        assertEquals(15, countElements());
    }

    // Helper method to count elements in stack
    private int countElements() {
        int count = 0;
        while (!stack.isEmpty()) {
            stack.pop();
            count++;
        }
        return count;
    }
}