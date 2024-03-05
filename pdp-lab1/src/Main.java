import java.io.*;
import java.util.*;

public class Main {
    private static final int noThreads = 10;
    private static final Inventory deposit = new Inventory();
    private static final List<Product> products = new ArrayList<>();
    private static final List<Bill> recordOfSales = new ArrayList<>();
    private static final List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        WriteInFile();
        ReadFromFile();

        float start =  (float) System.nanoTime() / 1000000;
        for (int i = 0; i < noThreads; i++) {
            Transaction t = new Transaction(deposit, "t" + i);
            int noProducts = new Random().nextInt(0,10);

            for (int j = 0; j < noProducts; j++) {
                int quantity = new Random().nextInt(0,10);
                int productId = new Random().nextInt(0, products.size());
                t.add(products.get(productId), quantity);
            }
            transactions.add(t);
        }

        List<Thread> threads = new ArrayList<>();

        for (var transaction: transactions) {
            threads.add(new Thread(transaction));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        verifyStock();

        float end = (float) System.nanoTime() / 1000000;
        System.out.println("\nEnd work: " + (end - start) / 1000 + " seconds");

    }

    static void verifyStock() {
        System.err.println("Verifying the stock...");
        double sumProductsPrice = 0;
        for (var record: recordOfSales) {
            Set<Product> products = record.getProducts();
            for (Product product: products) {
                sumProductsPrice += product.getPrice();
            }
        }

        double transactionsTotalSum = 0;
        for (var transaction : transactions) {
            transactionsTotalSum += transaction.getTotalPrice();
        }

        if (sumProductsPrice == transactionsTotalSum) {
            System.err.println("Stock verification failed!"); // TO DO:
        } else {
            System.err.println("Verification Successful!");
        }
    }

    private static void WriteInFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/dayana/Documents/uni3/PDP/pdp-lab1/products.txt"));
            for (int i = 0; i < 1000; i++) {
                Random r = new Random();
                int quantity = r.nextInt(0, 99);
                String s = new RandomString().generateRandomString() +  ' ' + r.nextDouble() + ' ' +  quantity + '\n';
                writer.write(s);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void ReadFromFile() {
        File file = new File("/Users/dayana/Documents/uni3/PDP/pdp-lab1/products.txt");
        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNext()){
                Product p = new Product(sc.next(), sc.nextFloat());
                products.add(p);
                deposit.add(p, sc.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

class RandomString {
    private static final String charsList =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int stringLength = 10;

    String generateRandomString() {
        StringBuilder randStr = new StringBuilder();
        for(int i = 0; i < stringLength; i++) {
            int number = getRandomNumber();
            char ch = charsList.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }
    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(charsList.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }
}