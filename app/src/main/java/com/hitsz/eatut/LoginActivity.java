package com.hitsz.eatut;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.hitsz.eatut.database.UserInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
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
        //TODO: 修改hashCode函数，避免地址冲突问题
        addNewUserToDatabase("15000050000", "12345678");

        mTelephoneView = (AutoCompleteTextView) findViewById(R.id.telephone);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        Button mPhoneSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        mPhoneSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void addNewUserToDatabase(String phone, String password) {
        UserInfo user1 = new UserInfo();
        countUserNumber = countUserNumber + 1;
        int hashID;
        hashID = BaseClass.getHashCodeByTelephone(Long.parseLong(phone), maxUserNum);
        itsHashCodeToID[hashID] = countUserNumber;
        user1.setTelephoneNumber(phone);
        user1.setPassword(password);
        user1.setId(hashID);
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
        } else if (!isPhoneValid(phone)) {
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

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }

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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private boolean isUserExist(String phone) {
        int hashID = BaseClass.getHashCodeByTelephone(Long.parseLong(phone), maxUserNum);
        if (itsHashCodeToID[hashID] == 0) return false;
        UserInfo user = LitePal.find(UserInfo.class, itsHashCodeToID[hashID]);
        return user.getTelephoneNumber().equals(phone);
    }

    private boolean isPasswordCorrect(String phone, String password) {
        int hashID = BaseClass.getHashCodeByTelephone(Long.parseLong(phone), maxUserNum);
        if (itsHashCodeToID[hashID] == 0) return false;
        UserInfo user = LitePal.find(UserInfo.class, itsHashCodeToID[hashID]);
        return user.getPassword().equals(password);
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
            if (isUserExist(mPhone)){
                return isPasswordCorrect(mPhone, mPassword);
            } else {
                addNewUserToDatabase(mPhone, mPassword);
                Log.d(TAG, "register new user");
                return true;
            }
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
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
}

