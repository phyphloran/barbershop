package services.impl;

import services.BarberShopService;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BarberShopServiceImpl implements BarberShopService {

    private int waitingClients = 0;

    private final int chairs;
    private final Lock lock = new ReentrantLock();
    private final Condition clientArrived = lock.newCondition();

    public BarberShopServiceImpl(int chairs) {
        this.chairs = chairs;
    }

    @Override
    public void clientCome(long id) {
        lock.lock();
        try {
            if (waitingClients == chairs) {
                System.out.println("Client " + id + " left (no free chairs)");
                return;
            }
            waitingClients++;
            System.out.println("Client " + id + " is waiting. Waiting: " + waitingClients);
            clientArrived.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void barberWork() {
        while (true) {
            lock.lock();
            try {
                while (waitingClients == 0) {
                    System.out.println("Barber is sleeping...");
                    clientArrived.await();
                }
                waitingClients--;
                System.out.println("Barber starts haircut. Waiting: " + waitingClients);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
            cutHair();
        }
    }

    private void cutHair() {
        try {
            System.out.println("Barber cutting hair...");
            Thread.sleep(2000);
            System.out.println("Haircut finished");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}