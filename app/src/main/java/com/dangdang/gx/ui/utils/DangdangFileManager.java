package com.dangdang.gx.ui.utils;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import androidx.collection.LongSparseArray;
import androidx.core.content.FileProvider;
import com.dangdang.gx.ui.log.LogM;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

//import com.dangdang.reader.ApplicationAdapterWrapper;

public class DangdangFileManager {
    public static final String TAG = "DangdangFileManager";


    public static final String PDF_RESOURCES_PATH = "plugin";
    public static final String OLD_ROOT_NAME = "dangdang";

    /**
     * application里赋值
     */
    public static String APP_ROOT_PATH;
    public static String APP_START_IMG_PATH;

    /**
     * 文件存储目录
     */
    public static String APP_DIR = "ddReader";

    public static final String OLD_ROOT_PATH = File.separator + OLD_ROOT_NAME
            + File.separator;

    public static final String ROOT_PATH = File.separator + APP_DIR
            + File.separator;

    public static final String USER_UNDEFINE = Constants.USER_UNDEFINE;

    public static final String USER_UNDEFINE_DIR = File.separator
            + USER_UNDEFINE + File.separator;
    public static final String USER_READ_BOOK = "readbook";
    public static final String USER_READ_BOOK_DIR = USER_READ_BOOK
            + File.separator;
    public static final String USER_BOOK_DIR = "book" + File.separator;
    public static final String BOOK_ERROR = "book_error";
    public static final String ERROR_LOG_PATH = File.separator + "errlog"
            + File.separator;

    public final static String FONT_DIR = USER_BOOK_DIR + "font"
            + File.separator;
    public final static String FONT_EXTEN = ".zip";
    public final static String PRE_SET_DIR = USER_UNDEFINE + File.separator
            + "preread" + File.separator;

    public final static String PLUGINAPK_NAME = "DDLightReadPlugin.apk";

    public final static String BOOK_CACHE = "bookcache" + File.separator;
    public final static String BOOK_STORE_CACHE = "storecache" + File.separator;// 书城cache
    public final static String BOOK_SHELF_RECOMMAND_CACHE = "shelf_recommand" + File.separator;//  书架cache
    public final static String READCOMPOSING_CACHE = "readm" + File.separator;

    public static final String DANGDANG_WIFI = "dangdangwifi";
    public static final String DANGDANG_WIFI_PATH = "/" + DANGDANG_WIFI;

    public static final String BD_TTS_DIR = "tts";

    public static int IMAGE_DECODER_SIZE = 200;

    public static int IMAGE_DEFAULT_WIDTH = 320;
    public static int IMAGE_DEFAULT_HEIGHT = 480;

    private static DangdangFileManager mFileManager = null;
    private static Bitmap sDefaultCover = null;
    //private static Bitmap mClipDefaultCover = null;

    private final static String PreSet_Ttf = "default_blue_font.ttf";
    private final static String PreSet_Ttfzip = "default_blue_font.zip";

    public final static int DEFAULT_FONTSIZE = 16;

    /**
     * 预置英文字体
     */
//	private final static String[] PreSet_EnTtf = { "DroidSerif-Regular.ttf" };// "opensans-light.ttf",
    private final static String[] PreSet_EnTtf = {"times.ttf", "DroidSerif-Regular.ttf"};// "opensans-light.ttf",
    private final static String PreSet_EnMonoTtf = "DroidSansMono.ttf";
    private final static String PreSet_KoreaTtf = "NotoSansKR-Regular.otf";

    // 不好看
    // private final static String PreSet_Css = "style.css";

    private final static String PreSet_DictZip = "dicts.zip";
    private final static String PreSet_DictDir = "dicts";
    private final static String PreSet_DictXdb = "dict-gbk.xdb";
    private final static String PreSet_DictRule = "rules.ini";

    private final static String PreSet_ReadZip = "readfile.zip";
    private final static String PreSet_ReadDir = "readfile";

    private final static String ReadDict = "readdict";

