public class DoubleLinkedNode<T> {
    private T data;
    private DoubleLinkedNode<T> prev, next;


    public DoubleLinkedNode (T data) {
        this.data = data;
    }

    public DoubleLinkedNode<T> getPrev () {
        return this.prev;
    }

    public DoubleLinkedNode <T> getNext () {
        return this.next;
    }


    public void setPrev (DoubleLinkedNode<T> node) {
        prev = node;
    }

    public void setNext (DoubleLinkedNode<T> node) {
        next = node;
    }

    public T getData () {
        return this.data;
    }
}
