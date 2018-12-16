public class DoubleLinkedList<T> {
    private DoubleLinkedNode <T> head;
    private int length;

    public T get (int index) {
        if (length == 0) {
            return null;
        } if (index<0){
            return get(length-1);
        }
        DoubleLinkedNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    public DoubleLinkedList (DoubleLinkedNode<T> head){
        this.head = head;
        head.setPrev(head);
        head.setNext(head);
        length = 1;
    }

    public void add (T data) {
        DoubleLinkedNode<T> newEnd = new DoubleLinkedNode<T>(data);
        if (head != null) {
            DoubleLinkedNode<T> end = head.getPrev();
            end.setNext(newEnd);
            newEnd.setPrev(end);
            newEnd.setNext(head);
            head.setPrev(newEnd);

        } else {
            head = newEnd;
            newEnd.setPrev(newEnd);
            newEnd.setNext(newEnd);
        }
        length +=1;
    }
}
