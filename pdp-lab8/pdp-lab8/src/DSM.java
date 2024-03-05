import domain.*;

import mpi.MPI;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// DSM allows multiple processes to share a common memory space, enabling them to read and update shared variables
public class DSM {
    // Keep track of the processes that subscribe to a variable
    public Map<String, Set<Integer>> subscribers;
    public int a, b, c;
    // used to ensure that only one thread can execute the critical sections of code at a time,
    // preventing race conditions when updating shared variables
    public static final Lock lock = new ReentrantLock();

    public DSM() {
        a = 0;
        b = 1;
        c = 2;
        subscribers = new ConcurrentHashMap<>();
        subscribers.put("a", new HashSet<>());
        subscribers.put("b", new HashSet<>());
        subscribers.put("c", new HashSet<>());
    }

    // Updates a variable and notifies subscribers about the change by sending an UpdateMessage
    public void updateVariable(String var, int value) {
        lock.lock();
        this.setVariable(var, value);
        Message message = new UpdateMessage(var, value);
        this.sendToSubscribers(var, message);
        lock.unlock();
    }

    // Sets the value of a variable based on the provided variable name
    public void setVariable(String var, int value) {
        if (var.equals("a"))
            this.a = value;
        if (var.equals("b"))
            this.b = value;
        if (var.equals("c"))
            this.c = value;
    }

    // Checks if a variable matches a specific value (old) and replaces it with a new value (newValue) if the condition is met.
    // It also sends an ErrorMessage to non-subscribers
    public void checkAndReplace(String var, int old, int newValue) {
        if (!this.subscribers.get(var).contains(MPI.COMM_WORLD.Rank())) {
            this.sendToSubscribers(var, new ErrorMessage(var, MPI.COMM_WORLD.Rank()));
        }
        if (var.equals("a") && this.a == old) {
            updateVariable("a", newValue);
        }
        if (var.equals("b")&& this.b == old) {
            updateVariable("b", newValue);
        }
        if (var.equals("c") && this.c == old) {
            updateVariable("c", newValue);
        }
    }

    // Allows a process to subscribe to updates for a specific variable.
    // It adds the process rank to the set of subscribers and notifies all processes about the subscription.
    public void subscribeTo(String var) {
        Set<Integer> subs = this.subscribers.get(var);
        subs.add(MPI.COMM_WORLD.Rank());
        this.subscribers.put(var, subs);
        this.sendAll(new SubscribeMessage(var, MPI.COMM_WORLD.Rank()));
    }

    // Synchronizes the subscription of a process for a specific variable.
    // It is called when a process receives a subscription message from another process.
    public void syncSubscription(String var, int rank) {
        Set<Integer> subs = this.subscribers.get(var);
        subs.add(rank);
        this.subscribers.put(var, subs);
    }

    // Sends a message to all subscribers of a particular variable, excluding the sender
    public void sendToSubscribers(String var, Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() == i || !subscribers.get(var).contains(i)) {
                continue;
            }
            MPI.COMM_WORLD.Send(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        }
    }

    // Sends a message to all processes, excluding the sender.
    // It is used for broadcasting subscription messages and closing messages
    private void sendAll(Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() == i && !(message instanceof CloseMessage)) {
                continue;
            }
            MPI.COMM_WORLD.Send(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        }
    }

    // Sends a CloseMessage to all processes, indicating that the DSM is closing
    public void close() {
        this.sendAll(new CloseMessage());
    }
}