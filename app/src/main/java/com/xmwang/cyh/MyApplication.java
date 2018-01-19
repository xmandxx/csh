package com.xmwang.cyh;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.xmwang.cyh.common.Data;

/**
 * @Description: MyApplication
 * @author: CTS
 * @date: 2017/2/16 10:36
 */
public class MyApplication extends Application {
    private static MyApplication mcontext;
    public static MyApplication instances;
//    /**
//     * GreenDao相关
//     */
//    private DaoMaster.DevOpenHelper mHelper;
//    private DaoMaster.DevOpenHelper mHelper2;
//    private SQLiteDatabase db;
//    private SQLiteDatabase db2;
//    private DaoMaster mDaoMaster;
//    private DaoMaster mDaoMaster2;
//    private DaoSession mDaoSession;
//    private DaoSession mDaoSession2;
//
//    private PackageInfo packInfo;
//    @SuppressLint("SdCardPath")
//    private static final String DB_PATH = "/data/data/com.lnjm.nongye/databases/";
//    //    private String DB_PATH = "";
//    private static final String DB_NAME = "area2.db";
//
//    public static String APP_ID = "wxac995e7a84d8a0b5";

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = this;
        instances = this;
        //刷新用户数据
        Data.instance.reUserInfo();
//        ActiveAndroid.initialize(this);//初始化ORM框架
//        Hawk.init(this).build();
////        getAppInfo();
////        DB_PATH = packInfo.applicationInfo.dataDir + "/";
//        copyDBToDatabases();
//        setDatabase();
//        setDatabase2();

//        /**
//         * 友盟相关
//         */
//        UMShareAPI.get(this);
//        PlatformConfig.setWeixin("wxac995e7a84d8a0b5", "10740acf9080574b3a1a6b8dee41f5e0");
//        PlatformConfig.setQQZone("1106237451", "dSwsZZBBsajUlxwh");
//        //极光推送
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);            // 初始化 JPush

        //当程序发生Uncaught异常时捕获
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable e) {
//                //做你要做的处理，比如把e.getMessage()保存到文件，发送一个email等等，不是本篇重点，不再赘述
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });

//        //初始化JMessage-sdk，第二个参数表示开启漫游
//        JMessageClient.setDebugMode(true);
//        JMessageClient.init(getApplicationContext(), true);
//        SharePreferenceManager.init(getApplicationContext(), JCHAT_CONFIGS);
//        //设置Notification的模式
//        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
//        //注册Notification点击的接收器
//        new NotificationClickEventReceiver(getApplicationContext());
////        UserInfo myInfo = JMessageClient.getMyInfo();
////        if (myInfo == null && UserInfoUtils.getInstance().getPhone() != null) {
////            loginJiGuang();
////        }
    }

    public static MyApplication getInstances() {
        return instances;
    }

    public static Context getContext() {
        return mcontext;
    }

