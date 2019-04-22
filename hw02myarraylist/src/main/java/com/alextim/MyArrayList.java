package com.alextim;

import org.omg.CORBA.BooleanHolder;

import java.util.*;

public class MyArrayList<E> implements List<E> {

    private Object[] data;
    private int size;
    private final int INIT_CAPACITY = 10;

    public MyArrayList() {
        data = new Object[INIT_CAPACITY];
    }

    public MyArrayList(int capacity) {
        if(capacity > 0)
            data = new Object[capacity];
        else
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);

        data[size++] = e;
        return true;
    }

    @Override
    public void add(int index, E element) {
        ensureCapacity(size + 1);

        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        ensureCapacity(size + c.size());

        E[] tempArray =  (E[]) new Object[size + c.size()];

        System.arraycopy(data, 0, tempArray, 0, index);
        System.arraycopy(c.toArray(), 0, tempArray, index, c.size());
        System.arraycopy(data, index, tempArray, index + c.size(), size - index);

        data = tempArray;
        size = tempArray.length;
        return true;
    }

    private void ensureCapacity(int targetSize) {
        if (targetSize - data.length > 0) {
            data = Arrays.copyOf(data, targetSize > 2*size ? targetSize : 2*size);
        }
    }

    @Override
    public E get(int index) {
        checkIndex(index);

        return (E) data[index];
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);

        data[index] = element;
        return (E)data[index];
    }

    @Override
    public int indexOf(Object o) {
        for(int i=0; i<size; i++)
            if(data[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i=size-1; i>=0; i++)
            if(data[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = (E)data[index];
        System.arraycopy(data, index+1, data, index, size - index - 1);
        size--;
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index != -1) {
            remove(index);
            return true;
        }
        return  false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        BooleanHolder modified = new BooleanHolder(false);
        c.forEach(action-> { if(remove(action)) modified.value = true; });
        return modified.value;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for(int i = 0; i<size; i++) {
            if(!c.contains(data[i])) {
                remove(i--);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        size = 0;
        data = new Object[INIT_CAPACITY];
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if(a.length != size)
            return null;
        else
            System.arraycopy(data, 0, a, 0, size);
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyListIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new MyListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new MyListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if(toIndex <= fromIndex)
            throw new IllegalArgumentException();

        Object[] dest = new Object[toIndex-fromIndex];
        System.arraycopy(data, fromIndex, dest, 0, toIndex - fromIndex);

        return new ArrayList<>((Collection<? extends E>) Arrays.asList(dest));
    }

    private class MyListIterator implements ListIterator<E> {

        private int index = -1;

        public MyListIterator() {
        }

        public MyListIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < size - 1;
        }

        @Override
        public E next() {
            if(hasNext())
                return (E)data[++index];
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public E previous() {
            if(hasPrevious())
                return (E)data[--index];
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            if(hasNext())
                return index + 1;
            else
                return -1;
        }

        @Override
        public int previousIndex() {
            if(hasPrevious())
                return index - 1;
            else
                return -1;
        }

        @Override
        public void remove() {
            MyArrayList.this.remove(index);
        }

        @Override
        public void set(E e) {
            MyArrayList.this.set(index, e);
        }

        @Override
        public void add(E e) {
            MyArrayList.this.add(e);
        }
    }

    private void checkIndex(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException( "Index: " + index + ", Size: " + size);
    }
}
