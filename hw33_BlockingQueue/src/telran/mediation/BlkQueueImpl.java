package telran.mediation;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlkQueueImpl<T> implements BlkQueue<T> {

    private LinkedList<T> messages;
    private final int maxSize;
    private Lock mutex = new ReentrantLock();
    private Condition conCons = mutex.newCondition();
    private Condition conProd = mutex.newCondition();


    public BlkQueueImpl(int maxSize) {
        this.messages = new LinkedList<>();
        this.maxSize = maxSize;
//        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void push(T message) throws InterruptedException {

        mutex.lock();
        try {
            while (!(messages.size() < maxSize)){
                conProd.await();
            }
            messages.push(message);
            System.out.println(message + " <== producer " + Thread.currentThread().getId());
            conCons.signal();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Not implemented");
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public T pop() {
        mutex.lock();
        try {
            while (messages.isEmpty()){
                conCons.await();
            }
            conProd.signal();
            T currMes = messages.removeFirst();
            System.out.println(currMes + " ==> consumer " + Thread.currentThread().getId());
            return currMes;

        } catch (Exception e) {
            throw new UnsupportedOperationException("Not implemented");
        } finally {
            mutex.unlock();
        }
    }
}
