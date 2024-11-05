import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Morgan Zabriskie
 *
 * @param <T> type to store
 */

 public class IUArrayList<T> implements IndexedUnsortedList<T> {

     private static final int DEFAULT_CAPACITY = 10;

     private T[] array;
     private int rear;
     private int modCount;

     /** Creates an empty list with default initial capacity */
     public IUArrayList() {
         this(DEFAULT_CAPACITY);
     }

     /**
      * Creates an empty list with the given initial capacity
      * 
      * @param initialCapacity
      */
     @SuppressWarnings("unchecked")
     public IUArrayList(int initialCapacity) {
         array = (T[]) (new Object[initialCapacity]);
         rear = 0;
         modCount = 0;
     }

     /** Double the capacity of array */
     private void expandCapacity() {
         array = Arrays.copyOf(array, array.length * 2);
     }

    @Override
    public void addToFront(T element) { 
        // check array length and increase if rear is in last spot of array
        if (rear == (array.length - 2)) {
            expandCapacity();
        }
        for(int i = rear; i > 0; i--) {
            array[i] = array[i - 1];
            modCount++;
        }
        array[0] = element;
        modCount++;
        rear++;
    }

    @Override
    public void addToRear(T element) {
        // check array length and increase if rear is in last spot of array
        if (rear == (array.length - 2)) {
            expandCapacity();
        }
        array[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(T element) {
        // check array length and increase if rear is in last spot of array
        if (rear == (array.length - 2)) {
            expandCapacity();
        }
        array[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) {
        // check array length and increase if rear is in last spot of array
        if (rear == (array.length - 2)) {
            expandCapacity();
        }

        // find location of target element
        int targetLocation = -1;
        for (int i = 0; i < rear; i++) {
            if (array[i] == target) {
                targetLocation = i;
                break;
            }
        }

        // add new element after target element
        if(targetLocation == -1) {
            throw new NoSuchElementException();
        } else if(targetLocation == 0 && rear == 1) {
            array[rear] = element;
            modCount++;
            rear++;
        } else {
            for (int i = rear; i >= (targetLocation + 1); i--) {
                array[i] = array[i - 1];
                modCount++;
            }
            array[targetLocation + 1] = element;
            modCount++;
            rear++;
        }
    }

    @Override
    public void add(int index, T element) {
        // check array length and increase if rear is in last spot of array
        if (rear == (array.length - 2)) {
            expandCapacity();
        }

        // check index is possible to set something at
        if ((index < 0) || (index > size())) {
            throw new IndexOutOfBoundsException();
        } else {
            // shift elements at and after index to the right and insert new value at given index
            for (int i = rear; i > index; i--) {
                array[i] = array[i - 1];
                modCount++;
            }
            array[index] = element;
            modCount++;
            rear++;
        }
    }

    @Override
    public T removeFirst() {
        if (array[0] == null) {
            throw new NoSuchElementException();
        } else {
            T element = array[0];
            modCount++;

            for (int i = 0; i < rear; i++) {
                array[i] = array[i + 1];
                modCount++;
            }
            // check you aren't setting rear to a negative number
            if (rear > 0) {
                rear--;
            } else {
                rear = 0;
            }
        array[rear] = null;
        return element;
        }
    }

    @Override
    public T removeLast() {
        if (array[0] == null) {
            throw new NoSuchElementException();
        } else {
            T element = array[rear - 1];
            array[rear - 1] = null;
            modCount++;

            // check you aren't setting rear to a negative number
            if (rear > 0) {
                rear--;
            } else {
                rear = 0;
            }
            return element;
        }
    }

    @Override
    public T remove(T element) {
        int elementLocation = -1;
        T returnElement = element;

        // find location of target element
        for(int i = 0; i < rear; i++) {
            if (array[i] == element) {
                elementLocation = i;
                returnElement = array[i];
                break;
            }
        }

        // move array elements to the left starting at target index
        if (elementLocation == -1) {
            throw new NoSuchElementException();
        } else {
            for(int i = elementLocation; i < rear; i++) {
                array[i] = array[i + 1];
                modCount++; 
            }
            // check you aren't setting rear to a negative number
            if (rear > 0) {
                rear--;
            } else {
                rear = 0;
            }
        }

        return returnElement;
    }

    @Override
    public T remove(int index) {
        T element = array[index];

        // check index is within possible range
        if ((index < 0) || (index >= size())) {
            throw new IndexOutOfBoundsException();
        } else {
            // remove element at index and shift elements after index to the left
            for(int i = index; i < rear; i++) {
                array[i] = array[i + 1];
                modCount++;
            }
            // check you aren't setting rear to a negative number
            if (rear > 0) {
                rear--;
            } else {
                rear = 0;
            }
        }

        return element;
    }

    @Override
    public void set(int index, T element) {
        if ((index < 0) || (index >= size())) {
            throw new IndexOutOfBoundsException();
        } else {
            array[index] = element;
            modCount++;
        }
    }

    @Override
    public T get(int index) {
        if ((index < 0) || (index >= size())) {
            throw new IndexOutOfBoundsException();
        } else {
            return array[index];
        }
    }

    @Override
    public int indexOf(T element) {
        int index = -1;

        for(int i = 0; i < rear; i++) {
            if (array[i] == element) {
                index = i;
                break;
            }
        }

        return index;
    }

    @Override
    public T first() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return array[0];
        }
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return array[rear - 1];
        }
    }

    @Override
    public boolean contains(T target) {
        boolean contains = false;

        for(int i = 0; i < rear; i++) {
            if(array[i] == target) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    @Override
    public boolean isEmpty() {
        boolean isEmpty = true;

        for(int i = 0; i < rear; i++) {
            if (array[i] != null) {
                isEmpty = false;
                break;
            }
        }

        return isEmpty;
    }

    @Override
    public int size() {
        int size = 0;

        for(int i = 0; i < rear; i++) {
            if (array[i] != null) {
                size++;
            }
        }

        return size;
    }

    @Override
    public String toString() {
        if (array[0] == null) {
            String returnVal = "[ ]";
            return returnVal;
        } else {
            String returnVal = "[";
            for (int i = 0; i < rear; i++) {
                if (i == (rear - 1)) {
                    returnVal += array[i] + "]";
                } else {
                    returnVal += array[i] + ", ";
                }
            }

            return returnVal;
        }
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> newIterator = new ArrayIterator<T>();
        return newIterator;
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
     * Private inner class for iterators in the IUArrayList class
     * 
     * @author Morgan Zabriskie
     *
     * @param <E> type to store
     */
    
    private class ArrayIterator<E extends T> implements Iterator<T> {

        // instance variables
        private boolean nextCalled = false;
        private int index = -1;
        private int iterModCount;

        public ArrayIterator() {
            this.iterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            if(modCount != iterModCount) {
                throw new ConcurrentModificationException();
            } else {
                boolean hasNext = false;
                if(array[this.index + 1] != null) {
                    hasNext = true;
                }

                return hasNext;
            }
        }

        @Override
        public T next() {
            if(modCount != iterModCount) {
                throw new ConcurrentModificationException();
            } else {
                // account for empty list
                if(array[0] == null) {
                    throw new NoSuchElementException();
                    // account for end of list
                } else if (array[this.index + 1] == null) {
                    throw new NoSuchElementException();
                } else {
                    this.index++;
                    T next = array[this.index];
                    nextCalled = true;
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
                    for (int i = this.index; i < rear; i++) {
                        array[i] = array[i + 1];
                        modCount++;
                        iterModCount++;
                    }
                    // check you aren't setting rear to a negative number
                    if (rear > 0) {
                        rear--;
                    } else {
                        rear = 0;
                    }
                    this.index--;
                    nextCalled = false;
                }
            }
        }
    }
 }