//    private void getAppInfo() {
//        // 获取packageManager的实例
//        PackageManager packageManager = getPackageManager();
//        // getPackageName()是你当前类的包名，0代表是获取版本信息
//        try {
//            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * assets目录下的db转移到databases
//     */
//    public void copyDBToDatabases() {
//        try {
//            String outFileName = DB_PATH + DB_NAME;
//            File file = new File(DB_PATH);
//            if (!file.mkdirs()) {
//                file.mkdirs();
//            }
//            File dataFile = new File(outFileName);
//            if (dataFile.exists()) {
//                dataFile.delete();
//            }
//            InputStream myInput;
//            myInput = getApplicationContext().getAssets().open(DB_NAME);
//            OutputStream myOutput = new FileOutputStream(outFileName);
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = myInput.read(buffer)) > 0) {
//                myOutput.write(buffer, 0, length);
//            }
//            myOutput.flush();
//            myOutput.close();
//            myInput.close();
//        } catch (IOException e) {
//            Log.e("error--->", "error--->" + e.toString());
//            e.printStackTrace();
//        }
//
//    }


//    public static boolean copyRawDBToApkDb(Context context, int copyRawDbResId, String apkDbPath, String dbName, boolean refresh)
//            throws IOException {
//        boolean b = false;
//
//        File f = new File(apkDbPath);
//        if (!f.exists()) {
//            f.mkdirs();
//        }
//
//        File dbFile = new File(apkDbPath + dbName);
////        b = isDbFileExists(dbFile, refresh);
//        b = dbFile.exists();
//        if (!b) {
//            InputStream is = context.getResources().openRawResource(copyRawDbResId);
//
//            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
//            ZipEntry entry;
//
//            while ((entry = zis.getNextEntry()) != null) {
//                int size;
//                byte[] buffer = new byte[1024 * 2];
//
//                OutputStream fos = new FileOutputStream(apkDbPath + entry.getName());
//                BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
//
//                while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
//                    bos.write(buffer, 0, size);
//                }
//                bos.flush();
//                bos.close();
//            }
//            zis.close();
//            is.close();
//        }
//        return !b;
//    }


//    /**
//     * 设置greenDao
//     */
//    private void setDatabase() {
//        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
//        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
//        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
//        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        mHelper = new DaoMaster.DevOpenHelper(this, "area2.db", null);
//        db = mHelper.getWritableDatabase();
//        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//        mDaoMaster = new DaoMaster(db);
//        mDaoSession = mDaoMaster.newSession();
//    }
//
//    /**
//     * 设置greenDao
//     */
//    private void setDatabase2() {
//        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
//        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
//        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
//        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        mHelper2 = new DaoMaster.DevOpenHelper(this, "his.db", null);
//        db2 = mHelper2.getWritableDatabase();
//        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//        mDaoMaster2 = new DaoMaster(db2);
//        mDaoSession2 = mDaoMaster2.newSession();
//    }
//
//    public DaoSession getDaoSession() {
//        return mDaoSession;
//    }
//
//    public DaoSession getDaoSession2() {
//        return mDaoSession2;
//    }
////
////    public SQLiteDatabase getDb() {
////        return db;
////    }
////
////    /**
////     * 极光聊天相关
////     */
////    public static void setPicturePath(String appKey) {
////        if (!SharePreferenceManager.getCachedAppKey().equals(appKey)) {
////            SharePreferenceManager.setCachedAppKey(appKey);
////            PICTURE_DIR = "sdcard/JChatDemo/pictures/" + appKey + "/";
////        }
////    }
////
////    public static UserEntry getUserEntry() {
////        return UserEntry.getUser(JMessageClient.getMyInfo().getUserName(), JMessageClient.getMyInfo().getAppKey());
////    }
////
//////    /**
//////     * 登录极光im
//////     */
//////    private void loginJiGuang() {
//////        //登录极光im
//////        Log.e("测试登录", UserInfoUtils.getInstance().getPhone());
//////        JMessageClient.login(UserInfoUtils.getInstance().getPhone(), "passwordlnjm.com", new BasicCallback() {
//////            @Override
//////            public void gotResult(final int status, final String desc) {
//////                if (status == 0) {
//////                    //刷新消息页面
//////                    EventBus.getDefault().post(new ConversationListEvent());
//////                    String username = JMessageClient.getMyInfo().getUserName();
//////                    String appKey = JMessageClient.getMyInfo().getAppKey();
//////                    UserEntry user = UserEntry.getUser(username, appKey);
//////                    if (null == user) {
//////                        user = new UserEntry(username, appKey);
//////                        user.save();
//////                    }
//////                    Log.e("极光登录", "登陆成功");
//////                } else {
//////                    HandleResponseCode.onHandle(mcontext, status, false);
//////                }
//////                Log.e("极光登录", "status = " + status);
//////            }
//////
//////        });
//////    }
////
////    @Override
////    public void onTerminate() {
////        super.onTerminate();
////        ActiveAndroid.dispose();
////    }
}
