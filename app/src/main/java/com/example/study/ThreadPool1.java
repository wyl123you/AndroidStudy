package com.example.study;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPool1 {
    private static final String TAG = ThreadPool1.class.getSimpleName();

    private static final Map<Integer, Map<Integer, ExecutorService>> TYPE_PRIORITY_POOLS = new ConcurrentHashMap<>();

    private static final Map<Task<?>, ScheduledExecutorService> TASK_SCHEDULED = new ConcurrentHashMap<>();

    private static final byte TYPE_SINGLE = -1;
    private static final byte TYPE_CACHED = -2;
    private static final byte TYPE_IO = -4;
    private static final byte TYPE_CPU = -8;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static <T> void executeBySingle(final Task<T> task) {
        execute(getPoolByType(TYPE_SINGLE), task);
    }

    public static <T> void executeBySingle(final Task<T> task, final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task);
    }

    /**
     * Executes the given task in an io thread pool after the given delay
     *
     * @param task  the task to execute
     * @param delay the time from now to delay execution
     * @param unit  the time unit of the delay parameter
     * @param <T>   the type of task's result
     */
    public static <T> void executeByIoWithDelay(final Task<T> task,
                                                final long delay,
                                                final TimeUnit unit) {
        executeWithDelay(getPoolByType(TYPE_IO), task, delay, unit);
    }

    /**
     * Executes the given task in an io thread pool after the given delay
     *
     * @param task     the task to execute
     * @param delay    the time from now to delay execution
     * @param unit     the time unit of the delay parameter
     * @param priority the priority of the thread in the pool
     * @param <T>      the type of task's result
     */
    public static <T> void executeByIoWithDelay(final Task<T> task,
                                                final long delay,
                                                final TimeUnit unit,
                                                @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO, priority), task, delay, unit);
    }


    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long delay,
                                                  final TimeUnit unit) {
        executeAtFixedRate(getPoolByType(TYPE_IO), task, delay, unit);
    }

    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long initialDelay,
                                                  final long delay,
                                                  final TimeUnit unit) {
        executeAtFixedRate(getPoolByType(TYPE_IO), task, initialDelay, delay, unit);
    }

    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long delay,
                                                  final TimeUnit unit,
                                                  @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO, priority), task, delay, unit);
    }

    public static <T> void executeByIoAtFixedRate(final Task<T> task,
                                                  final long initialDelay,
                                                  final long delay,
                                                  final TimeUnit unit,
                                                  @IntRange(from = 1, to = 10) final int priorit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO, priorit), task, initialDelay, delay, unit);
    }

    public static <T> void cancel(@NotNull final Task<T> task) {
        task.cancel();
    }

    private static <T> void execute(final ExecutorService pool, final Task<T> task) {
        executeWithDelay(pool, task, 0, TimeUnit.MILLISECONDS);
    }

    private static <T> void executeWithDelay(final ExecutorService pool,
                                             final Task<T> task,
                                             final long delay,
                                             final TimeUnit unit) {
        if (delay < 0) {
            getScheduleByTask(task).execute(() -> pool.execute(task));
        } else {
            getScheduleByTask(task).schedule(() -> pool.execute(task), delay, unit);
        }
    }

    private static <T> void executeAtFixedRate(final ExecutorService pool,
                                               @NotNull final Task<T> task,
                                               final long delay,
                                               final TimeUnit unit) {
        task.setScheduled(true);
        getScheduleByTask(task).scheduleAtFixedRate(() -> pool.execute(task), 0, delay, unit);
    }

    private static <T> void executeAtFixedRate(final ExecutorService pool,
                                               @NotNull final Task<T> task,
                                               final long initialDelay,
                                               final long delay,
                                               final TimeUnit unit) {
        task.setScheduled(true);
        getScheduleByTask(task).scheduleAtFixedRate(() -> pool.execute(task), initialDelay, delay, unit);
    }


    private static <T> ScheduledExecutorService getScheduleByTask(final Task<T> task) {
        ScheduledExecutorService pool = TASK_SCHEDULED.get(task);
        if (pool == null) {
            UtilTreadFactory factory = new UtilTreadFactory("schedule", Thread.MAX_PRIORITY);
            pool = Executors.newSingleThreadScheduledExecutor(factory);
            TASK_SCHEDULED.put(task, pool);
        }
        return pool;
    }

    private static <T> void removeScheduleByTask(final Task<T> task) {
        ScheduledExecutorService pool = TASK_SCHEDULED.get(task);
        if (pool != null) {
            TASK_SCHEDULED.remove(task);
            shutdownAndAwaitTermination(pool);
        }
    }

    private static ExecutorService getPoolByType(final int type) {
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
                        new UtilTreadFactory("single", priority)
                );
            case TYPE_CACHED:
                return Executors.newCachedThreadPool(
                        new UtilTreadFactory("cached", priority)
                );
            case TYPE_IO:
                return new ThreadPoolExecutor(
                        2 * CPU_COUNT + 1,
                        2 * CPU_COUNT + 1,
                        30, TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(128),
                        new UtilTreadFactory("io", priority)
                );
            case TYPE_CPU:
                return new ThreadPoolExecutor(
                        CPU_COUNT + 1,
                        2 * CPU_COUNT + 1,
                        30, TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(128),
                        new UtilTreadFactory("cpu", priority)
                );
            default:
                return Executors.newFixedThreadPool(
                        type,
                        new UtilTreadFactory("fixed(" + type + ")", priority)
                );
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
            pool.shutdownNow();
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public abstract static class SimpleTask<T> extends Task<T> {

        @Override
        public void onCancel() {
            Log.d(TAG, "Task Cancel");
        }

        @Override
        public void onFail(@NotNull Exception e) {
            Log.d(TAG, "Task Fail:" + e.getMessage());
        }

        @Override
        public void onSuccess(T result) {
            if (result != null) {
                Log.d(TAG, "Task Success");
            }
        }
    }

    public abstract static class Task<T> implements Runnable, LifecycleObserver {
        private boolean isScheduled;
        private volatile int state;
        private static final int NEW = 0;
        private static final int COMPLETING = 1;
        private static final int CANCELLED = 2;
        private static final int EXCEPTIONAL = 3;

        public Task() {
            this.state = NEW;
        }

        public void setScheduled(boolean scheduled) {
            this.isScheduled = scheduled;
        }

        public abstract T doInBackground() throws Exception;

        public abstract void onSuccess(T result);

        public abstract void onCancel();

        public abstract void onFail(Exception e);

        @CallSuper
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            Log.d(TAG, getClass().getSimpleName() + " Task onDestroy");
            cancel();
        }

        @Override
        public void run() {
            try {
                if (state != NEW) {
                    return;
                }

                final T result = doInBackground();

                if (isScheduled) {
                    Deliver.post(() -> onSuccess(result));
                } else {
                    state = COMPLETING;
                    Deliver.post(() -> {
                        onSuccess(result);
                        removeScheduleByTask(Task.this);
                    });
                }
            } catch (Exception e) {
                if (state != NEW) {
                    return;
                }
                state = EXCEPTIONAL;
                Deliver.post(() -> {
                    onFail(e);
                    removeScheduleByTask(Task.this);
                });
            }
        }

        public void cancel() {
            if (state != NEW) {
                return;
            }
            state = CANCELLED;
            Deliver.post(() -> {
                onCancel();
                removeScheduleByTask(Task.this);
            });
        }
    }

    private static final class UtilTreadFactory extends AtomicLong implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final int priority;

        private UtilTreadFactory(String prefix, int priority) {
            SecurityManager manager = System.getSecurityManager();
            this.group = manager != null ? manager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = prefix + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
            this.priority = priority;
        }

        @NotNull
        @Contract(pure = true)
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(group, runnable, namePrefix + getAndIncrement(), 0) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable throwable) {
                        Log.d(TAG, throwable.toString());
                    }
                }
            };
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            thread.setPriority(priority);
            return thread;
        }
    }


    private static class Deliver {
        private static final Handler MAIN_HANDLER;

        static {
            Looper looper;
            try {
                looper = Looper.getMainLooper();
            } catch (Exception e) {
                e.printStackTrace();
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
