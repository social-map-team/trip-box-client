
/**
 * Created by gxyzw_000 on 2014/11/29.
 */
package com.socialmap.yy.travelbox.module.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.adpater.ExpandAdapter;
import com.socialmap.yy.travelbox.data.DBHelper;
import com.socialmap.yy.travelbox.model.User;
import com.socialmap.yy.travelbox.utils.TbsClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.socialmap.yy.travelbox.utils.TbsClient.getInstance;

public class FriendsActivity extends Activity implements OnChildClickListener {

    private ExpandableListView mListView = null;
    private ExpandAdapter mAdapter = null;
    private List<List<User>> mData = new ArrayList<List<User>>();
    private String[] function = new String[]{"个人资料", "位置分享"};
    private EditText inputString;
    private DBHelper dbHelper;
    private int[] mGroupArrays = new int[]{
            R.array.spongebob,
            R.array.patrick,
            R.array.sandy};

    private int[] mDetailIds = new int[]{
            R.array.spongebob_detail,
            R.array.patrick_detail,
            R.array.sandy_detail};

    private int[][] mImageIds = new int[][]{
            {R.drawable.img_00,
                    R.drawable.img_01,
                    R.drawable.img_02},
            {R.drawable.img_03,
                    R.drawable.img_04,
                    R.drawable.img_05,
                    R.drawable.img_06,
                    R.drawable.img_07,
                    R.drawable.img_08,
                    R.drawable.img_09},
            {R.drawable.img_10,
                    R.drawable.img_11}};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        mListView = new ExpandableListView(this);
        mListView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        setContentView(mListView);

        mListView.setGroupIndicator(getResources().getDrawable(
                R.drawable.expander_floder));
        mAdapter = new ExpandAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(this);
    }

    /* <span style="color:#ff0000;">
     * ChildView 设置 布局很可能onChildClick进不来，要在 ChildView layout 里加上
     * android:descendantFocusability="blocksDescendants",
     * 还有isChildSelectable里返回true

    </span>*/
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


        User user = mAdapter.getChild(groupPosition, childPosition);
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("")
                .setIcon(android.R.drawable.ic_menu_more)
                .setItems(function, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)

                    {
                        if (which == 0) {
                            Intent intent = new Intent();
                            intent.setClass(FriendsActivity.this, FriendsInfoActivity.class);
                            intent.putExtra("user", "");//todo 应该传递什么值过去
                            startActivity(intent);
                        } else if (which == 1) {
                            Toast.makeText(getApplicationContext(), "对其位置分享", Toast.LENGTH_SHORT).show();
                        }


                    }
                })


                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {


                            }
                        }
                )
                .create()
                .show();


        return true;
    }

    private void initData() {


        dbHelper.open();
        List<User> list = new ArrayList<User>();
        Cursor returnCursor = dbHelper.findList(false, "friend_list", new String[]{"id"}, null, null, null, null, "id desc", null);
        while (returnCursor.moveToNext()) {
            String id = returnCursor.getString(returnCursor.getColumnIndexOrThrow("id"));

            User user = new User(mImageIds[1][1], id, getStringArray(mDetailIds[1])[1]);
            list.add(user);
        }
        dbHelper.closeConnection();
        mData.add(list);


        for (int i = 0; i < mGroupArrays.length; i++) {
            String[] childs = getStringArray(mGroupArrays[i]);
            String[] details = getStringArray(mDetailIds[i]);
            for (int j = 0; j < childs.length; j++) {
                User user = new User(mImageIds[i][j], childs[j], details[j]);
                list.add(user);
            }
            mData.add(list);
        }
    }

    private String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friend_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.add_more:
                LayoutInflater inflater = (LayoutInflater) FriendsActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layout = (RelativeLayout) inflater.inflate(
                        R.layout.input_add, null);
                inputString = (EditText) layout
                        .findViewById(R.id.input_add_string);

                new AlertDialog.Builder(FriendsActivity.this)
                        .setTitle("请输入想要添加的好友名")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(layout)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        String str = inputString.getText().toString();

                                        getInstance()
                                                .request("/profile/friend/{id}", "post",
                                                        "id", str
                                                )
                                                .execute(new TbsClient.Callback() {
                                                    @Override
                                                    public void onFinished(TbsClient.ServerResponse response) {
                                                        try {
                                                            String content = new String(response.getContent(), "UTF-8");
                                                            Log.i("yy", response.getStatusCode() + "\n" + content);

                                                        } catch (UnsupportedEncodingException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });

                                        List<User> list = new ArrayList<User>();
                                        User user = new User(mImageIds[1][1], str, getStringArray(mDetailIds[1])[1]);
                                        list.add(user);

                                        dbHelper.open();
                                        ContentValues values = new ContentValues(); // 相当于map
                                        values.put("id", str);

                                        dbHelper.insert("friend_list", values);
                                        dbHelper.closeConnection();


                                        mData.add(list);


                                    }


                                })
                        .setNegativeButton("取消", null)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


}