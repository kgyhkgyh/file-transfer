package executor;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/7/5.
 */
public class ExecutorTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService root = Executors.newFixedThreadPool(5);
        Future<String> future = root.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "this is my task ";
            }
        });
        System.out.println(future.get());
    }

}
