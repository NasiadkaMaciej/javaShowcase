import java.util.Arrays;

public class Stack<T> {
    private T[] elements;
    private int size;

    public Stack() {
        elements = (T[]) new Object[10]; // Initial capacity
        size = 0;
    }

    public void push(T value) {
        if (size == elements.length)
            elements = Arrays.copyOf(elements, elements.length * 2); // Dynamic resizing
        elements[size++] = value;
    }

    public T pop() {
        if (size == 0)
            throw new IllegalStateException("Stack is empty");
        return elements[--size];
    }

    public T peek() {
        if (size == 0)
            throw new IllegalStateException("Stack is empty");
        return elements[size - 1];
    }

    public boolean isEmpty() { return size == 0; }
}