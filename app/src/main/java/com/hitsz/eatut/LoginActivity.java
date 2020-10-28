package com.hitsz.eatut;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.google.android.material.snackbar.Snackbar;
import com.hitsz.eatut.database.CanteenInfo;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.UserInfo;
import com.hitsz.eatut.database.WindowInfo;
import com.hitsz.eatut.managerActivities.ManagerActivity;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static com.hitsz.eatut.BaseClass.buildTreeFromDatabase;
import static com.hitsz.eatut.BaseClass.createLinkedListFromDatabase;
import static com.hitsz.eatut.BaseClass.isPasswordValid;
import static com.hitsz.eatut.BaseClass.isUserExist;
import static com.hitsz.eatut.BaseClass.md5;

/**
 * @author lixiang
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    public static final String TAG = "LoginActivity";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private String managerNumber = "88888888888";//15888858888
    private String managerPassword = "12345678";
    public final int maxUserNum = 100000;
    private int[] itsHashCodeToID = new int[maxUserNum];
    private int countUserNumber = 0;
    // UI references.
    private AutoCompleteTextView mTelephoneView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        //Connector.getDatabase();
        Stetho.initializeWithDefaults(this);
        SQLiteDatabase db = LitePal.getDatabase();


        //TODO: remove after connect to a real server
        if (!isUserExist(managerNumber)){
            addManagerToDatabase(managerNumber, managerPassword);
        }

        mTelephoneView = (AutoCompleteTextView) findViewById(R.id.telephone);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        /**
         * 忘记密码
         */
        TextView forgetPsd = findViewById(R.id.forget_psd);
        forgetPsd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);
            }

        });

        /**
         * Sign up Activity
         */
        Button mSignUpButton =(Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        Button mPhoneSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        mPhoneSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderCount();
                MainCount();
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void addManagerToDatabase(String phone, String password) {
        UserInfo user1 = new UserInfo();
        user1.setTelephoneNumber(phone);
        user1.setPassword(md5(password));
        user1.save();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        //当系统版本低于android_M时，跳过权限检查
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        //当系统版本大于等于android_M时，执行权限申请代码
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        //当自身已经被允许权限中没有READ_CONTACTS时，申请通讯录读取权限READ_CONTACTS
        //shouldShowRequestPermissionRationale ==> 是否需要调用系统的权限申请界面
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mTelephoneView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            //展示请求权限界面，第一个参数是权限数组，第二个是请求码
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            //请求码 对应上面请求的请求码
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();//读取联系人列表内的phone
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid phone, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        //登录信息提交的异步任务已经实例化，则无需进行操作，第一次执行attemptLogin()时，mAuthTask并未初始化
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.重设用户名和密码框的错误提示
        mTelephoneView.setError(null);
        //setError方法是TextView下面的方法，主要是提示一个错误信息，
        //内部有系统集成的错误提示图标，原理是在TextView的右边出现一个Drawable
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mTelephoneView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;//是否退出执行登陆进程
        View focusView = null;
        //焦点View，当某个输入框输入信息不符合标准时，
        // 不执行登陆进程，并锁定焦点到那个输入控件

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid phone address.当用户名不为空，判断密码是否符合标准
        if (TextUtils.isEmpty(phone)) {
            mTelephoneView.setError(getString(R.string.error_field_required));
            focusView = mTelephoneView;
            cancel = true;
        } else if (!BaseClass.isPhoneValid(phone)) {
            // 输入不是手机号的格式
            mTelephoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mTelephoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.在上面的操作中出现错误了，不执行具体的登录，并且把焦点切换到上面去
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.开启滚动条，执行登录的异步任务
            showProgress(true);
            mAuthTask = new UserLoginTask(phone, password);
            mAuthTask.execute((Void) null);
        }
    }

//    private boolean isInputValid(String phone) {
//        return (phone.length() == 11 | phone.length() == 9);
//    }
//
//    private boolean isPasswordValid(String password) {
//        return password.length() >= 8;
//    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only phone addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Phone
                .CONTENT_ITEM_TYPE},

                // Show primary phone addresses first. Note that there won't be
                // a primary phone address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> phones = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            phones.add(cursor.getString(ProfileQuery.NUMBER));
            cursor.moveToNext();
        }

        addPhonesToAutoComplete(phones);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addPhonesToAutoComplete(List<String> phoneAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, phoneAddressCollection);

        mTelephoneView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                // TODO: search for this method
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
        };

        int NUMBER = 0;
        int IS_PRIMARY = 1;
    }

