package com.example.study.manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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

    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int ASSERT = Log.ASSERT;

    @IntDef({VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
    }

    //特殊日志类型
    private static final int FILE = 0x10;
    private static final int JSON = 0x20;
    private static final int XML = 0x30;

    @IntDef({FILE, JSON, XML})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final Config CONFIG = new Config();

    private static SimpleDateFormat simpleDateFormat;
    private static ExecutorService EXECUTOR;

    public LogUtil() {
        //不可实例化
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void v(final Object... objects) {
        //VERBOSE:无TAG,可变长字符串打印
        log(VERBOSE, CONFIG.getGlobalTag(), objects);
    }

    public static void v(final String tag, final Object... objects) {
        //VERBOSE:有TAG,可变长字符串打印
        log(VERBOSE, tag, objects);
    }

    public static void d(final Object... objects) {
        //DEBUG:无TAG,可变长字符串打印
        log(DEBUG, CONFIG.getGlobalTag(), objects);
    }

    public static void d(final String tag, final Object... objects) {
        //DEBUG:有TAG,可变长字符串打印
        log(DEBUG, tag, objects);
    }

    public static void i(final Object... objects) {
        //INFO:无TAG,可变长字符串打印
        log(INFO, CONFIG.getGlobalTag(), objects);
    }

    public static void i(final String tag, final Object... objects) {
        //INFO:有TAG,可变长字符串打印
        log(INFO, tag, objects);
    }

    public static void w(final Object... objects) {
        //WARN:无TAG,可变长字符串打印
        log(WARN, CONFIG.getGlobalTag(), objects);
    }

    public static void w(final String tag, final Object... objects) {
        //WARN:有TAG,可变长字符串打印
        log(WARN, tag, objects);
    }

    public static void e(final Object... objects) {
        //ERROR:无TAG,可变长字符串打印
        log(ERROR, CONFIG.getGlobalTag(), objects);
    }

    public static void e(final String tag, final Object... objects) {
        //ERROR:有TAG,可变长字符串打印
        log(ERROR, tag, objects);
    }

    public static void a(final Object... objects) {
        //ASSERT:无TAG,可变长字符串打印
        log(ASSERT, CONFIG.getGlobalTag(), objects);
    }

    public static void a(final String tag, final Object... objects) {
        //ASSERT:有TAG,可变长字符串打印
        log(ASSERT, tag, objects);
    }

    //特殊数据类型日志打印(文件)
    public static void file(final Object content) {
        int typeAndLevel = FILE | DEBUG;
        log(typeAndLevel, CONFIG.getGlobalTag(), content);
    }

    public static void file(final String tag, final Object content) {
        int typeAndLevel = FILE | DEBUG;
        log(typeAndLevel, tag, content);
    }

    public static void file(@Level final int level, final Object content) {
        int typeAndLevel = FILE | level;
        log(typeAndLevel, CONFIG.getGlobalTag(), content);
    }

    public static void file(@Level final int level, final String tag, final Object content) {
        int typeAndLevel = FILE | level;
        log(typeAndLevel, tag, content);
    }

    //特殊数据类型日志打印(Json)
    public static void json(final Object content) {
        int typeAndLevel = JSON | DEBUG;
        log(typeAndLevel, CONFIG.getGlobalTag(), content);
    }

    public static void json(final String tag, final Object content) {
        int typeAndLevel = JSON | DEBUG;
        log(typeAndLevel, tag, content);
    }

    public static void json(@Level final int level, final Object content) {
        int typeAndLevel = JSON | level;
        log(typeAndLevel, CONFIG.getGlobalTag(), content);
    }

    public static void json(@Level final int level, final String tag, final Object content) {
        int typeAndLevel = JSON | level;
        log(typeAndLevel, tag, content);
    }

    //特殊数据类型日志打印(Xml)
    public static void xml(final Object content) {
        int typeAndLevel = XML | DEBUG;
        log(typeAndLevel, CONFIG.getGlobalTag(), content);
    }

    public static void xml(final String tag, final Object content) {
        int typeAndLevel = XML | DEBUG;
        log(typeAndLevel, tag, content);
    }

    public static void xml(@Level final int level, final Object content) {
        int typeAndLevel = XML | level;
        log(typeAndLevel, CONFIG.getGlobalTag(), content);
    }

    public static void xml(@Level final int level, final String tag, final Object content) {
        int typeAndLevel = XML | level;
        log(typeAndLevel, tag, content);
    }

    private static void log(final int typeAndLevel, final String tag, final Object... msg) {
        if (!CONFIG.isEnable()) return;
        final int type = typeAndLevel & 0xf0;
        final int level = typeAndLevel & 0x0f;

        if (CONFIG.isLog2Console() || CONFIG.isLog2File()) {
            String body = "aaa";
            /*
             * 控制台日志打印条件:
             * 1、开启控制台日志打印
             * 2、日志等级大于等于控制台过滤等级
             * 3、日志类型为非文件类型
             */
            if (CONFIG.isLog2Console() && level >= CONFIG.getConsoleFilter() && type != FILE) {
                print2Console(level, type, tag, msg);
            }

            /*
             * 文件日志打印条件:
             * 1、开启文件日志打印
             * 2、日志等级大于等于日志过滤等级
             * 3、日志类型为文件类型
             */
            if (CONFIG.isLog2File() && level >= CONFIG.getFileFilter() || type == FILE) {
                print2File();
            }
        }
    }

    private static void print2Console(
            final int level,
            final int type,
            final String tag,
            final @NotNull Object... text) {
        for (Object object : text) {
            if (object instanceof String) {
                Log.println(level, tag, (String) object);
            } else {
                Log.println(level, tag, object.getClass().getName());
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(object.getClass().getSimpleName(), object);
                    Log.println(level, tag, jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void print2File() {

    }

//    private static TagHead processHead(String tag) {
//        if (TextUtils.isEmpty(tag) && !CONFIG.isLogHeadEnable()) {
//
//        }
//    }

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

    public static final class TagHead {
        private String tag;
        private String[] consoleHead;
        private String fileHead;

        public TagHead(String tag, String[] consoleHead, String fileHead) {
            this.tag = tag;
            this.consoleHead = consoleHead;
            this.fileHead = fileHead;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String[] getConsoleHead() {
            return consoleHead;
        }

        public void setConsoleHead(String[] consoleHead) {
            this.consoleHead = consoleHead;
        }

        public String getFileHead() {
            return fileHead;
        }

        public void setFileHead(String fileHead) {
            this.fileHead = fileHead;
        }
    }

    public static final class Config {
        private boolean enable = true;//LogUtil是否可用
        private boolean log2Console = true;//打印到控制台
        private boolean log2File = true;
        private boolean logHeadEnable = true;
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
        private int consoleFilter = VERBOSE;
        private int fileFilter = VERBOSE;

        @IntDef({BASE64, BINARY})
        @Retention(RetentionPolicy.SOURCE)
        public @interface ENCRYPT {
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

        public Config setConsoleFilter(@Level int consoleFilter) {
            this.consoleFilter = consoleFilter;
            return this;
        }

        public Config setFileFilter(int fileFilter) {
            this.fileFilter = fileFilter;
            return this;
        }

        public Config setLogHeadEnable(boolean logHeadEnable) {
            this.logHeadEnable = logHeadEnable;
            return this;
        }

        public boolean isLogHeadEnable() {
            return logHeadEnable;
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