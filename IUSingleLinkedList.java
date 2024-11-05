import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Morgan Zabriskie
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;
    private int size;
    private int modCount;

    /** Constructor for the IUSingleLinkedList class
     * Instantiates the head, tail, size, and modCount
     * Creates an empty list */
    public IUSingleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        if (size == 0) { // empty list
            Node<T> newNode = new Node<T>(element);
            head = newNode;
            tail = newNode;
            size++;
            modCount++;
        } else {
            Node<T> newNode = new Node<T>(element);
            newNode.setNext(head);
            head = newNode;
            size++;
            modCount++;
        }

    }

    @Override
    public void addToRear(T element) {
        //check list is empty
        if (size == 0) {
            Node<T> newNode = new Node<T>(element);
            head = newNode;
            tail = newNode;
            size++;
            modCount++;
        } else {
            Node<T> newNode = new Node<T>(element);
            tail.setNext(newNode);
            tail = newNode;
            size++;
            modCount++;
        }
    }

    @Override
    public void add(T element) {
        // check list is empty
        if (size == 0) {
            Node<T> newNode = new Node<T>(element);
            head = newNode;
            tail = newNode;
            size++;
            modCount++;
        } else {
            Node<T> newNode = new Node<T>(element);
            tail.setNext(newNode);
            tail = newNode;
            size++;
            modCount++;
        }
    }

    @Override
    public void addAfter(T element, T target) {
        //check for empty list
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            Node<T> currNode = head;
            int currIndex = 0;
            int targetIndex = -1;
            Node<T> targetNode = null;
            for(int i = 0; i < size; i++) { // find node that is target index
                if(currNode.getElement().equals(target)) {
                    targetNode = currNode;
                    targetIndex = currIndex;
                    break;
                } else {
                    currNode = currNode.getNext();
                    currIndex++;
                }
            }

            if(targetIndex == -1) {
                throw new NoSuchElementException();
            } else if (targetIndex == (size - 1)) { // if target is tail
                Node<T> newNode = new Node<T>(element);
                tail.setNext(newNode);
                tail = newNode;
                size++;
                modCount++;
            } else {
                Node<T> newNode = new Node<T>(element);
                newNode.setNext(targetNode.getNext());
                targetNode.setNext(newNode);
                size++;
                modCount++;
            }
        }
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        } else { 
            if (size == 0) { // check for empty list
                Node<T> newNode = new Node<T>(element);
                head = newNode;
                tail = newNode;
                size++;
                modCount++;
            } else if (size > 0 && index == 0) { // check for adding at head
                Node<T> newNode = new Node<T>(element);
                newNode.setNext(head);
                head = newNode;
                size++;
                modCount++;
            } else if (index == size) { // check for adding at tail
                Node<T> newNode = new Node<T>(element);
                tail.setNext(newNode);
                tail = newNode;
                size++;
                modCount++;
            } else {
                Node<T> targetNode = head;
                Node<T> currNode = head;

                for(int i = 0; i < index; i++) {
                    targetNode = currNode;
                    currNode = currNode.getNext();
                }

                Node<T> newNode = new Node<T>(element);
                newNode.setNext(currNode); // target node is before new node
                targetNode.setNext(newNode);
                size++;
                modCount++;
            }
        }
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            Node<T> returnNode = head;
            head = returnNode.getNext();
            size--;
            modCount++;
            return returnNode.getElement();
        }
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            Node<T> targetNode = head;
            Node<T> currNode = head;

            for (int i = 0; i < (size - 1); i++) {
                targetNode = currNode;
                currNode = currNode.getNext();
            }
            T returnElement = currNode.getElement();
            targetNode.setNext(null);
            tail = targetNode;
            size--;
            modCount++;
            return returnElement;
        }
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        boolean found = false;
        Node<T> previous = null;
        Node<T> current = head;

        while (current != null && !found) {
            if (element.equals(current.getElement())) {
                found = true;
            } else {
                previous = current;
                current = current.getNext();
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }

        if (size() == 1) { // only node
            head = tail = null;
        } else if (current == head) { // first node
            head = current.getNext();
        } else if (current == tail) { // last node
            tail = previous;
            tail.setNext(null);
        } else { // somewhere in the middle
            previous.setNext(current.getNext());
        }

        size--;
        modCount++;

        return current.getElement();
    }

    @Override
    public T remove(int index) {
        T returnElement = null;
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else if (index == 0) { // if removing head
            returnElement = head.getElement();
            head = head.getNext();
            if (size == 1) { // check for 1 element list
                tail = head;
            }
            size--;
            modCount++;
        } else {
            Node<T> currNode = head;
            Node<T> targetNode = head;
            for(int i = 0; i < index; i++) {
                targetNode = currNode;
                currNode = currNode.getNext();
            }
            returnElement = currNode.getElement(); //check if this should be target node or current node
            
            if (currNode == tail) { // check if tail needs to be updated
                targetNode.setNext(null);
                tail = targetNode;
            } else {
                targetNode.setNext(currNode.getNext());
            }
            size--;
            modCount++;
        }
        return returnElement;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else if (index == 0) { // if index is head
            head.setElement(element);
        } else if (index == (size - 1)) { // if index is tail
            tail.setElement(element);
        } else {
            Node<T> currNode = null;
            currNode = head.getNext();
            int nodeIndex = 1;
            while(nodeIndex != index) {
                currNode = currNode.getNext();
                nodeIndex++;
            }
            currNode.setElement(element);
        }
        modCount++;
    }

    @Override
    public T get(int index) {
        T element;
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else if (index == 0) { // if index is head
            element = head.getElement();
        } else if (index == (size - 1)) { // if index is tail
            element = tail.getElement();
        } else {
            Node<T> currNode = null;
            currNode = head.getNext();
            int nodeIndex = 1;
            while(nodeIndex != index) {
                currNode = currNode.getNext();
                nodeIndex++;
            }
            element = currNode.getElement();
        }
        return element;
    }

    @Override
    public int indexOf(T element) {
        int index = -1;
        Node<T> currNode = head;
        for(int i = 0; i < size; i++) {
            if (currNode.getElement().equals(element)) {
                index = i;
                break;
            }
            currNode = currNode.getNext();
        }
            return index;
    }

    @Override
    public T first() {
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            return head.getElement();
        }
    }

    @Override
    public T last() {
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            return tail.getElement();
        }
    }

    @Override
    public boolean contains(T target) {
        boolean targetExists = false;
        Node<T> currNode = head;
        for (int i = 0; i < size; i++) {
            if (currNode.getElement().equals(target)) {
                targetExists = true;
                break;
            }
            currNode = currNode.getNext();
        }
        return targetExists;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        Node<T> currNode = head;
        if (size == 0) {
            String returnVal = "[ ]";
            return returnVal;
        } else {
            String returnVal = "[";
            for (int i = 0; i < size; i++) {
                if (i == (size - 1)) {
                    returnVal += currNode.getElement() + "]";
                } else {
                    returnVal += currNode.getElement() + ", ";
                    currNode = currNode.getNext();
                }
            }

            return returnVal;
        }
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> newIterator = new SLLIterator<T>();
        return newIterator;
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException();
    }

    /** Iterator for IUSingleLinkedList */
    private class SLLIterator<E extends T> implements Iterator<T> {
        private boolean nextCalled = false;
        private Node<T> nextNode;
        private Node<T> prevNode;
        private int iterModCount;
        private int index = -1;

        /** Creates a new iterator for the list */
        public SLLIterator() {
            nextNode = head;
            this.iterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = false;
            if(modCount != iterModCount) {
                throw new ConcurrentModificationException();
            } else if (size == 0) {
                hasNext = false;
            } 
            else if (nextNode == null) {
                hasNext = false;
            } else {
                    hasNext = true;
            }
                
            return hasNext;
        }

        @Override
        public T next() {
            if (modCount != iterModCount) {
                throw new ConcurrentModificationException();
            } else {
                T next = null;
                if(size == 0 ) {
                    throw new NoSuchElementException();
                } else if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                else {
                    next = nextNode.getElement();
                    prevNode = nextNode;
                    nextNode = nextNode.getNext();
                    nextCalled = true;
                    index++;
                    return next;
                }
            }
        }

        @Override
        public void remove() {
            if(modCount != iterModCount) {
                throw new ConcurrentModificationException();
            } else {
                if(!nextCalled) {
                    throw new IllegalStateException();
                } else {
                    if(size == 1) {
                        head = null;
                        tail = null;
                        size--;
                        iterModCount++;
                        modCount++;
                        nextCalled = false;
                    } else if(prevNode == head) {
                        head = head.getNext();
                        prevNode.setNext(null);
                        prevNode = head;
                        size--;
                        iterModCount++;
                        modCount++;
                        nextCalled = false;
                    } else if (prevNode == tail) {
                        Node<T> targetNode = head;
                        for(int i = 1; i < (size - 1); i++) {
                            targetNode = targetNode.getNext();
                        }
                        targetNode.setNext(null);
                        tail = targetNode;
                        size--;
                        modCount++;
                        iterModCount++;
                        nextCalled = false;
                    } else {
                        Node<T> targetNode = head;
                        for (int i = 1; i < index; i++) {
                            targetNode = targetNode.getNext();
                        }
                        targetNode.setNext(prevNode.getNext());
                        prevNode.setNext(null);
                        size--;
                        index--;
                        modCount++;
                        iterModCount++;
                        nextCalled = false;
                    }
                }
            }
        }
    }
}