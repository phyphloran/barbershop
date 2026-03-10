import services.BarberShopService;
import services.impl.BarberShopServiceImpl;


public class Main {

    private static final int CHAIRS_COUNT = 3;
    private static final int CLIENTS_COUNT = 15;
    private static final int CLIENT_INTERVAL = 300;
    private static final BarberShopService shop = new BarberShopServiceImpl(CHAIRS_COUNT);

    public static void main(String[] args) throws InterruptedException {
        Thread barberThread = new Thread(
                () -> shop.barberWork()
        );
        barberThread.start();
        for (int i = 1; i <= CLIENTS_COUNT; i++) {
            int clientId = i;
            new Thread(() -> shop.clientCome(clientId)).start();
            Thread.sleep(CLIENT_INTERVAL);
        }
    }
}