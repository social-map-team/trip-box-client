
/**
 * Created by gxyzw_000 on 2014/11/29.
 */
package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.socialmap.yy.travelbox.adpater.ExpandAdapter;

import com.socialmap.yy.travelbox.model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends Activity implements OnChildClickListener {

    private ExpandableListView mListView = null;
    private ExpandAdapter mAdapter = null;
    private List<List<User>> mData = new ArrayList<List<User>>();
   private String[] function = new String[]{"聊天", "位置分享","个人资料"};
    private int[] mGroupArrays = new int[] {
            R.array.spongebob,
            R.array.patrick,
            R.array.sandy };

    private int[] mDetailIds = new int[] {
            R.array.spongebob_detail,
            R.array.patrick_detail,
            R.array.sandy_detail };

    private int[][] mImageIds = new int[][] {
            { R.drawable.img_00,
                    R.drawable.img_01,
                    R.drawable.img_02 },
            { R.drawable.img_03,
                    R.drawable.img_04,
                    R.drawable.img_05,
                    R.drawable.img_06,
                    R.drawable.img_07,
                    R.drawable.img_08,
                    R.drawable.img_09 },
            { R.drawable.img_10,
                    R.drawable.img_11 } };

    /** Called when the activity is first created. */
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
               public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setClass(FriendsActivity.this, FriendsInfoActivity.class);
                        startActivity(intent);



/*
        User user = mAdapter.getChild(groupPosition, childPosition);
        new AlertDialog.Builder(this)
                .setTitle(User.getName())
                .setMessage(User.getDetail())
                .setIcon(android.R.drawable.ic_menu_more)
                .setItems(function, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)

                    {
                        if (which == 0) {
                            Intent intent = new Intent();
                            intent.setClass(FriendsActivity.this, FriendsInfoActivity.class);
                            startActivity(intent);
                        } else if (which == 1) {
                            Toast.makeText(getApplicationContext(), "对其位置分享", Toast.LENGTH_SHORT) .show();
                        }
                        else if(which == 2){
                            Intent intent = new Intent();
                            intent.setClass(FriendsActivity.this, FriendsInfoActivity.class);
                            startActivity(intent);
                        }


                    }
                })
                    //TODO 缺少跳转事件


                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                            }
                        }
                )
                    .create()
                    .show();
*/

                    return true;
                }

        private void initData() {
                for (int i = 0; i < mGroupArrays.length; i++) {
                    List<User> list = new ArrayList<User>();
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

        }