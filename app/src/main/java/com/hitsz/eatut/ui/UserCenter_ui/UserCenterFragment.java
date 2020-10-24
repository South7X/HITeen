package com.hitsz.eatut.ui.UserCenter_ui;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatTextView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.popupwindowlibrary.view.ScreenPopWindow;
import com.hitsz.eatut.LoginActivity;
import com.hitsz.eatut.MainActivity;
import com.hitsz.eatut.R;
import com.hitsz.eatut.SignUpActivity;
import com.hitsz.eatut.StatisticsActivity;
import com.hitsz.eatut.adapter.dish;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.UserInfo;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.hitsz.eatut.BaseClass.getUnitedList;
import static com.hitsz.eatut.BaseClass.traversalWholeLinkedList;

public class UserCenterFragment extends Fragment {

    private Button change_btn;
    private Button stastic_btn;
    private Button about_btn;
    private String phoneNumber;
    private String name;
    private TextView user_name;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_center, container, false);
        change_btn=root.findViewById(R.id.button_change);
        stastic_btn=root.findViewById(R.id.button_stastic);
        user_name=root.findViewById(R.id.user_center_name);
        about_btn=root.findViewById(R.id.about);
        init();
        return root;
    }

    private void init(){

        SharedPreferences pref2 = getActivity().getSharedPreferences("currentID",MODE_PRIVATE);
        int userID = pref2.getInt("userID", -1);
        List<UserInfo> userInfos = LitePal.where("id == ?", ""+userID).find(UserInfo.class);
        for (UserInfo user: userInfos){
            phoneNumber = user.getTelephoneNumber();
            name = user.getUserName();
        }
        user_name.setText(name);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: intent data put in
                Intent intent = new Intent(getActivity(), ChangeUserInfoActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivityForResult(intent, 1);
            }
        });
        stastic_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent Statistics_intent = new Intent(getActivity(), StatisticsActivity.class);
                startActivity(Statistics_intent);
            }
        });
        about_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent About_intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(About_intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("userName");
                    if(!TextUtils.isEmpty(returnedData)){
                        //设置用户名
                        user_name.setText(returnedData);
                    }

                }
                break;
            default:
        }
    }
}