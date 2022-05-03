package com.contec.scanpickphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ImageLoader {
    private static ImageLoader mInstance;
    private Handler mHandler;
    private LruCache<String, Bitmap> mLruCache;
    private volatile Semaphore mPoolSemaphore;
    private Thread mPoolThread;
    private Handler mPoolThreadHander;
    private volatile Semaphore mSemaphore = new Semaphore(0);
    private LinkedList<Runnable> mTasks;
    private int mThreadCount = 1;
    private ExecutorService mThreadPool;
    private Type mType = Type.LIFO;

    public enum Type {
        FIFO,
        LIFO
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(1, Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    private ImageLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        this.mPoolThread = new Thread() {
            public void run() {
                Looper.prepare();
                ImageLoader.this.mPoolThreadHander = new Handler() {
                    public void handleMessage(Message msg) {
                        ImageLoader.this.mThreadPool.execute(ImageLoader.this.getTask());
                        try {
                            ImageLoader.this.mPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                ImageLoader.this.mSemaphore.release();
                Looper.loop();
            }
        };
        this.mPoolThread.start();
        this.mLruCache = new LruCache<String, Bitmap>(((int) Runtime.getRuntime().maxMemory()) / 8) {
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        this.mThreadPool = Executors.newFixedThreadPool(threadCount);
        this.mPoolSemaphore = new Semaphore(threadCount);
        this.mTasks = new LinkedList<>();
        if (type == null) {
            type = Type.LIFO;
        }
        this.mType = type;
    }

    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);
        if (this.mHandler == null) {
            this.mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    ImageView imageView = holder.imageView;
                    Bitmap bm = holder.bitmap;
                    if (imageView.getTag().toString().equals(holder.path)) {
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }
        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null) {
            ImgBeanHolder holder = new ImgBeanHolder(this, (ImgBeanHolder) null);
            holder.bitmap = bm;
            holder.imageView = imageView;
            holder.path = path;
            Message message = Message.obtain();
            message.obj = holder;
            this.mHandler.sendMessage(message);
            return;
        }
        addTask(new Runnable() {
            public void run() {
                ImageSize imageSize = ImageLoader.this.getImageViewWidth(imageView);
                ImageLoader.this.addBitmapToLruCache(path, ImageLoader.this.decodeSampledBitmapFromResource(path, imageSize.width, imageSize.height));
                ImgBeanHolder holder = new ImgBeanHolder(ImageLoader.this, (ImgBeanHolder) null);
                holder.bitmap = ImageLoader.this.getBitmapFromLruCache(path);
                holder.imageView = imageView;
                holder.path = path;
                Message message = Message.obtain();
                message.obj = holder;
                ImageLoader.this.mHandler.sendMessage(message);
                ImageLoader.this.mPoolSemaphore.release();
            }
        });
    }

    private synchronized void addTask(Runnable runnable) {
        try {
            if (this.mPoolThreadHander == null) {
                this.mSemaphore.acquire();
            }
        } catch (InterruptedException e) {
        }
        this.mTasks.add(runnable);
        this.mPoolThreadHander.sendEmptyMessage(272);
    }

    private synchronized Runnable getTask() {
        Runnable runnable;
        if (this.mType == Type.FIFO) {
            runnable = this.mTasks.removeFirst();
        } else if (this.mType == Type.LIFO) {
            runnable = this.mTasks.removeLast();
        } else {
            runnable = null;
        }
        return runnable;
    }

    public static ImageLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    private ImageSize getImageViewWidth(ImageView imageView) {
        int width;
        int height = 0;
        ImageSize imageSize = new ImageSize(this, (ImageSize) null);
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params.width == -2) {
            width = 0;
        } else {
            width = imageView.getWidth();
        }
        if (width <= 0) {
            width = params.width;
        }
        if (width <= 0) {
            width = getImageViewFieldValue(imageView, "mMaxWidth");
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }
        if (params.height != -2) {
            height = imageView.getHeight();
        }
        if (height <= 0) {
            height = params.height;
        }
        if (height <= 0) {
            height = getImageViewFieldValue(imageView, "mMaxHeight");
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    private Bitmap getBitmapFromLruCache(String key) {
        return this.mLruCache.get(key);
    }

    private void addBitmapToLruCache(String key, Bitmap bitmap) {
        if (getBitmapFromLruCache(key) == null && bitmap != null) {
            this.mLruCache.put(key, bitmap);
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        if (width <= reqWidth || height <= reqHeight) {
            return 1;
        }
        return Math.max(Math.round(((float) width) / ((float) reqWidth)), Math.round(((float) width) / ((float) reqWidth)));
    }

    private Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;

        private ImgBeanHolder() {
        }

        /* synthetic */ ImgBeanHolder(ImageLoader imageLoader, ImgBeanHolder imgBeanHolder) {
            this();
        }
    }

    private class ImageSize {
        int height;
        int width;

        private ImageSize() {
        }

        /* synthetic */ ImageSize(ImageLoader imageLoader, ImageSize imageSize) {
            this();
        }
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = ((Integer) field.get(object)).intValue();
            if (fieldValue <= 0 || fieldValue >= Integer.MAX_VALUE) {
                return 0;
            }
            int value = fieldValue;
            Log.e("TAG", new StringBuilder(String.valueOf(value)).toString());
            return value;
        } catch (Exception e) {
            return 0;
        }
    }
}
