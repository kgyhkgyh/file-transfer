/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-2-28   comment
 * linxueqin  2015-2-28  Created
 */
package com.apusic.adxp.agent.netty.adxp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author linxueqin
 *
 */
public class TimeoutList<E> extends TimerTask implements List<E> {

    /**
     * 超时时间
     */
    private long timeout = 0;
    
    /**
     * 任务队列
     */
    private List<E> list = Collections.synchronizedList(new ArrayList<E>());
    
    /**
     * 超时队列
     */
    private List<Integer> counterList = new ArrayList<Integer>();
    
    /**
     * 任务锁
     */
    private ReentrantLock lock = new ReentrantLock();
    
    /**
     * 
     */
    private Condition condition = lock.newCondition();
    
    public TimeoutList(long timeout) {
        this.timeout = timeout;
        Timer timer = new Timer();
        timer.schedule(this, 0, 1000);
    }
    
    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        if(list.size() == 0) {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        for(int i = 0 ; i < list.size() ; i++) {
            Integer count = counterList.get(i);
            if(count > timeout) {
                list.remove(i);
                counterList.remove(i);
            }else {
                count++;
                counterList.set(i, count);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see java.util.List#size()
     */
    public int size() {
        return list.size();
    }

    /* (non-Javadoc)
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return list.contains(o);
    }

    /* (non-Javadoc)
     * @see java.util.List#iterator()
     */
    public Iterator<E> iterator() {
        return list.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return list.toArray();
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray(T[])
     */
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(E e) {
        if(list.size() ==0) {
            lock.lock();
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
        counterList.add(0);
        return list.add(e);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        try {
            int indexOf = list.indexOf(o);
            counterList.remove(indexOf);
        } catch (Exception e) {
            return false;
        }
        return list.remove(o);
    }

    /* (non-Javadoc)
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends E> c) {
        if(list.size() ==0) {
            lock.lock();
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
        int index = list.size();
        int size = c.size();
        for(int i = 0 ; i < size ; i++) {
            counterList.add(index, 0);
            index++;
        }
        return list.addAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        if(list.size() ==0) {
            lock.lock();
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
        return list.addAll(index, c);
    }

    /* (non-Javadoc)
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.List#clear()
     */
    public void clear() {
        list.clear();
        counterList.clear();
    }

    /* (non-Javadoc)
     * @see java.util.List#get(int)
     */
    public E get(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    public E set(int index, E element) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, E element) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    public E remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator()
     */
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<E> listIterator(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */
    public List<E> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }

}
