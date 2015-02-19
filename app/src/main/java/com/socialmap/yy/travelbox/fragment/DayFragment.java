package com.socialmap.yy.travelbox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.model.DailyTravelSchedule;
import com.socialmap.yy.travelbox.model.TravelSchedule;

import java.text.SimpleDateFormat;

public  class DayFragment extends Fragment {
        private DailyTravelSchedule day;
        private static LayoutInflater inflater;
        private GestureDetector gestureDetector;

        public DayFragment(Context context) {
            super();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setDay(DailyTravelSchedule d) {
            day = d;
        }

        public void setGestureDetector(GestureDetector gestureDetector) {
            this.gestureDetector = gestureDetector;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_schedule_fragment, null);
            ListView list = (ListView) root.findViewById(R.id.tb_schedule_list);

            list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });
            assert day != null;
            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return day.getSchedules().size();
                }

                @Override
                public Object getItem(int i) {
                    return day.getSchedules().get(i);
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = inflater.inflate(R.layout.activity_schedule_list_item, null);
                        TextView content = (TextView) convertView.findViewById(R.id.content);
                        TextView start = (TextView) convertView.findViewById(R.id.start);
                        TextView end = (TextView) convertView.findViewById(R.id.end);
                        TravelSchedule t = (TravelSchedule) getItem(position);
                        content.setText(t.getContent());
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        start.setText(df.format(t.getStart()));
                        end.setText(df.format(t.getEnd()));
                    }
                    return convertView;
                }
            });
            return root;
        }
    }

