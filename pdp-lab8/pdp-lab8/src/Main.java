import domain.*;
import mpi.MPI;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        DSM dsm = new DSM();
        int me = MPI.COMM_WORLD.Rank();
        if (me == 0) {
            // Created to listen for messages
            Thread thread = new Thread(new Listener(dsm));
            thread.start();

            // Subscribe to variables
            dsm.subscribeTo("a");
            dsm.subscribeTo("b");
            dsm.subscribeTo("c");
            // Called to update the values of a, b and c
            dsm.checkAndReplace("a",0,111);
            dsm.checkAndReplace("b",1,333);
            dsm.checkAndReplace("c",2, 101);
            // Signal the end of communication
            dsm.close();

            thread.join();

        } else if (me == 1) {
            // Created to listen for messages
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            // Subscribe to variable a
            dsm.subscribeTo("a");
            thread.join();
        } else if (me == 2) {
            // Created to listen for messages
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            // Subscribe to variable c
            dsm.subscribeTo("c");
            // Update the value of a
            dsm.checkAndReplace("a", 2, 100);
            // Signal the end of communication
            dsm.close();
            thread.join();
        }
        MPI.Finalize();
    }

    // Responsible for listening to messages in a separate thread
    private static class Listener implements Runnable {
        private final DSM dsm;

        public Listener(DSM dsm) {
            this.dsm = dsm;
        }

        @Override
        public void run() {
            // Continuously listens for messages using the MPI.COMM_WORLD.Recv
            while (true) {
                System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " waiting..");
                Object[] messages = new Object[1];

                MPI.COMM_WORLD.Recv(messages, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, MPI.ANY_TAG);
                Message message = (Message) messages[0];

                if (message instanceof CloseMessage) {
                    System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " stopped listening...");
                    return;
                } else if (message instanceof SubscribeMessage) {
                    SubscribeMessage subscribeMessage = (SubscribeMessage) message;
                    System.out.println("Subscribe message received");
                    System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " received: rank " + subscribeMessage.rank +
                            " subscribes to " + subscribeMessage.var);
                    dsm.syncSubscription(subscribeMessage.var, subscribeMessage.rank);
                } else if (message instanceof UpdateMessage) {
                    UpdateMessage updateMessage = (UpdateMessage) message;
                    System.out.println("Update message received");
                    System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " received:" + updateMessage.var + "->"
                            + updateMessage.val);
                    dsm.setVariable(updateMessage.var, updateMessage.val);
                } else if (message instanceof ErrorMessage) {
                    ErrorMessage errorMessage = (ErrorMessage) message;
                    System.out.println("ErrorMessage message received");
                    System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " received: Process " + errorMessage.rank
                            + " tried to update unavailable variable " + errorMessage.var);
                }
                writeAll(dsm);
            }
        }
    }

    // Generates a string representation of the current state of shared variables and subscribers and prints it
    public static void writeAll(DSM dsm) {
        StringBuilder sb = new StringBuilder();
        sb.append("Write all\n");
        sb.append("Rank ").append(MPI.COMM_WORLD.Rank()).append("-> a = ").append(dsm.a).append(", b = ")
                .append(dsm.b).append(", c = ").append(dsm.c).append("\n");
        sb.append("Subscribers: \n");
        for (String var : dsm.subscribers.keySet()) {
            sb.append(var).append("->").append(dsm.subscribers.get(var).toString()).append("\n");
        }
        System.out.println(sb.toString());
    }
}