    private final static String Ext_SDcardPath = "SECONDARY_STORAGE";
    private final static String Sdcard = "sdcard";
    private final static String BookStore_Dir = "bookstore";
    private final static String BookStore_Zip = "bookstore.zip";
    private final static String BOOKNAME_SIGN = "(电子书)";

    private final static String PreSet_ReadEndPageImg = "dd-f.jpg";

    private static Context mContext;

    private DangdangFileManager() {
    }

    private static Context getContext() {
//        return ApplicationAdapterWrapper.getInstance().getApplication().getApplicationContext();
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public synchronized static DangdangFileManager getFileManagerInstance() {
        if (null == mFileManager) {
            mFileManager = new DangdangFileManager();
        }
        return mFileManager;
    }

    public static void initSdkMode(Context context) {
        DangdangFileManager.getFileManagerInstance().setContext(context);
        DangdangFileManager.APP_ROOT_PATH = "/data/data/" + context.getPackageName();
        APP_DIR = "ddReaderSdk";
    }

    public static boolean checkMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static String getWifiFilePath() {
        if (checkMounted()) {
            if (!hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                return getExternalFilesDir() + DANGDANG_WIFI;
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + DANGDANG_WIFI_PATH;
        }
        return "";
    }

    private static String getAppApkDir() {
        String path = DangdangFileManager.APP_ROOT_PATH + "/apk/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private static String getSdcardApkDir() {
        String path = "";
        if (DangdangFileManager.checkMounted()) {
            path = DangdangFileManager.getRootPathOnSdcard() + "apk/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return path;
    }

    private static File getApkFile(String path, String filename) {
        File file = new File(path, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String getRootPath(Context appContext) {
        boolean currentDataInSdcard = true;// sAccountManager.getConfig().isCurrentDataInSdcard();
        String rootPath = null;
        if (currentDataInSdcard && checkMounted()) {
            rootPath = getRootPathOnSdcard();
        } else {
            rootPath = getRootPathOnPhone(appContext);
        }
        // LogM.d(TAG, "rootPath:" + currentDataInSdcard + ", " + rootPath);
        return rootPath;

    }

    private static String getRootPathOnPhone(Context appContext) {
        File phoneFiles = appContext.getFilesDir();
        String rootPath = phoneFiles.getAbsolutePath() + OLD_ROOT_PATH;
        return rootPath;
    }

    public static final int STORAGE_MIN_SIZE = 15 * 1024 * 1024; // 存储空间必须大于15M
    public static final String PDF_TEMP_RESOURCES = "pdf_resources.zip";

    public static String getPdfResourceRootPath(Context appContext) {
        if (MemoryStatus.getAvailableInternalMemorySize() >= STORAGE_MIN_SIZE) {
            return appContext.getFilesDir().getAbsolutePath() + File.separator
                    + PDF_RESOURCES_PATH + File.separator;
        }
        if (MemoryStatus.getAvailableExternalMemorySize() >= STORAGE_MIN_SIZE) {
            return getRootPathOnSdcard() + PDF_RESOURCES_PATH + File.separator;
        }
        return "";
    }

    public static String getRootPathOnSdcard() {
        if (!hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            return getExternalFilesDir() + APP_DIR + File.separator;
        File sdcard = Environment.getExternalStorageDirectory();
        String rootPath = sdcard.getAbsolutePath() + ROOT_PATH;
        return rootPath;
    }

    public static String getExternalDDRootPath() {
        if (!hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            return getExternalFilesDir();
        return Environment.getExternalStorageDirectory() + File.separator;
    }

    public static String getSdcardPath() {
        File sdDir = null;
        boolean sdCardExist = checkMounted(); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.getPath();
        }
        return "/";
    }

    private static String getSystemCachePath() {
        return getContext().getCacheDir().getAbsolutePath();
    }

    public static String getBookCachePath() {
        String path = APP_ROOT_PATH + File.separator + BOOK_CACHE;
        if (checkMounted()) {
            path = getRootPathOnSdcard() + BOOK_CACHE;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getBookNameFromPath(String path) {

        String str = path.substring(path.lastIndexOf("/") + 1);
        int loc = str.lastIndexOf(".");
        if (loc != -1) {
            str = str.substring(0, str.lastIndexOf("."));
        }
        return str;
    }

    private static void recurrenceDeleteFile(File rootFile) {
        if (rootFile == null) {
            return;
        }
        if (rootFile.isFile()) {
            rootFile.delete();
            return;
        }
        File[] listFile = rootFile.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; ++i) {
                recurrenceDeleteFile(listFile[i]);
            }
        }
        rootFile.delete();
    }

    public static void deleteBook(File product) {
        try {
            if (product == null) {
                return;
            }
            if (!product.exists()) {
                return;
            }
            recurrenceDeleteFile(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static final String BOOK_ENCODING = "utf-8";
    public static final String BOOK_SUFFIX = ".epub";
    public static final String BOOK_SUFFIX_CACHE = ".epub.cache";
    public static final String BOOK_FINISH = "book_finish";
    public static final String BOOK_SIZE = "book_size";
    public static final String BOOK_JSON = "book_json";
    public static final String BOOK_KEY = "book_key";
    public static final String BOOK_DECODE_KEY = "book_decode_key";
    public static final char REPLACE = '#';
    public static final char DELI = '|';
    public static final long BOOK_DOWN_TIMEOUT = 60 * 60 * 1000; // 1 小时超时时间
    public static final String ITEM_BOOK_COVER = "cover.jpg";
    public static final String ITEM_BOOK_READ_PROGRESS = "book_progress";

    /**
     * @param productDir
     * @return
     * @deprecated 可能判断错误 /.dangdang/readbook/read/....
     */
    public static boolean isReadBook(String productDir) {
        if (productDir == null) {
            return true;
        }
        int index = productDir.lastIndexOf(USER_READ_BOOK);
        if (index > 0) {
            File file = new File(productDir.substring(0,
                    index + USER_READ_BOOK.length()));
            return file.isDirectory();
        }
        return false;
    }

    public static final String getFileSize(File file) {
        String fileSize = "0.00K";
        if (file.exists()) {
            fileSize = FormetFileSize(file.length());
            return fileSize;
        }
        return fileSize;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = "";
        if (fileS < 0) {
            fileSizeString = "0.00B";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static boolean writeStringToFile(String text, File file) {
        try {
            return writeDataToFile(text.getBytes(BOOK_ENCODING), file);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public static boolean writeDataToFile(byte[] datas, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(datas);
            fos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
        return false;
    }

    public static final int MAX_LENGTH = 10 * 1024; // 10k

    public static boolean writeStringToFile(InputStream is, File file) {
        FileOutputStream fos = null;
        DataInputStream dis = null;
        try {
            File f = file.getParentFile();
            if (f.exists() && f.isDirectory())
                ;
            else
                f.mkdirs();
            fos = new FileOutputStream(file);
            dis = new DataInputStream(is);
            byte[] data = new byte[MAX_LENGTH];
            int len = dis.read(data);
            while (len > 0) {
                fos.write(data, 0, len);
                /* fos.write(data); */
                fos.flush();
                len = dis.read(data);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(dis);
            close(fos);
        }
        return false;
    }


    static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 删除单个文件夹下文件
     *
     * @param file
     * @return
     */
    public static boolean deleteCurrFile(File file) {
        try {
            boolean delete = false;
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        File oldFile = files[i];
                        File newFile = new File(oldFile.getAbsolutePath() + "temp");
                        if (oldFile.renameTo(newFile)) {
                            newFile.delete();
                        } else {
                            oldFile.delete();
                        }
                        //					files[i].delete();
                    }
                }
                file.delete();
                File oldFile = file;
                File newFile = new File(oldFile.getAbsolutePath() + "temp");
                if (oldFile.renameTo(newFile)) {
                    newFile.delete();
                } else {
                    oldFile.delete();
                }
                delete = true;
            }
            return delete;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void copyFile(File sourceFile, File desFile,
                                boolean isDeleteSourceFile) throws IOException {
        if (sourceFile.isFile() && sourceFile.exists()) {
            byte[] buffer = new byte[1024];
            InputStream fis = new FileInputStream(sourceFile);
            OutputStream fos = new FileOutputStream(desFile);
            int b = fis.read(buffer);
            while (b != -1) {
                fos.write(buffer, 0, b);
                b = fis.read(buffer);
            }
            fis.close();
            fos.flush();
            fos.close();
            if (isDeleteSourceFile) {
                sourceFile.delete();
            }
        }
    }

    public static String getExternalRootPath(Context context) {
        if (!hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return getExternalFilesDir() + APP_DIR + File.separator;
        }
        LongSparseArray<String> array = Utils.getExterPath();
        int len = array.size();
        if (len <= 0)
            return getRootPath(context);
        return array.valueAt(len - 1) + ROOT_PATH;
    }

    /**
     * ------------------------------------------------ 获取该书全本存储目录
     *
     * @param context
     * @param productId
     * @param userName
     * @return
     */
    public static String getFullProductDir(Context context, String productId,
                                           String userName) {
        try {
            String rootPath = getExternalRootPath(context);
            File rootFile = new File(rootPath);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File bookFile = new File(rootFile.getAbsolutePath()
                    + File.separator + userName + File.separator
                    + USER_BOOK_DIR + productId);
            if (!bookFile.exists()) {
                bookFile.mkdirs();
            }
            return bookFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取该书试读存储目录
     *
     * @param context
     * @param productId
     * @return
     */
    public static String getTryProductDir(Context context, String productId) {
        try {
            String rootPath = getExternalRootPath(context);
            File rootFile = new File(rootPath);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }

            File trybookFile = new File(rootFile.getAbsolutePath()
                    + USER_UNDEFINE_DIR + USER_READ_BOOK_DIR + productId);
            if (!trybookFile.exists()) {
                trybookFile.mkdirs();
            }
            return trybookFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取书的Key保存路径
     *
     * @param productDir
     * @return
     */
    public static File getBookKey(String productDir) {
        File file = new File(productDir + File.separator + BOOK_KEY);
        return file;
    }

    ///**
    // * 获取下载书所要保存的文件
    // *
    // * @param productDir
    // * @param productId
    // * @return
    // */
    //public static File getBookDest(String productDir, String productId,
    //                               BookType type) {
    //    File destBook;
    //    if (type == BookType.BOOK_TYPE_NOT_NOVEL) {
    //        destBook = new File(productDir + File.separator + productId
    //                + BOOK_SUFFIX);
    //    } else if (type == ShelfBook.BookType.BOOK_TYPE_IS_FULL_YES || type == BookType.BOOK_TYPE_IS_COMICS_FULL_YES) {
    //        destBook = new File(getPartBookDir(productId) + productId + ".zip");
    //    } else {
    //        destBook = new File(getPartBookDir(productId) + productId);
    //    }
    //
    //    if (!destBook.exists()) {
    //        try {
    //            destBook.createNewFile();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    return destBook;
    //}

    /**
     * 获取下载书所要保存的文件
     *
     * @param productDir
     * @param productId
     * @return
     */
    //public static File getBookDest(String productDir, String productId, String chapterId,
    //                               ShelfBook.BookType type) {
    //    File destBook;
    //    if (type == ShelfBook.BookType.BOOK_TYPE_NOT_NOVEL) {
    //        destBook = new File(productDir + File.separator + productId
    //                + BOOK_SUFFIX);
    //    } else if (type == ShelfBook.BookType.BOOK_TYPE_IS_LISTEN) {
    //        destBook = new File((getListenBookDir(productId) + chapterId));
    //    } else if (type == ShelfBook.BookType.BOOK_TYPE_IS_FULL_YES || type == ShelfBook.BookType.BOOK_TYPE_IS_COMICS_FULL_YES) {
    //        destBook = new File(getPartBookDir(productId) + productId + ".zip");
    //    } else {
    //        destBook = new File(getPartBookDir(productId) + productId);
    //    }
    //
    //    if (!destBook.exists()) {
    //        try {
    //            destBook.createNewFile();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    return destBook;
    //}

    public static boolean saveBookJson(String productDir, String bookJson) {
        // 测试
        File jsonFile = new File(productDir + File.separator + BOOK_JSON);
        if (!jsonFile.exists() && bookJson != null) {
            // JSONObject jsonObj = downTag.mJsonObject;
            // jsonObj.toString();
            return writeStringToFile(bookJson, jsonFile);
        }
        return true;
    }

    public static boolean checkBookJson(String productDir) {
        File jsonFile = new File(productDir + File.separator + BOOK_JSON);
        return jsonFile.exists();
    }

    public static long getDownLoadStart(String productDir, String productId) {
        File bookFile = new File(productDir + File.separator + productId
                + BOOK_SUFFIX);
        if (bookFile.exists()) {
            return bookFile.length();
        } else {
            try {
                bookFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    public static void writeDownLoadSize(String productDir, int fileSize) {
        File downSize = new File(productDir + File.separator + BOOK_SIZE);
        if (downSize.exists()) {
            downSize.delete();
        }
        writeStringToFile(String.valueOf(fileSize), downSize);
    }

    public static void writeDownloadFinishFile(String productDir) {
        File downSize = new File(productDir + File.separator + BOOK_FINISH);
        if (!downSize.exists()) {
            try {
                downSize.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean hasDownloadFinish(String productDir) {
        File file = new File(productDir + File.separator + BOOK_FINISH);
        return file.exists();
    }

    public static File getFontDownloadSaveFile(String productDir,
                                               String indentityId) {
        String filename = indentityId + FONT_EXTEN;
        File fontFile = new File(productDir, filename);
        try {
            if (!fontFile.exists()) {
                fontFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontFile;
    }

    public static String getCoverPath(String productid, String bookdir) {
        /*
         * if (productid.contains(InbuildBooks.PUBLIC_KEY_PREFIX)) { return
		 * bookdir + File.separator + DangdangFileManager.ITEM_BOOK_COVER; //
		 * return getSubString(mBookDir, File.separator, false) + //
		 * File.separator + DangdangFileManager.ITEM_BOOK_COVER; } else {
		 * ShelfService service = ShelfService.getInstance(mContext);
		 * ShelfBookInfo info = service.getShelfBookByBookId(productid); if
		 * (info == null) return ""; return info.getmCoverUrl();//
		 * ResourceManager.getManager().getClipImagPath(info.getmCoverUrl()); }
		 */
        return null;
    }

    public static Bitmap getBitmap(String path, int minSideLength,
                                   int maxNumOfPixels) {
        Bitmap bm = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        opt.inSampleSize = 1;
        bm = BitmapFactory.decodeFile(path, opt);
        // pad拉伸 太难看了
        opt.inSampleSize = computeSampleSize(opt, minSideLength, maxNumOfPixels);
        opt.inJustDecodeBounds = false;
        opt.inDither = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            bm = BitmapFactory.decodeFile(path, opt);
        } catch (OutOfMemoryError err) {
            LogM.e(TAG, "bitmap decoder failed");
            bm = null;
        }
        return bm;
    }

    public static boolean isFileExist(String dir) {
        File file = new File(dir);
        return file.exists();
    }

    public static boolean isFileHasChild(String dir) {
        File file = new File(dir);
        if (file.exists() && file.list() != null) {
            if (file.list().length > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取预置文件夹,测试环境预置到sd卡，生产环境预置到应用目录files中
     *
     * @return 预置文件夹file
     */
    public static File getPreSetFile() {
        // TODO
        String preSetDir = "";
        preSetDir = getContext().getFilesDir() + File.separator + PRE_SET_DIR;
        File file = new File(preSetDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取预置书城压缩包的拷贝路径
     *
     * @return
     */
    public static String getPreSetBookStoreZip() {
        return getPreSetFile().toString() + File.separator + BookStore_Zip;
    }

    /**
     * 预置字体文件路径
     *
     * @return
     */
    public static String getPreSetTTF() {
        return getPreSetReadDir() + File.separator + PreSet_Ttf;
    }

    public static String getPreSetEnMonoTTF() {

        return getPreSetReadDir() + File.separator + PreSet_EnMonoTtf;
    }

    public static String getPreSetKoreaTTF() {

        return getPreSetReadDir() + File.separator + PreSet_KoreaTtf;
    }


    public static String[] getPreSetEnTTF() {
        final String[] enttfNames = PreSet_EnTtf;
        String[] enTTfPaths = new String[enttfNames.length];
        for (int i = 0; i < enttfNames.length; i++) {
            enTTfPaths[i] = getPreSetReadDir() + File.separator + enttfNames[i];
        }
        return enTTfPaths;
    }

    /**
     * 阅读相关文件目录
     *
     * @return
     */
    public static String getPreSetReadDir() {

        return getPreSetFile().toString() + File.separator + PreSet_ReadDir;
    }

    /**
     * 阅读相关文件zip文件路径
     *
     * @return
     */
    public static String getPreSetReadZip() {

        return getPreSetFile().toString() + File.separator + PreSet_ReadZip;
    }

    public static String getBookStoreDir() {
        return getPreSetFile().toString() + File.separator;
    }

    /**
     * 阅读结束页图片
     *
     * @return
     */
    public static String getPreSetReadEndPageImg(){
        return getPreSetFile().toString()  + File.separator + PreSet_ReadEndPageImg;
    }


    /**
     * 词库文件路径
     *
     * @return
     */
    public static String getPreSetDictXdb() {

        return getPreSetReadDir() + File.separator + PreSet_DictXdb;
    }

    /**
     * 词库文件路径
     *
     * @return
     */
    public static String getPreSetDictRule() {

        return getPreSetReadDir() + File.separator + PreSet_DictRule;
    }

    /**
     * 字典文件目录
     *
     * @return
     */
    public static String getReadDictDir() {
        String preSetDir = "";
        if (checkMounted()) {
            preSetDir = getRootPath(getContext()) + PRE_SET_DIR;
        } else {
            preSetDir = getContext().getFilesDir() + File.separator + PRE_SET_DIR;
        }
        File file = new File(preSetDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return preSetDir + File.separator + ReadDict;
    }

    public static boolean isReadExists() {
        File readDirFile = new File(getPreSetReadDir());
        File[] childs = readDirFile.listFiles();
        if (!readDirFile.exists() || (childs == null || childs.length < 9)) {
            return false;
        }
        return true;
    }

    public static String getReadComposingCacheDir() {
//		String dirPath = getAppRootDir()
        String dirPath = getSystemCachePath() + File.separator
                + READCOMPOSING_CACHE;
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirPath;
    }

    public static void saveFile(final Bitmap bm, File file) {
        OutputStream outStream = null;
        ByteArrayOutputStream baos = null;
        try {
            /*
             * if(bm.isRecycled()){ return; }
			 */
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            outStream = new FileOutputStream(file);
            outStream.write(data, 0, data.length);
        } catch (Exception e) {
            LogM.d("save","saveFile  error");
            e.printStackTrace();
        } finally {
            closeStream(outStream);
            closeStream(baos);
        }
    }

    public static Drawable getStartPageDrawable(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                Bitmap bt = BitmapFactory.decodeFile(path);
                if (bt.getWidth() > IMAGE_DEFAULT_WIDTH
                        && bt.getHeight() > IMAGE_DEFAULT_HEIGHT) {
                    return new BitmapDrawable(bt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // return BitmapDrawable.createFromPath(path);
        }
        return null;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String initTxtCoverPath() {
        String path = DROSUtility.getCachePath();
        File file = new File(path + "txt_cover.png");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        //writeStringToFile(
        //        getContext().getResources().openRawResource(R.raw.txt_cover), file);
        return file.getAbsolutePath();
    }

    // 缓存书架推荐数据
    public static void writeShelfRecommandDataToFile(String data, String fileName) {
        if (checkMounted()) {
            String filepath = getRootPathOnSdcard() + BOOK_SHELF_RECOMMAND_CACHE;
            File file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {

                file = new File(getRootPathOnSdcard()
                        + BOOK_SHELF_RECOMMAND_CACHE + fileName);
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream(file));
                out.writeObject(data);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readShelfRecommandDataFromFile(String fileName) {
        String result = null;
        File file = new File(getRootPathOnSdcard() + BOOK_SHELF_RECOMMAND_CACHE
                + fileName);
        if (file.exists() && file.length() > 0) {
            try {
                ObjectInputStream is = new ObjectInputStream(
                        new FileInputStream(file));
                result = (String) is.readObject();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取修改头像时拍照照片相对路径
     *
     * @param context
     * @return
     */
    public static String getUserHeadPortraitsTakePhotoPath(Context context) {
        String path = "";

        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                path = getRootPathOnSdcard();
            }
        }

        return path;
    }

    /**
     * 获取阅历截图绝对路径
     *
     * @param context
     * @return
     */
    public static String getPersonalHistoryPrintscreenPath(Context context) {
        String path = "";

        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                path = getRootPathOnSdcard() + "share.jpg";
            }
        }

        return path;
    }

    public static String getPersonalShakeSharePath(Context context) {
        String path = "";
        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                path = getRootPathOnSdcard() + "shakeshare.jpg";
            }
        }

        return path;
    }

    public static final String READFILE_DIR = "readfile";
    public static final String IMAGE_CACHE_DIR = "ImageCache";
    public static final String RESOURCE_DOWNLOAD_DIR = "ResourceDownload";
    public static final String UNDEFINE_DIR = "undefine";
    public static final String PREREAD_DIR = "preread";
    public static final String BOOKSTORE_DIR = "bookstore";
    public static final String BOOK_DIR = "book";
    public static final String DDEPUB_DIR = "ddepub";
    public static final String DDEPUB_RES_DIR = "res";
    public static final String PART_BOOK_DIR = "PartBook";
    public static final String LISTEN_BOOK_DIR = "ListenBook";
    public static final String PRESET_FILENAME_CSS = "original_style.css";
    public static final String PRESET_FILENAME_DICT_XDB = "dict-gbk.xdb";
    public static final String PRESET_FILENAME_RULES = "rules.ini";
    public static final int BUFFER_SIZE = 10 * 1024; // 缓冲大小，10k

    /**
     * 获取手机外部空间大小
     *
     * @return byte size
     */
    public static long getTotalExternalMemorySize() {
        if (checkMounted()) {
            long blockSize = 0;
            long totalBlocks = 0;
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                blockSize = stat.getBlockSize();
                totalBlocks = stat.getBlockCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取外部存儲路徑
     *
     * @return
     */
    public static String getExternalFilesDir() {
        String rootDir = "";
        if (checkMounted()) {
            rootDir = getContext().getExternalFilesDir(null) + File.separator;
        } else {
            return getAppRomDir();
        }
        return rootDir;
    }

    /**
     * 获取存储根文件夹
     *
     * @return
     */
    public static String getAppRootDir() {
        String rootDir = APP_DIR + File.separator;
        if (checkMounted()) {
            if (hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                rootDir = Environment.getExternalStorageDirectory() + File.separator + rootDir;
            else
                rootDir = getExternalFilesDir() + rootDir;
        } else {
            return getAppRomDir();
        }
        return rootDir;
    }

    /**
     * 获取应用安装目录下根文件夹
     *
     * @return
     */
    private static String getAppRomDir() {
        String romDir = getContext().getFilesDir()
                + File.separator + APP_DIR + File.separator;
        return romDir;
    }

    /**
     * 获取图片缓存路径
     *
     * @return
     */
    public static String getImageCacheDir() {
        return getAppRootDir() + IMAGE_CACHE_DIR + File.separator;
    }

    public static String getResourceDownloadPath() {
        return getAppRootDir() + RESOURCE_DOWNLOAD_DIR + File.separator;
    }

    public static String getSPEpubDir(String mediaId) {
        String ddepubDir = getAppRootDir() + DDEPUB_DIR + File.separator;
        File dir = new File(ddepubDir);
        if (!dir.exists())
            dir.mkdir();
        if (dir.isFile()) {
            dir.delete();
            dir.mkdirs();
        }

        return ddepubDir + mediaId + File.separator;
    }

    public static String getSPEpubResDir(String mediaId) {
        String ddepubResDir = getSPEpubDir(mediaId) + DDEPUB_RES_DIR + File.separator;
        File dir = new File(ddepubResDir);
        if (!dir.exists())
            dir.mkdir();

        return ddepubResDir;
    }

    /**
     * 获取书的路径
     *
     * @param bookId
     * @return
     */
    public static String getBookDir(String bookId) {
        String path = getAppRootDir() + BOOK_DIR + File.separator + bookId;
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        if (dir.isFile()) {
            dir.delete();
            dir.mkdirs();
        }
        return path + File.separator;
    }

    /**
     * 获原创书的路径
     *
     * @param bookId
     * @return
     */
    public static String getPartBookDir(String bookId) {
        String path = getAppRootDir() + PART_BOOK_DIR + File.separator + bookId;
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        if (dir.isFile()) {
            dir.delete();
            dir.mkdirs();
        }
        return path + File.separator;
    }

    /**
     * 获听书的路径
     *
     * @param bookId
     * @return
     */
    public static String getListenBookDir(String bookId) {
        String path = getExternalFilesDir() + LISTEN_BOOK_DIR + File.separator + bookId;
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        if (dir.isFile()) {
            dir.delete();
            dir.mkdirs();
        }
        return path + File.separator;
    }

    /**
     * 获取本地启动图路径
     *
     * @return
     */
    public static String getBootBitmapPath() {
        File dir = new File(getAppRomDir());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return getAppRomDir() + "boot_bitmap";
    }

    public static String getApkDir() {
        String path = getAppRootDir() + "apk/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getFontProductDir(String indentityId, String userName) {
        try {
            String rootPath = getAppRootDir();
            File rootFile = new File(rootPath);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File bookFile = new File(rootFile.getAbsolutePath()
                    + File.separator + userName + File.separator + FONT_DIR
                    + indentityId);
            if (!bookFile.exists()) {
                bookFile.mkdirs();
            }
            return bookFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTTsDir() {
        String rootPath = getAppRootDir();
        File rootFile = new File(rootPath);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }

        return rootPath + BD_TTS_DIR;
    }

        /**
         * 获取发现详情截图绝对路径
         *
         * @return
         */
    public static String getFindDetailPrintscreenPath() {
        return getImageCacheDir() + "find_share.jpg";
    }

    /**
     * 清空指定目录
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    /**
     * 写入apk
     *
     * @param mContext
     */
    public static void writePluginApk(final Context mContext) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                File f = DangdangFileManager.getApkFile(
                        DangdangFileManager.getAppApkDir(), PLUGINAPK_NAME);
                if (DangdangFileManager.getFileManagerInstance()
                        .copyFilesFromAssets(mContext, PLUGINAPK_NAME, f))
                    return;
                f = DangdangFileManager.getApkFile(
                        DangdangFileManager.getSdcardApkDir(), PLUGINAPK_NAME);
                if (!DangdangFileManager.getFileManagerInstance()
                        .copyFilesFromAssets(mContext, PLUGINAPK_NAME, f)) {
                    // UiUtil.showToast(mContext, R.string.sdcard_space_error);
                }
            }

        }).start();
    }

    /**
     * 从assets目录中复制文件内容(先放到系统目录，写失败的话放到SD卡上)
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String 原文件路径 如：aa
     * @param newFile String 复制后路径 如：xx:/bb/cc
     */
    public boolean copyFilesFromAssets(final Context context,
                                       final String oldPath, final File newFile) {
        boolean isSuccess = false;

        try {
            // 如果是文件
            InputStream is = context.getAssets().open(oldPath);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;

        }

        return isSuccess;
    }

    public static Uri getUriFromFile(Context context, File file) {
        Uri mTempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //mTempUri = getImageContentUri(context,file);
            mTempUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            mTempUri = Uri.fromFile(file);
        }

        return mTempUri;
    }

    public static boolean hasPermissions(String perm) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//
//        if (mContext != null && ContextCompat.checkSelfPermission(mContext, perm)
//                != PackageManager.PERMISSION_GRANTED) {
//            return false;
//        }

        return false;
    }
}
