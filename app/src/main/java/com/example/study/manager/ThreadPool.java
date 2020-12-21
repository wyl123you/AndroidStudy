package com.example.study.manager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2020/11/2 上午8:58
 * @Company LotoGram
 */
public class ThreadPool {

    private static final String TAG = "ThreadPool";

    private static final Map<Integer, Map<Integer, ExecutorService>> TYPE_PRIORITY_POOLS = new ConcurrentHashMap<>();

    private static final Map<Task<?>, ScheduledExecutorService> TASK_SCHEDULED = new ConcurrentHashMap<>();

    private static final byte TYPE_SINGLE = -1;
    private static final byte TYPE_CACHED = -2;
    private static final byte TYPE_IO = -4;
    private static final byte TYPE_CPU = -8;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * Return whether the thread is the main thread.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static ExecutorService getFixedPool(@IntRange(from = 1) final int size) {
        return getPoolByTypeAndPriority(size);
    }

    public static ExecutorService getFixedPool(@IntRange(from = 1) final int size,
                                               @IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(size, priority);
    }

    public static ExecutorService getSinglePool() {
        return getPoolByTypeAndPriority(TYPE_SINGLE);
    }

    public static ExecutorService getSinglePool(
            @IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_SINGLE, priority);
    }

    public static ExecutorService getCachePool() {
        return getPoolByTypeAndPriority(TYPE_CACHED);
    }

    public static ExecutorService getCachePool(
            @IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CACHED, priority);
    }

    public static ExecutorService getIoPool() {
        return getPoolByTypeAndPriority(TYPE_IO);
    }

    public static ExecutorService getIoPool(
            @IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_IO, priority);
    }

    public static ExecutorService getCpuPool() {
        return getPoolByTypeAndPriority(TYPE_CPU);
    }

    public static ExecutorService getCpuPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CPU, priority);
    }

    public static <T> void executeByFixed(@IntRange(from = 1) final int size,
                                          final Task<T> task) {
        execute(getPoolByTypeAndPriority(size), task);
    }

    public static <T> void executeByFixed(@IntRange(from = 1) final int size,
                                          final Task<T> task,
                                          @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(size, priority), task);
    }

    public static <T> void executeByFixedWithDelay(@IntRange(from = 1) final int size,
                                                   final Task<T> task,
                                                   final long delay,
                                                   final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(size), task, delay, unit);
    }

    public static <T> void executeByFixedWithDelay(@IntRange(from = 1) final int size,
                                                   final Task<T> task,
                                                   final long delay,
                                                   final TimeUnit unit,
                                                   @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(size, priority), task, delay, unit);
    }

    public static <T> void executeByFixedAtFixedRate(@IntRange(from = 1) final int size,
                                                     final Task<T> task,
                                                     final long period,
                                                     final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, 0, period, unit);
    }

    public static <T> void executeByFixedAtFixedRate(@IntRange(from = 1) final int size,
                                                     final Task<T> task,
                                                     final long period,
                                                     final TimeUnit unit,
                                                     @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, 0, period, unit);
    }

    public static <T> void executeByFixedAtFixedRate(@IntRange(from = 1) final int size,
                                                     final Task<T> task,
                                                     final long initialDelay,
                                                     final long period,
                                                     final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, initialDelay, period, unit);
    }

    public static <T> void executeByFixedAtFixedRate(@IntRange(from = 1) final int size,
                                                     final Task<T> task,
                                                     final long period,
                                                     final long initialDelay,
                                                     final TimeUnit unit,
                                                     @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeBySingle(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE), task);
    }

    public static <T> void executeBySingle(final Task<T> task,
                                           @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task);
    }

    public static <T> void executeBySingleWithDelay(final Task<T> task,
                                                    final long delay,
                                                    final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE), task, delay, unit);
    }

    public static <T> void executeBySingleWithDelay(final Task<T> task,
                                                    final long delay,
                                                    final TimeUnit unit,
                                                    @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, delay, unit);
    }

    public static <T> void executeBySingleAtFixedRate(final Task<T> task,
                                                      final long period,
                                                      final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, 0, period, unit);
    }

    public static <T> void executeBySingleAtFixedRate(final Task<T> task,
                                                      final long period,
                                                      final TimeUnit unit,
                                                      @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, 0, period, unit);
    }

    public static <T> void executeBySingleAtFixedRate(final Task<T> task,
                                                      final long initialDelay,
                                                      final long period,
                                                      final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, initialDelay, period, unit);
    }

    public static <T> void executeBySingleAtFixedRate(final Task<T> task,
                                                      final long period,
                                                      final long initialDelay,
                                                      final TimeUnit unit,
                                                      @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeByCache(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED), task);
    }

    public static <T> void executeByCache(final Task<T> task,
                                          @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED, priority), task);
    }

    public static <T> void executeByCacheWithDelay(final Task<T> task,
                                                   final long delay,
                                                   final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED), task, delay, unit);
    }

    public static <T> void executeByCacheWithDelay(final Task<T> task,
                                                   final long delay,
                                                   final TimeUnit unit,
                                                   @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, delay, unit);
    }

    public static <T> void executeByCacheAtFixedRate(final Task<T> task,
                                                     final long period,
                                                     final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, 0, period, unit);
    }

    public static <T> void executeByCacheAtFixedRate(final Task<T> task,
                                                     final long period,
                                                     final TimeUnit unit,
                                                     @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, 0, period, unit);
    }

    public static <T> void executeByCacheAtFixedRate(final Task<T> task,
                                                     final long initialDelay,
                                                     final long period,
                                                     final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, initialDelay, period, unit);
    }

    public static <T> void executeByCacheAtFixedRate(final Task<T> task,
                                                     final long period,
                                                     final long initialDelay,
                                                     final TimeUnit unit,
                                                     @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeByIo(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_IO), task);
    }

    public static <T> void executeByIo(final Task<T> task,
                                       @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_IO, priority), task);
    }

    public static <T> void executeByIoWithDelay(final Task<T> task,
                                                final long delay,
                                                final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO), task, delay, unit);
    }

    public static <T> void executeByIoWithDelay(final Task<T> task,
                                                final long delay,
                                                final TimeUnit unit,
                                                @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO, priority), task, delay, unit);
    }

    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long period,
                                                  final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, 0, period, unit);
    }

    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long period,
                                                  final TimeUnit unit,
                                                  @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO, priority), task, 0, period, unit);
    }

    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long initialDelay,
                                                  final long period,
                                                  final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, initialDelay, period, unit);
    }

    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long period,
                                                  final long initialDelay,
                                                  final TimeUnit unit,
                                                  @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeByCpu(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CPU), task);
    }

    public static <T> void executeByCpu(final Task<T> task,
                                        @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CPU, priority), task);
    }

    public static <T> void executeByCpuWithDelay(final Task<T> task,
                                                 final long delay,
                                                 final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU), task, delay, unit);
    }

    public static <T> void executeByCpuWithDelay(final Task<T> task,
                                                 final long delay,
                                                 final TimeUnit unit,
                                                 @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU, priority), task, delay, unit);
    }

    public static <T> void executeByCpuAtFixedRate(final Task<T> task,
                                                   final long period,
                                                   final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, 0, period, unit);
    }

    public static <T> void executeByCpuAtFixedRate(final Task<T> task,
                                                   final long period,
                                                   final TimeUnit unit,
                                                   @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU, priority), task, 0, period, unit);
    }

    public static <T> void executeByCpuAtFixedRate(final Task<T> task,
                                                   final long initialDelay,
                                                   final long period,
                                                   final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, initialDelay, period, unit);
    }

    public static <T> void executeByCpuAtFixedRate(final Task<T> task,
                                                   final long period,
                                                   final long initialDelay,
                                                   final TimeUnit unit,
                                                   @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU, priority), task, initialDelay, period, unit);
    }


    public static <T> void executeByCustom(final ExecutorService pool,
                                           final Task<T> task) {
        execute(pool, task);
    }

    public static <T> void executeByCustomWithDelay(final ExecutorService pool,
                                                    final Task<T> task,
                                                    final long delay,
                                                    final TimeUnit unit) {
        executeWithDelay(pool, task, delay, unit);
    }

    public static <T> void executeByCustomAtFixedRate(final ExecutorService pool,
                                                      final Task<T> task,
                                                      final long period,
                                                      final TimeUnit unit) {
        executeAtFixedRate(pool, task, 0, period, unit);
    }

    public static <T> void executeByCustomAtFixedRate(final ExecutorService pool,
                                                      final Task<T> task,
                                                      final long initialDelay,
                                                      final long period,
                                                      final TimeUnit unit) {
        executeAtFixedRate(pool, task, initialDelay, period, unit);
    }


    private static <T> void execute(final ExecutorService pool,
                                    final Task<T> task) {
        executeWithDelay(pool, task, 0, TimeUnit.MILLISECONDS);
    }

    private static <T> void executeWithDelay(final ExecutorService pool,
                                             final Task<T> task,
                                             final long delay,
                                             final TimeUnit unit) {
        //execute:立即执行
        //schedule:延时任务
        //scheduleAtFixedRate:循环任务，按照上一次任务的发起时间计算下一次任务的开始时间
        //scheduleWithFixedDelay:循环任务，以上一次任务的结束时间计算下一次任务的开始时间
        if (delay <= 0) {
            getScheduledByTask(task).execute(() -> pool.execute(task));
        } else {
            getScheduledByTask(task).schedule(() -> pool.execute(task), delay, unit);
        }
    }

    private static <T> void executeAtFixedRate(final ExecutorService pool,
                                               @NotNull final Task<T> task,
                                               final long initialDelay,
                                               final long period,
                                               final TimeUnit unit) {
        task.isSchedule = true;
        getScheduledByTask(task).scheduleAtFixedRate(() -> pool.execute(task), initialDelay, period, unit);
    }

    private static ScheduledExecutorService getScheduledByTask(final Task<?> task) {
        ScheduledExecutorService pool = TASK_SCHEDULED.get(task);
        if (pool == null) {
            UtilsThreadFactory factory = new UtilsThreadFactory("schedule", Thread.MAX_PRIORITY);
            pool = Executors.newScheduledThreadPool(1, factory);
            TASK_SCHEDULED.put(task, pool);
        }
        return pool;
    }

    private static void removeScheduleByTask(final Task<?> task) {
        ScheduledExecutorService pool = TASK_SCHEDULED.get(task);
        if (pool != null) {
            TASK_SCHEDULED.remove(task);
            shutdownAndAwaitTermination(pool);
        }
    }

    private static void shutdownAndAwaitTermination(@NotNull final ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type) {
        return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY);
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type, final int priority) {
        ExecutorService pool;

        Map<Integer, ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(type);

        if (priorityPools == null) {
            priorityPools = new ConcurrentHashMap<>();
            pool = createPoolByTypeAndPriority(type, priority);
            priorityPools.put(priority, pool);
            TYPE_PRIORITY_POOLS.put(type, priorityPools);
        } else {
            pool = priorityPools.get(priority);
            if (pool == null) {
                pool = createPoolByTypeAndPriority(type, priority);
                priorityPools.put(priority, pool);
            }
        }
        return pool;
    }

    private static ExecutorService createPoolByTypeAndPriority(final int type, final int priority) {
        switch (type) {
            case TYPE_SINGLE:
                return Executors.newSingleThreadExecutor(
                        new UtilsThreadFactory("single", priority)
                );
            case TYPE_CACHED:
                return Executors.newCachedThreadPool(
                        new UtilsThreadFactory("cached", priority)
                );
            case TYPE_IO:
                return new ThreadPoolExecutor(2 * CPU_COUNT + 1,
                        2 * CPU_COUNT + 1,
                        30, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(128),
                        new UtilsThreadFactory("io", priority));
            case TYPE_CPU:
                return new ThreadPoolExecutor(CPU_COUNT + 1,
                        2 * CPU_COUNT + 1,
                        30, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(128),
                        new UtilsThreadFactory("cpu", priority));
            default:
                return Executors.newFixedThreadPool(
                        type,
                        new UtilsThreadFactory("fixed(" + type + ")", priority)
                );
        }
    }

    private static final class UtilsThreadFactory extends AtomicLong implements ThreadFactory {

        private final String TAG = this.getClass().getSimpleName();
        private static final AtomicLong poolNumber = new AtomicLong(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final int priority;

        public UtilsThreadFactory(String prefix, int priority) {
            SecurityManager s = System.getSecurityManager();
            this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = prefix + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
            this.priority = priority;
        }

        @NotNull
        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = new Thread(group, runnable, namePrefix + getAndIncrement(), 0) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                    }
                }
            };
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(priority);
            return t;
        }
    }

    public abstract static class SimpleTask<T> extends Task<T> {

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }

        @Override
        public void onFail(Exception e) {
            Log.d(TAG, "onFail");
        }

        @Override
        public void onSuccess(T result) {
            if (result != null) {
                Log.d(TAG, "onSuccess");
            }
        }
    }

    private abstract static class Task<T> implements Runnable, LifecycleObserver {

        protected final String TAG = this.getClass().getSimpleName();
        private boolean isSchedule;
        private volatile int state;
        private static final int NEW = 0;
        private static final int COMPLETING = 1;
        private static final int CANCELLED = 2;
        private static final int EXCEPTIONAL = 3;

        private Task() {
            this.state = NEW;
        }

        public abstract T doingBackground() throws Exception;

        public abstract void onSuccess(T result);

        public abstract void onCancel();

        public abstract void onFail(Exception e);

        @Override
        public void run() {
            try {
                if (state != NEW) return;
                final T result = doingBackground();

                if (isSchedule) {
                    Deliver.post(() -> onSuccess(result));
                } else {
                    state = COMPLETING;
                    Deliver.post(() -> {
                        onSuccess(result);
                        removeScheduleByTask(Task.this);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (state != NEW) return;

                state = EXCEPTIONAL;
                Deliver.post(() -> {
                    onFail(e);
                    removeScheduleByTask(Task.this);
                });
            }
        }

        public void cancel() {
            if (state != NEW) return;
            state = CANCELLED;
            Deliver.post(() -> {
                onCancel();
                removeScheduleByTask(Task.this);
            });
        }

        @CallSuper
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            Log.d(TAG, "Task onDestroy");
            cancel();
        }
    }

    private static final class Deliver {

        private static final Handler MAIN_HANDLER;

        static {
            Looper looper;
            try {
                looper = Looper.getMainLooper();
            } catch (Exception e) {
                looper = null;
            }
            if (looper != null) {
                MAIN_HANDLER = new Handler(looper);
            } else {
                MAIN_HANDLER = null;
            }
        }

        private static void post(final Runnable runnable) {
            if (MAIN_HANDLER != null) {
                MAIN_HANDLER.post(runnable);
            } else {
                runnable.run();
            }
        }
    }
}