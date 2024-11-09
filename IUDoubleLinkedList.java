import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented.
 * A ListIterator with working add(), remove(), and set()
 * methods is implemented.
 * 
 * @author Morgan Zabriskie
 * 
 * @param <T> type to store
 */

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;
    private int size;
    private int modCount;

    /** Constructor for the IUDoubleLinkedList class
     * Instantiates the head, tail, size, and modCount
     * Creates an empty list */
    public IUDoubleLinkedList() {
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
            head.setPrev(newNode);
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
            Node<T> prevNode = tail;
            tail = newNode;
            newNode.setPrev(prevNode);
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
            Node<T> prevNode = tail;
            tail = newNode;
            newNode.setPrev(prevNode);
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
                Node<T> prevNode = tail;
                tail.setNext(newNode);
                tail = newNode;
                newNode.setPrev(prevNode);
                size++;
                modCount++;
            } else {
                Node<T> newNode = new Node<T>(element);
                newNode.setNext(targetNode.getNext());
                newNode.setPrev(targetNode);
                targetNode.setNext(newNode);
                newNode.getNext().setPrev(newNode);
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
                head.setPrev(newNode);
                newNode.setNext(head);
                head = newNode;
                size++;
                modCount++;
            } else if (index == size) { // check for adding at tail
                Node<T> newNode = new Node<T>(element);
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
                size++;
                modCount++;
            } else {
                Node<T> currNode = head; // new element set before currNode

                for(int i = 0; i < index; i++) {
                    currNode = currNode.getNext();
                }

                Node<T> newNode = new Node<T>(element);
                newNode.setNext(currNode);
                newNode.setPrev(currNode.getPrev());
                currNode.getPrev().setNext(newNode);
                currNode.setPrev(newNode);
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
        } else if (size == 1) {
            T returnElement = tail.getElement();
            tail = null;
            head = null;
            size--;
            modCount++;
            return returnElement;
        } else {
            T returnElement = tail.getElement();
            tail.getPrev().setNext(null);
            tail = tail.getPrev();
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
            current.getNext().setPrev(previous);
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
            returnElement = currNode.getElement();
            
            if (currNode == tail) { // check if tail needs to be updated
                targetNode.setNext(null);
                tail = targetNode;
            } else {
                targetNode.setNext(currNode.getNext());
                currNode.getNext().setPrev(targetNode);
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
        Iterator<T> newIterator = new DLLIterator<T>();
        return newIterator;
    }

    @Override
    public ListIterator<T> listIterator() {
        ListIterator<T> newListIterator = new DLLListIterator<T>();
        return newListIterator;
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        ListIterator<T> newListIterator = new DLLListIterator<T>(startingIndex);
        return newListIterator;
    }

    /** Iterator for IUDoubleLinkedList */
    private class DLLIterator<E extends T> implements Iterator<T> {
        private boolean nextCalled = false;
        private Node<T> nextNode;
        private Node<T> prevNode;
        private int iterModCount;
        private int index = -1;

        /** Constructor - creates a new iterator for the list with iterator starting
         * at index -1
         */
        public DLLIterator() {
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
                        prevNode.getPrev().setNext(prevNode.getNext());
                        prevNode.getNext().setPrev(prevNode.getPrev());
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
    
    /** List Iterator for IUDoubleLinkedList */
    private class DLLListIterator<E extends T> implements ListIterator<T> {
        //instance variables
        private boolean nextCalled = false;
        private boolean prevCalled = false;
        private boolean addCalled = false;
        private boolean removeCalled = false;
        private Node<T> nextNode;
        private Node<T> prevNode;
        private int iterModCount;
        private int index = -1;

        /**
         * Constructor for ListIterator
         */
        public DLLListIterator() {
            nextNode = head;
            iterModCount = modCount;
        }

        /**
         * Constructor -creates a new iterator for the list with iterator starting
         * at index at user given value
         */
        public DLLListIterator(int startingIndex) {
            if(startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            } else {
                if(startingIndex == 0) {
                    index = -1;
                    nextNode = head;
                    iterModCount = modCount;
                } else {
                        Node<T> currentNode = head;
                        for(int i = 0; i < startingIndex; i++) {
                            currentNode = currentNode.getNext();
                        }
                        index = startingIndex - 1;
                        if(currentNode == head) {
                            prevNode = null;
                            nextNode = currentNode;
                        } else if (currentNode == null)  {
                            nextNode = null;
                            prevNode = tail;
                        } else {
                            nextNode = currentNode;
                            prevNode = currentNode.getPrev();
                        }
                        iterModCount = modCount;
                }
            }
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                boolean hasNext = false;
                if (size == 0) {
                    hasNext = false;
                } else if (index == -1) {
                    if(head == null) {
                        hasNext = false;
                    } else {
                        hasNext = true;
                    }
                } else {
                    if (nextNode == null) {
                        hasNext = false;
                    } else {
                        hasNext = true;
                    }
                }
                return hasNext;
            }
        }

        @Override
        public T next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                if (size == 0) {
                    throw new NoSuchElementException();
                } else {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    } else {
                        T next = nextNode.getElement();
                        index++;
                        if(nextNode == tail) { // going to back of list
                            prevNode = nextNode;
                            nextNode = null;
                        } else {
                            prevNode = nextNode;
                            nextNode = nextNode.getNext();
                        }
                        
                        removeCalled = false;
                        addCalled = false;
                        prevCalled = false;
                        nextCalled = true;
                        return next;
                    }
                }
            }
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                boolean hasPrevious = false;
                if(size == 0) {
                    hasPrevious = false;
                } else if (index == -1) {
                    hasPrevious = false;
                } else {
                    if(prevNode == null) {
                        hasPrevious = false;
                    } else {
                        hasPrevious = true;
                    }
                }
                return hasPrevious;
            }
        }

        @Override
        public T previous() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                if(size == 0) {
                    throw new NoSuchElementException();
                } else {
                    if (prevNode == null) {
                        throw new NoSuchElementException();
                    } else {
                        T previous = prevNode.getElement();
                        index--;

                        if(prevNode == head) { // going to front of list
                            nextNode = prevNode;
                            prevNode = null;
                            head = nextNode;
                        } else if (prevNode == tail) {
                            nextNode = prevNode;
                            prevNode = prevNode.getPrev();
                            tail = nextNode;
                        }
                        else {
                            nextNode = prevNode;
                            prevNode = prevNode.getPrev();
                        }

                        prevCalled = true;
                        nextCalled = false;
                        removeCalled = false;
                        addCalled = false;
                        return previous;
                    }
                }
            }
        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                int nextIndex;
                if(size == 0) {
                    nextIndex = 0;
                } else {
                    nextIndex = index + 1;
                }

                return nextIndex;
            }
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                int prevIndex;
                if (prevNode == null) {
                    prevIndex = -1;
                } else {
                    prevIndex = index;
                }

                return prevIndex;
            }
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                 if (nextCalled == false && prevCalled == false) {
                    throw new IllegalStateException();
                 } else {
                    if (removeCalled || addCalled) {
                        throw new IllegalStateException();
                    } else {
                        if(nextCalled) {
                            if (size == 1) {
                                head = null;
                                tail = null;
                                prevNode = null;
                                nextNode = null;
                                index--;
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                nextCalled = false;
                            } else if(prevNode == head) {
                                nextNode.setPrev(null);
                                prevNode = null;
                                head = nextNode;
                                index--;
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                nextCalled = false;
                            } else if(prevNode == tail) {
                                prevNode.getPrev().setNext(null);
                                prevNode = prevNode.getPrev();
                                tail = prevNode;
                                index--;
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                nextCalled = false;
                            } else {
                                prevNode.getPrev().setNext(nextNode);
                                prevNode = prevNode.getPrev();
                                nextNode.setPrev(prevNode);
                                index--;
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                nextCalled = false;
                            }
                        } else if (prevCalled) {
                            if (size == 1) {
                                head = null;
                                tail = null;
                                prevNode = null;
                                nextNode = null;
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                prevCalled = false;
                            } else if (nextNode == head) {
                                nextNode.getNext().setPrev(null);
                                prevNode = null;
                                nextNode = nextNode.getNext();
                                head = nextNode;
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                prevCalled = false;
                            } else if (nextNode == tail) {
                                prevNode.setNext(null);
                                nextNode = null;
                                tail = prevNode;
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                prevCalled = false;
                            } else {
                                prevNode.setNext(nextNode.getNext());
                                nextNode = nextNode.getNext();
                                nextNode.setPrev(prevNode);
                                size--;
                                modCount++;
                                iterModCount++;
                                removeCalled = true;
                                prevCalled = false;
                            }
                        } 
                    }
                 }
            }
        }
            

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                if(removeCalled == true || addCalled == true || size == 0) {
                    throw new IllegalStateException();
                } else {
                    if (nextCalled == true) { // setting next element
                        prevNode.setElement(e);
                        iterModCount++;
                        modCount++;
                    } else if (prevCalled == true) { // setting previous element
                        nextNode.setElement(e);
                        iterModCount++;
                        modCount++;
                    } else { // neither next or previous was called
                        throw new IllegalStateException();
                    }
                }
            }
        }

        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            } else {
                if(size == 0) { // empty list
                    Node<T> newNode = new Node<T>(e);
                    head = newNode;
                    tail = newNode;
                    index++;
                    size++;
                    prevNode = newNode;
                    nextNode = null;
                    iterModCount++;
                    modCount++;
                    addCalled = true;
                    nextCalled = false;
                    prevCalled = false;
                } else if (nextNode == head) { // adding at head
                    Node<T> newNode = new Node<T>(e);
                    newNode.setNext(head);
                    head.setPrev(newNode);
                    head = newNode;
                    prevNode = newNode;
                    index++;
                    size++;
                    iterModCount++;
                    modCount++;
                    addCalled = true;
                    nextCalled = false;
                    prevCalled = false;
                } else if (prevNode == tail) { // adding at tail
                    Node<T> newNode = new Node<T>(e);
                    newNode.setPrev(tail);
                    tail.setNext(newNode);
                    tail = newNode;
                    prevNode = newNode;
                    index++;
                    size++;
                    iterModCount++;
                    modCount++;
                    addCalled = true;
                    nextCalled = false;
                    prevCalled = false;
                } else { // adding in middle of list
                    Node<T> newNode = new Node<T>(e);
                    newNode.setPrev(prevNode);
                    newNode.setNext(nextNode);
                    prevNode.setNext(newNode);
                    nextNode.setPrev(newNode);
                    prevNode = newNode;
                    index++;
                    size++;
                    iterModCount++;
                    modCount++;
                    addCalled = true;
                    nextCalled = false;
                    prevCalled = false;
                }
            }
        }
    }
}