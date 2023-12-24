import java.util.Iterator;

public class LList<T> implements Iterator {
    Node<T> head;
    int size;
    Node<T> iterate;

    public LList(Node<T> head) {
        this.head = head;
    }

    public LList(T val) {
        this.head = new Node<>(val);
    }

    private Node<T> iterator;

    public LList() {

    }

    @Override
    public boolean hasNext() {
        return iterator.next != null;
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            iterator = head;
            return iterator;
        }
        iterator = iterator.next;
        return iterator;
    }
}
