package telran.actors;

import telran.mediation.BlkQueue;

public class MsgProducer extends Thread{
    BlkQueue<String> blkQueue;
    int nMessages;
    int sendIntervalMillis;

    public MsgProducer(BlkQueue<String> blkQueue, int nMessages, int sendIntervalMillis) {
        super();
        this.blkQueue = blkQueue;
        this.nMessages = nMessages;
        this.sendIntervalMillis = sendIntervalMillis;
    }

    @Override
    public void run() {
        for (int i = 0; i < nMessages; i++) {
            try {
                Thread.sleep(sendIntervalMillis);
            } catch (InterruptedException e) {
                // noop
            }
            String message = "message#" + i;
            try {
                blkQueue.push(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            System.out.println(message + " <== producer " + getId());
        }
    }
}