//    /**
//     * Represents an asynchronous login/registration task used to authenticate
//     * the user.
//     */
//    private boolean isUserExist(String phone) {
//        int hashID = BaseClass.getHashCodeByTelephone(Long.parseLong(phone), maxUserNum);
//        if (itsHashCodeToID[hashID] == 0) return false;
//        UserInfo user = LitePal.find(UserInfo.class, itsHashCodeToID[hashID]);
//        return user.getTelephoneNumber().equals(phone);
//    }


    /**
     * Check if password correct compared with dataset, input can be phone
     * @param input: input phoneNumber
     * @param password: input password
     * @return True , False
     */
    private boolean isPasswordCorrect(String input, String password) {
        List<UserInfo> users;
        users = LitePal.where("telephoneNumber = ?", input).find(UserInfo.class);
        for (UserInfo user: users) {
            return user.getPassword().equals(md5(password));
        }
        return false;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhone;
        private final String mPassword;

        UserLoginTask(String phone, String password) {
            mPhone = phone;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //后台任务，耗时操作此处执行，该处代码执行在子线程
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            /*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mPhone)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
            */
            if (BaseClass.isUserExist(mPhone)){
                Log.v(TAG, "background: user exist");
                return isPasswordCorrect(mPhone, mPassword);
            } else {
                return true;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
                Log.v(TAG, "onPostExecute: in success");
                if (!isUserExist(mPhone)) {
                    Log.v(TAG, "onPostExecute: user not exist");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setTitle("来啦老铁！");
                    dialog.setMessage("快去注册吧，这样才能登录喔～");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Intent intent2 = new Intent(LoginActivity.this, ManagerActivity.class);
//                SavePhone(mPhone);
                int userID = (LitePal.where("telephoneNumber like ?", mPhone).find(UserInfo.class)).get(0).getId();
                Log.d("userID", Integer.toString(userID));
                SaveUserID(userID);
                Log.d("managerPassword", mPassword);
                Log.d("managerPassword", managerPassword);
                if (mPhone.equals(managerNumber)&&mPassword.equals(managerPassword)) startActivity(intent2);//管理者界面
                else startActivity(intent);//用户界面
            } else {
                Log.v(TAG, "onPostExecute: in else");
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    private void SaveUserID(int userID){
        //保存当前用户ID
        SharedPreferences pref=this.getSharedPreferences("currentID",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("userID",userID);
        editor.apply();
    }
    private void OrderCount(){
        SharedPreferences pref=this.getSharedPreferences("OrderCount",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        Boolean isBegin=pref.getBoolean("isBegin",false);
        if(isBegin==false)
        {
            editor.putInt("count",0);
            editor.putBoolean("isBegin",true);
            editor.apply();
        }
    }
    private void MainCount(){
        SharedPreferences pref=this.getSharedPreferences("MainCount",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        Boolean isBegin=pref.getBoolean("isBegin",false);
        if(isBegin==false)
        {
            initInformation();
            editor.putBoolean("isBegin",true);
            editor.apply();
        }
    }

    private void initCanteen(int num, String[] canteenName, int[] canteenImage, int[] canteenWindowNumber) {
        //食堂
        for (int i = 0; i < num; i ++){
            CanteenInfo canteen = new CanteenInfo();
            canteen.setCanteenName(canteenName[i]);
            canteen.setImageID(canteenImage[i]);
            canteen.setCanteenWindowNumber(canteenWindowNumber[i]);
            canteen.save();

        }
    }

    private void initWindow(int num, String belongToCanteenName, String[] windowName, int [] windowImage, int[] windowDishNumber) {
        //档口
        for (int i = 0; i < num; i ++){
            WindowInfo window = new WindowInfo();
            window.setWindowName(windowName[i]);
            window.setBelongToCanteenName(belongToCanteenName);
            window.setImageID(windowImage[i]);
            window.setWindowDishNumber(windowDishNumber[i]);
            window.save();
        }
    }

    private void initDish(int num, String[] dishName, String belongToWindow, String belongToCanteen,
                          String[] tags, float[] dishPrice, int[] dishImage){
        for (int i = 0; i < num; i ++){
            DishInfo dish = new DishInfo();
            dish.setDishName(dishName[i]);
            dish.setBelongToWindow(belongToWindow);
            dish.setBelongToCanteen(belongToCanteen);
            dish.setDishTags(tags[i]);
            dish.setDishPrice(dishPrice[i]);
            dish.setImageID(dishImage[i]);
            dish.save();
        }
    }
    private void initInformation(){
        String[] tags = {"清淡","酸","甜","苦","辣","咸","油炸"};
        int canteenNumber = 3;
        String[] canteenName = {"荔园一食堂", "荔园二食堂", "荔园三食堂"};
        int[] canteenImage = {R.drawable.canteen1, R.drawable.canteen2, R.drawable.canteen3};
        int[] canteenWindowNumber = {15, 5, 10};
        initCanteen(canteenNumber, canteenName, canteenImage, canteenWindowNumber);

        //initialize canteen3
        String[] windowName_canteen3 = {"乐记水饺", "开饭了", "兰州拉面", "粤式烧腊"};
        int[] windowImage_canteen3 = {R.drawable.jiaozi_window, R.drawable.kaifanle_window, R.drawable.lamian_window, R.drawable.shaola_window};
        int[] windowDishNumber_canteen3 = {3, 10, 10, 10};
        initWindow(4, canteenName[2], windowName_canteen3, windowImage_canteen3, windowDishNumber_canteen3);

        //initialize dishes
        String[] dishName_window1_canteen3 = {"猪肉白菜水饺", "猪肉玉米水饺", "猪肉芹菜水饺", "韭菜鸡蛋水饺"};
        String[] dishTags_window1_canteen3 = {tags[0], tags[0], tags[0], tags[0]};
        float[] dishPrice_window1_canteen3 = {12, 9, 10, 9};
        int[] dishImage_window1_canteen3 = {R.drawable.dumplings, R.drawable.dumplings, R.drawable.dumplings, R.drawable.dumplings};
        initDish(4, dishName_window1_canteen3, windowName_canteen3[0], canteenName[2],
                dishTags_window1_canteen3, dishPrice_window1_canteen3, dishImage_window1_canteen3);


        String[] dishName_window2_canteen3 = {"蒸南瓜", "口水鸡", "鸡排", "番茄炒蛋"};
        String[] dishTags_window2_canteen3 = {tags[2]+'$'+tags[0], tags[4], tags[6], tags[1]+'$'+tags[2]};
        float[] dishPrice_window2_canteen3 = {1, 8, 8, 4};
        int[] dishImage_window2_canteen3 = {R.drawable.nangua, R.drawable.koushuiji, R.drawable.jipai, R.drawable.fanqiechaodan};
        initDish(4, dishName_window2_canteen3, windowName_canteen3[1], canteenName[2],
                dishTags_window2_canteen3, dishPrice_window2_canteen3, dishImage_window2_canteen3);

        String[] dishName_window3_canteen3 = {"土豆牛肉拌面", "传统牛肉拉面"};
        String[] dishTags_window3_canteen3 = {tags[4], tags[0]};
        float[] dishPrice_window3_canteen3 = {15, 8};
        int[] dishImage_window3_canteen3 = {R.drawable.tudouniuroubanmian, R.drawable.niuroulamian};
        initDish(2, dishName_window3_canteen3, windowName_canteen3[2], canteenName[2],
                dishTags_window3_canteen3, dishPrice_window3_canteen3, dishImage_window3_canteen3);

        String[] dishName_window4_canteen3 = {"烧鸭饭", "鸡腿饭", "猪脚饭"};
        String[] dishTags_window4_canteen3 = {tags[5], tags[5], tags[5]};
        float[] dishPrice_window4_canteen3 = {13,15,15};
        int[] dishImage_window4_canteen3 = {R.drawable.shaoyafan, R.drawable.jituifan, R.drawable.zhujiaofan};
        initDish(3, dishName_window4_canteen3, windowName_canteen3[3], canteenName[2],
                dishTags_window4_canteen3, dishPrice_window4_canteen3, dishImage_window4_canteen3);




        buildTreeFromDatabase();
        createLinkedListFromDatabase();
    }

    /**
     * 注册成功返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("phoneNumber");
                    if(!TextUtils.isEmpty(returnedData)){
                        //设置用户名到 mTelephoneView 控件
                        mTelephoneView.setText(returnedData);
                        //mTelephoneView 控件的setSelection()方法来设置光标位置
                        mTelephoneView.setSelection(returnedData.length());
                    }

                }
                break;
            default:
        }
    }
}

