package com.contec.phms.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import u.aly.bs;

public final class BitMapUtil {
    private static final LinkedList CACHE_ENTRIES = new LinkedList();
    private static int CACHE_SIZE = 1;
    private static final Map IMG_CACHE_INDEX = new HashMap();
    private static final byte[] LOCKED = new byte[0];
    private static final BitmapFactory.Options OPTIONS_DECODE = new BitmapFactory.Options();
    private static final BitmapFactory.Options OPTIONS_GET_SIZE = new BitmapFactory.Options();
    private static final Queue TASK_QUEUE = new LinkedList();
    private static final Set TASK_QUEUE_INDEX = new HashSet();
    private static final Size ZERO_SIZE = new Size(0, 0);

    static {
        OPTIONS_GET_SIZE.inJustDecodeBounds = true;
        new Thread() {
            {
                setDaemon(true);
            }

            public void run() {
                while (true) {
                    synchronized (BitMapUtil.TASK_QUEUE) {
                        if (BitMapUtil.TASK_QUEUE.isEmpty()) {
                            try {
                                BitMapUtil.TASK_QUEUE.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    QueueEntry entry = (QueueEntry) BitMapUtil.TASK_QUEUE.poll();
                    BitMapUtil.TASK_QUEUE_INDEX.remove(BitMapUtil.createKey(entry.path, entry.width, entry.height));
                    Bitmap unused = BitMapUtil.createBitmap(entry.path, entry.width, entry.height);
                }
            }
        }.start();
    }

    public static Bitmap getBitmap(String path, int width, int height) {
        if (path == null) {
            return null;
        }
        try {
            if (CACHE_ENTRIES.size() >= CACHE_SIZE) {
                destoryLast();
            }
            Bitmap bitMap = useBitmap(path, width, height);
            if (bitMap != null && !bitMap.isRecycled()) {
                return bitMap;
            }
            Bitmap bitMap2 = createBitmap(path, width, height);
            String key = createKey(path, width, height);
            synchronized (LOCKED) {
                IMG_CACHE_INDEX.put(key, bitMap2);
                CACHE_ENTRIES.addFirst(key);
            }
            return bitMap2;
        } catch (OutOfMemoryError e) {
            destoryLast();
            System.out.println(CACHE_SIZE);
            return createBitmap(path, width, height);
        }
    }

    public static Size getBitMapSize(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return ZERO_SIZE;
        }
        InputStream in = null;
        try {
            InputStream in2 = new FileInputStream(file);
            try {
                BitmapFactory.decodeStream(in2, (Rect) null, OPTIONS_GET_SIZE);
                Size size = new Size(OPTIONS_GET_SIZE.outWidth, OPTIONS_GET_SIZE.outHeight);
                closeInputStream(in2);
                return size;
            } catch (Throwable th2) {
                Throwable th = th2;
                in = in2;
                closeInputStream(in);
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } catch (FileNotFoundException e2) {
            Size size22 = ZERO_SIZE;
            closeInputStream(in);
            return size22;
        }
        return null;
    }

    private static Bitmap useBitmap(String path, int width, int height) {
        Bitmap bitMap;
        String key = createKey(path, width, height);
        synchronized (LOCKED) {
            bitMap = (Bitmap) IMG_CACHE_INDEX.get(key);
            if (bitMap != null && CACHE_ENTRIES.remove(key)) {
                CACHE_ENTRIES.addFirst(key);
            }
        }
        return bitMap;
    }

    private static void destoryLast() {
        Bitmap bitMap;
        synchronized (LOCKED) {
            String key = (String) CACHE_ENTRIES.removeLast();
            if (key.length() > 0 && (bitMap = (Bitmap) IMG_CACHE_INDEX.remove(key)) != null && !bitMap.isRecycled()) {
                bitMap.recycle();
            }
        }
    }

    public static void destoryall() {
        Bitmap bitMap;
        synchronized (LOCKED) {
            Iterator _it = CACHE_ENTRIES.iterator();
            while (_it.hasNext()) {
                String _key = (String) _it.next();
                CACHE_ENTRIES.remove(_key);
                if (_key.length() > 0 && (bitMap = (Bitmap) IMG_CACHE_INDEX.remove(_key)) != null && !bitMap.isRecycled()) {
                    bitMap.recycle();
                }
            }
        }
    }

    private static String createKey(String path, int width, int height) {
        if (path == null || path.length() == 0) {
            return bs.b;
        }
        return String.valueOf(path) + "_" + width + "_" + height;
    }

    private static Bitmap createBitmap(String path, int width, int height) {
        Bitmap decodeStream;
        File file = new File(path);
        if (file.exists()) {
            InputStream in = null;
            try {
                InputStream in2 = new FileInputStream(file);
                try {
                    Size size = getBitMapSize(path);
                    if (size.equals(ZERO_SIZE)) {
                        closeInputStream(in2);
                        return null;
                    }
                    int scale = Math.max(size.getWidth() / width, size.getHeight() / height);
                    synchronized (OPTIONS_DECODE) {
                        OPTIONS_DECODE.inSampleSize = scale;
                        decodeStream = BitmapFactory.decodeStream(in2, (Rect) null, OPTIONS_DECODE);
                    }
                    closeInputStream(in2);
                    return decodeStream;
                } catch (Throwable th) {
                    th = th;
                    in = in2;
                    closeInputStream(in);
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e2) {
                FileNotFoundException e = e2;
                try {
                    CLog.i("BitMapUtil", "createBitmap==" + e.toString());
                    closeInputStream(in);
                    return null;
                } catch (Throwable th2) {
                    Throwable th = th2;
                    closeInputStream(in);
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static void closeInputStream(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                CLog.v("BitMapUtil", "closeInputStream==" + e.toString());
            }
        }
    }

    static class Size {
        private int height;
        private int width;

        Size(int width2, int height2) {
            this.width = width2;
            this.height = height2;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }

    static class QueueEntry {
        public int height;
        public String path;
        public int width;

        QueueEntry() {
        }
    }
}
