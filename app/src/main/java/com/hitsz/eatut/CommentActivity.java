package com.hitsz.eatut;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.adapter.commentAdapter;
import com.hitsz.eatut.adapter.commentItem;
import com.hitsz.eatut.adapter.orderFoodAdapter;
import com.hitsz.eatut.database.Comment;
import com.hitsz.eatut.database.UserInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.hitsz.eatut.BaseClass.getCanteenCommentKeyword;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{
    private String canteenName;
    private int userID;

    private RecyclerView recyclerView;
    private TextView canCommentName;
    private TextView canCommentKeyWord;
    private EditText commentInput;
    private RadioButton anonymousFlag;
    private Button commentSubmit;

    private AlertDialog.Builder builder;
    private List<commentItem> commentItemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        canteenName = intent.getStringExtra("extra_data");
        SharedPreferences pref2 = this.getSharedPreferences("currentID",MODE_PRIVATE);
        userID = pref2.getInt("userID", -1);

        findView();
        //食堂信息
        initCanteenIntro();
        //评论展示
        initComment();
        initRecycle();
    }
    public void onClick(View v) {
        if (v.getId() == R.id.comment_submit_btn) {
            //把评论存到数据库中
            String commentText = commentInput.getText().toString();
            if (commentText.isEmpty()) {
                DialogEmpty();
            } else {
                long postTime = System.currentTimeMillis();
                String userName;
                if (anonymousFlag.isChecked())
                    userName = "匿名";
                else {
                    UserInfo userInfo = LitePal.where("id=?", "" + userID).find(UserInfo.class).get(0);
                    userName = userInfo.getUserName();
                }
                Comment comment = new Comment();
                comment.setPostTime(postTime);
                comment.setCommentText(commentText);
                comment.setUserName(userName);
                comment.setAnonymousFlag(anonymousFlag.isChecked());
                comment.setCommentCanteen(canteenName);
                comment.setCommentKeyWords("还没完成");
                comment.save();
                DialogSuccess();
            }
        }
    }
    private void findView(){
        recyclerView=findViewById(R.id.comment_recycle);
        canCommentName=findViewById(R.id.canteen_comment_name);
        canCommentKeyWord=findViewById(R.id.canteen_comment_keyword);
        commentInput=findViewById(R.id.comment_input);
        anonymousFlag=findViewById(R.id.anonymous_flag);
        commentSubmit=findViewById(R.id.comment_submit_btn);
        commentSubmit.setOnClickListener(this);
        builder=new AlertDialog.Builder(CommentActivity.this);
    }
    private void initCanteenIntro(){
        canCommentName.setText(canteenName);
        canCommentKeyWord.setText(getCanteenCommentKeyword(canteenName));
    }
    private void initComment(){
        //找到当前食堂对应的评论数据
        List<Comment> comments = LitePal.where("commentCanteen=?", canteenName).find(Comment.class);
        commentItemList.clear();
        for(Comment one:comments){
            String userName = one.getUserName();
            long postTime = one.getPostTime();
            String commentContent = one.getCommentText();
            commentItem item = new commentItem(postTime, commentContent, userName);
            commentItemList.add(item);
        }
        //按照postTime降序排序
        Collections.sort(commentItemList, new Comparator<commentItem>() {
            @Override
            public int compare(commentItem c1, commentItem c2) {
                long postTime1 = c1.getPostTime();
                long postTime2 = c2.getPostTime();
                return (int) (postTime2 - postTime1);
            }
        });
    }
    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter adapter = new commentAdapter(commentItemList);
        recyclerView.setAdapter(adapter);
    }
    private void DialogEmpty(){
        builder.setTitle("提交失败");
        builder.setMessage("您还没有输入评论！");
        builder.setPositiveButton("okk", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog=builder.show();
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void DialogSuccess(){
        builder.setTitle("提交成功");
        builder.setMessage("评论提交成功！");
        builder.setPositiveButton("okk", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog=builder.show();
                dialog.dismiss();
                //把编辑框清空
                commentInput.setText("");
                //重新init
                initComment();
                initRecycle();
            }
        });
        builder.show();
        canCommentKeyWord.setText(getCanteenCommentKeyword(canteenName));
    }
}
