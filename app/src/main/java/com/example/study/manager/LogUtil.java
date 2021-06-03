package com.example.study.manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/3 下午1:42
 * @Company LotoGram
 */

public final class LogUtil {

    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;

    @IntDef({V, D, I, W, E, A})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
        int value() default V;
    }

    private static final int FILE = 0x0010;
    private static final int JSON = 0x0020;
    private static final int XML = 0x0030;

    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final Config CONFIG = new Config();

    private static SimpleDateFormat simpleDateFormat;
    private static ExecutorService EXECUTOR;

    public LogUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void v(final Object... msg) {
        log(V, CONFIG.getGlobalTag(), msg);
    }

    public static void v(final String tag, final Object... msg) {
        log(V, tag, msg);
    }

    public static void d(final Object... msg) {
        log(D, CONFIG.getGlobalTag(), msg);
    }

    public static void d(final String tag, final Object... msg) {
        log(D, tag, msg);
    }

    public static void i(final Object... msg) {
        log(I, CONFIG.getGlobalTag(), msg);
    }

    public static void i(final String tag, final Object... msg) {
        log(I, tag, msg);
    }

    public static void w(final Object... msg) {
        log(W, CONFIG.getGlobalTag(), msg);
    }

    public static void w(final String tag, final Object... msg) {
        log(W, tag, msg);
    }

    public static void e(final Object... msg) {
        log(E, CONFIG.getGlobalTag(), msg);
    }

    public static void e(final String tag, final Object... msg) {
        log(E, tag, msg);
    }

    public static void a(final Object... msg) {
        log(A, CONFIG.getGlobalTag(), msg);
    }

    public static void a(final String tag, final Object... msg) {
        log(A, tag, msg);
    }

    public static void file(final Object content) {
        log(FILE | D, CONFIG.getGlobalTag(), content);
    }

    public static void file(final String tag, final Object content) {
        log(FILE | D, tag, content);
    }

    public static void file(@TYPE final int type, final Object content) {
        log(FILE | type, CONFIG.getGlobalTag(), content);
    }

    public static void file(@TYPE final int type, final String tag, final Object content) {
        log(FILE | type, tag, content);
    }

    public static void json(final Object content) {
        log(JSON | D, CONFIG.getGlobalTag(), content);
    }

    public static void json(final String tag, final Object content) {
        log(JSON | D, tag, content);
    }

    public static void json(@TYPE final int type, final Object content) {
        log(JSON | type, CONFIG.getGlobalTag(), content);
    }

    public static void json(@TYPE final int type, final String tag, final Object content) {
        log(JSON | type, tag, content);
    }

    public static void xml(final Object content) {
        log(XML | D, CONFIG.getGlobalTag(), content);
    }

    public static void xml(final String tag, final Object content) {
        log(XML | D, tag, content);
    }

    public static void xml(@TYPE final int type, final Object content) {
        log(XML | type, CONFIG.getGlobalTag(), content);
    }

    public static void xml(@TYPE final int type, final String tag, final Object content) {
        log(XML | type, tag, content);
    }

    private static void log(final int type, final String tag, final Object... msg) {
        if (!CONFIG.isEnable()) return;
        final int typeHigh = type & 0xf0;
        final int typeLow = type & 0x0f;
        if (CONFIG.isLog2Console() || CONFIG.isLog2File() || typeHigh == FILE) {
            if (typeLow < CONFIG.getConsoleFilter() && typeLow < CONFIG.getFileFilter()) return;

            if (CONFIG.isLog2Console() && typeHigh != FILE && typeLow >= CONFIG.getConsoleFilter()) {
                for (Object str : msg) {
                    Log.d(tag, (String) str);
                }
            }
        }
    }

    private static void executor(final Runnable r) {
        if (EXECUTOR == null) {
            EXECUTOR = Executors.newSingleThreadExecutor();
        }
        EXECUTOR.execute(r);
    }

    @NotNull
    private static String getTime() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        }
        return simpleDateFormat.format(new Date());
    }

    private static String getDay() {
        String time = getTime();
        return time.split(" ")[0];
    }


    public static Config getConfig() {
        return CONFIG;
    }


    @NotNull
    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            Object object = clazz.getMethod("currentActivityThread").invoke(null);
            Object app = clazz.getMethod("getApplication").invoke(object);
            if (app == null) {
                throw new NullPointerException("you failed to get the application");
            }
            return (Application) app;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException("you failed to get the application");
        }
    }

    private static boolean isSpace(final String str) {
        if (TextUtils.isEmpty(str)) return true;
        for (int i = 0, len = str.length(); i < len; ++i) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static final class Config {
        private boolean enable = true;//LogUtil是否可用
        private boolean log2Console = true;//打印到控制台
        private boolean log2File = true;
        private boolean autoDelete = true;
        private boolean isSpaceTag = true;

        private String defaultDir;
        private String dir;
        private String filePrefix = String.valueOf(System.currentTimeMillis());
        private String fileExtension = ".txt";
        private String globalTag = null;

        @ENCRYPT
        private int encryptType = BASE64;
        private int validDays = 15;
        private int consoleFilter = V;
        private int fileFilter = V;

        @IntDef({BASE64, BINARY})
        @Retention(RetentionPolicy.SOURCE)
        public @interface ENCRYPT {
            int value() default BASE64;
        }

        public static final int BASE64 = 0x0001;
        public static final int BINARY = 0x0002;

        private Config() {
            if (!TextUtils.isEmpty(defaultDir)) return;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && getApplicationByReflect().getExternalCacheDir() != null) {
                defaultDir = getApplicationByReflect().getExternalCacheDir() + FILE_SEP + "log" + FILE_SEP;
            } else {
                defaultDir = getApplicationByReflect().getCacheDir() + FILE_SEP + "log" + FILE_SEP;
            }
        }

        public Config setEnable(final boolean enable) {
            this.enable = enable;
            return this;
        }

        public Config setLog2Console(final boolean log2Console) {
            this.log2Console = log2Console;
            return this;
        }

        public Config setLog2File(final boolean log2File) {
            this.log2File = log2File;
            return this;
        }

        public Config setFileExtension(final String fileExtension) {
            if (isSpace(fileExtension)) {
                this.fileExtension = ".txt";
            } else {
                if (fileExtension.endsWith(".")) {
                    this.fileExtension = fileExtension;
                } else {
                    this.fileExtension = "." + fileExtension;
                }
            }
            return this;
        }

        public Config setFilePrefix(final String filePrefix) {
            if (isSpace(filePrefix)) {
                this.filePrefix = "log";
            } else {
                this.filePrefix = filePrefix;
            }
            return this;
        }

        public Config setEncryptType(@ENCRYPT final int encryptType) {
            this.encryptType = encryptType;
            return this;
        }

        public Config setDir(final String dir) {
            if (isSpace(dir)) {
                this.dir = null;
            } else {
                this.dir = dir.endsWith(FILE_SEP) ? dir : dir + FILE_SEP;
            }
            return this;
        }

        public Config setDir(final File dir) {
            this.dir = dir == null ? null : (dir.getAbsolutePath() + FILE_SEP);
            return this;
        }


        public Config setValidDays(final int validDays) {
            this.validDays = validDays;
            return this;
        }

        public Config setAutoDelete(final boolean autoDelete) {
            this.autoDelete = autoDelete;
            return this;
        }


        public Config setGlobalTag(final String globalTag) {
            if (isSpace(globalTag)) {
                this.globalTag = "";
                this.isSpaceTag = true;
            } else {
                this.globalTag = globalTag;
                this.isSpaceTag = false;
            }
            return this;
        }

        public void setConsoleFilter(@TYPE int consoleFilter) {
            this.consoleFilter = consoleFilter;
        }

        public void setFileFilter(int fileFilter) {
            this.fileFilter = fileFilter;
        }

        public int getConsoleFilter() {
            return consoleFilter;
        }

        public boolean isSpaceTag() {
            return isSpaceTag;
        }

        public int getFileFilter() {
            return fileFilter;
        }

        public String getGlobalTag() {
            return globalTag;
        }

        public int getValidDays() {
            return validDays;
        }

        public boolean isAutoDelete() {
            return autoDelete;
        }

        public String getDir() {
            return dir;
        }

        public int getEncryptType() {
            return encryptType;
        }

        public boolean isEnable() {
            return enable;
        }

        public boolean isLog2Console() {
            return log2Console;
        }

        public boolean isLog2File() {
            return log2File;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public String getFilePrefix() {
            return filePrefix;
        }

        @NotNull
        @Override
        public String toString() {
            return "Config"
                    + LINE_SEP + "dir: " + getDir()
                    + LINE_SEP + "enable: " + isEnable()
                    + LINE_SEP + "log2Console: " + isLog2Console()
                    + LINE_SEP + "log2File: " + isEnable()
                    + LINE_SEP + "filePrefix: " + getFilePrefix()
                    + LINE_SEP + "fileExtension: " + getFileExtension()
                    + LINE_SEP + "encryptType: " + getEncryptType()
                    + LINE_SEP + "validDays: " + getValidDays()
                    + LINE_SEP + "globalTag: " + getGlobalTag()
                    + LINE_SEP + "autoDelete: " + isAutoDelete();
        }
    }
}