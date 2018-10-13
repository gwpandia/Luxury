package com.example.pandia.luxury;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LuxuryListViewActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView mShowText;
    private Button mButton;
    private SearchView mSearchView;
    LuxuryItemAdapter mListItemAdapter;

    String[] names = new String[] { "今天", "明天", "後天" };
    String[] lists = new String[] { "Today", "Tomorrow", "Acuired" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luxury_list_view);

        mListView = findViewById(R.id.LuxuryListViewActivity_ListView);
        mListItemAdapter = new LuxuryItemAdapter(this, Arrays.asList(names));
        mListView.setAdapter(mListItemAdapter);
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(LuxuryListViewActivity.this,
                "You Choose " + position + " listItem", Toast.LENGTH_SHORT).show());

        mSearchView = findViewById(R.id.LuxuryListViewActivity_SearchView);
        mSearchView.setFocusable(false);
        setSearchFunction();

    }

    private void setSearchFunction() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mListItemAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    public class LuxuryItemAdapter extends BaseAdapter implements Filterable {
        private LayoutInflater mInflater;
        List<String> mOriginalItem;
        List<String> mItem;

        public LuxuryItemAdapter(Context context, List<String> item) {
            mInflater = LayoutInflater.from(context);
            mItem = item;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mItem.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = mInflater.inflate(R.layout.luxury_item, null);

            ImageView itemImg = (ImageView) convertView.findViewById(R.id.Luxury_Item_Image);
            TextView itemName = (TextView) convertView.findViewById(R.id.Luxury_Item_Name);
            TextView itemSubname = (TextView) convertView.findViewById(R.id.Luxury_Item_SubName);

            //itemImg.setImageResource(logos[position]);
            itemName.setText(mItem.get(position));
            //itemSubname.setText(lists[]);

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    constraint = constraint.toString();
                    FilterResults result = new FilterResults();
                    if (mOriginalItem == null) {
                        synchronized (this){
                            mOriginalItem = new ArrayList<String>(Arrays.asList(names));
                        }
                    }
                    if (constraint != null && constraint.toString().length() > 0) {
                        ArrayList<String> filteredItem = new ArrayList<String>();
                        for (int i = 0; i < mOriginalItem.size(); ++i) {
                            String title = mOriginalItem.get(i);
                            if (title.contains(constraint)) {
                                filteredItem.add(title);
                            }
                        }
                        result.count = filteredItem.size();
                        result.values = filteredItem;
                    }
                    else {
                        synchronized (this) {
                            ArrayList<String> list = new ArrayList<String>(mOriginalItem);
                            result.values = list;
                            result.count = list.size();

                        }
                    }

                    return result;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mItem = (ArrayList<String>)results.values;
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return filter;
        }
    }
}
