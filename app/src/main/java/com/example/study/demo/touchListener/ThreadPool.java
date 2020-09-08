package com.example.study.demo.touchListener;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.gson.Gson;

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
 * @author WYL
 * @date 2020/7/22
 * @organization Lotogram
 * @link https://www.cnblogs.com/zincredible/p/10984459.html
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
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param delay        The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final Task<T> task,
                                                final long initialDelay,
                                                final long delay,
                                                final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, initialDelay, delay, unit);
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type) {
        return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY);
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type, final int priority) {
        ExecutorService pool;
        Map<Integer, ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(type);
        if (priorityPools == null) {
            priorityPools = new ConcurrentHashMap<>();
            TYPE_PRIORITY_POOLS.put(type, priorityPools);
            pool = createPoolByTypeAndPriority(type, priority);
            priorityPools.put(priority, pool);
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
                        new LinkedBlockingQueue<Runnable>(128),
                        new UtilsThreadFactory("io", priority)
                );
            case TYPE_CPU:
                return new ThreadPoolExecutor(CPU_COUNT + 1,
                        2 * CPU_COUNT + 1,
                        30, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(128),
                        new UtilsThreadFactory("cpu", priority)
                );
            default:
                return Executors.newFixedThreadPool(
                        type,
                        new UtilsThreadFactory("fixed(" + type + ")", priority)
                );
        }
    }

    private static <T> void executeAtFixedRate(final ExecutorService pool,
                                               final Task<T> task,
                                               long initialDelay,
                                               final long period,
                                               final TimeUnit unit) {
        task.isSchedule = true;
        getScheduledByTask(task).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                pool.execute(task);
            }
        }, initialDelay, period, unit);
    }

    private static ScheduledExecutorService getScheduledByTask(final Task<?> task) {
        ScheduledExecutorService scheduled = TASK_SCHEDULED.get(task);
        if (scheduled == null) {
            UtilsThreadFactory factory = new UtilsThreadFactory("scheduled", Thread.MAX_PRIORITY);
            scheduled = Executors.newScheduledThreadPool(1, factory);
            TASK_SCHEDULED.put(task, scheduled);
        }
        return scheduled;
    }

    private static void removeScheduleByTask(final Task<?> task) {
        ScheduledExecutorService scheduled = TASK_SCHEDULED.get(task);
        if (scheduled != null) {
            TASK_SCHEDULED.remove(task);
            shutdownAndAwaitTermination(scheduled);
        }
    }

    private static void shutdownAndAwaitTermination(final ExecutorService pool) {
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

    private static final class UtilsThreadFactory extends AtomicLong implements ThreadFactory {
        private static final String TAG = "UtilsThreadFactory";
        private static final AtomicLong POOL_NUMBER = new AtomicLong(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final int priority;

        public UtilsThreadFactory(String prefix, int priority) {
            SecurityManager s = System.getSecurityManager();
            this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = prefix + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
            this.priority = priority;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + getAndIncrement(), 0) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Exception e) {
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
            Log.d(TAG, "onFail:" + new Gson().toJson(e));
        }

        @Override
        public void onSuccess(T result) {
            if (result != null) {
                Log.d(TAG, "onSuccess");
            }
        }
    }

    public abstract static class Task<T> implements Runnable, LifecycleObserver {
        private boolean isSchedule;
        private volatile int state;
        private static final int NEW = 0;
        private static final int COMPLETING = 1;
        private static final int CANCELLED = 2;
        private static final int EXCEPTIONAL = 3;

        public Task() {
            state = NEW;
        }

        public abstract T doInBackground() throws Exception;

        public abstract void onSuccess(T result);

        public abstract void onCancel();

        public abstract void onFail(Exception e);

        @Override
        public void run() {
            try {
                final T result = doInBackground();

                if (state != NEW) return;

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
            Log.d(TAG, getClass().getSimpleName() + " Task onDestroy");
            cancel();
        }
    }

    private static class Deliver {
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
