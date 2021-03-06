package src.transport.util;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用布尔原子变量，信号量保证只释放一次
 * Created by Administrator on 2015/8/26.
 */
public class SemaphoreReleaseOnlyOnce {
    private final AtomicBoolean released = new AtomicBoolean(false);
    private final Semaphore semaphore;


    public SemaphoreReleaseOnlyOnce(Semaphore semaphore) {
        this.semaphore = semaphore;
    }


    public void release() {
        if (this.semaphore != null) {
            if (this.released.compareAndSet(false, true)) {
                this.semaphore.release();
            }
        }
    }


    public Semaphore getSemaphore() {
        return semaphore;
    }
}
