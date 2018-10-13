package com.example.pandia.luxury.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandia.luxury.R;
import com.example.pandia.luxury.configs.Config;
import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.interfaces.ILuxuryListModel;
import com.example.pandia.luxury.interfaces.ILuxuryListPresenter;
import com.example.pandia.luxury.interfaces.ILuxuryListView;
import com.example.pandia.luxury.models.LuxuryListModel;
import com.example.pandia.luxury.presenters.LuxuryListPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LuxuryListViewActivity extends AppCompatActivity implements ILuxuryListView {

    private ListView mListView;
    private TextView mShowText;
    private Button mButton;
    private SearchView mSearchView;
    private LuxuryItemAdapter mListItemAdapter;

    //TODO: Where to put lifecycle ?
    private ILuxuryListModel mListModel;
    private ILuxuryListPresenter mListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luxury_list_view);

        mListPresenter = new LuxuryListPresenter();
        mListModel = LuxuryListModel.createLuxuryListModel(getApplicationContext(), Config.DEFAULT_DATA_IO, mListPresenter);
        mListPresenter.setView(this);

        mListView = findViewById(R.id.LuxuryListViewActivity_ListView);
        mListItemAdapter = new LuxuryItemAdapter(this);
        mListView.setAdapter(mListItemAdapter);
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(LuxuryListViewActivity.this,
                "You Choose " + position + " listItem", Toast.LENGTH_SHORT).show());

        mSearchView = findViewById(R.id.LuxuryListViewActivity_SearchView);
        mSearchView.setFocusable(false);
        setSearchFunction();

        //Todo: Remove test code
        mListPresenter.onListScrolledUp();
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

    @Override
    public void updateListViewItems(ArrayList<LuxuryItem> luxuryItems) {
        mListItemAdapter.setDisplayItems(luxuryItems);
        mListItemAdapter.notifyDataSetChanged();
    }

    public class LuxuryItemAdapter extends BaseAdapter implements Filterable {
        private LayoutInflater mInflater;
        private ArrayList<LuxuryItem> mOriginalItems;
        private ArrayList<LuxuryItem> mItems;

        public LuxuryItemAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mItems = new ArrayList<LuxuryItem>();
        }

        public void setDisplayItems(ArrayList<LuxuryItem> items) {
            if (items != null) {
                Log.i("pandia", "DisplayItem size :" + items.size());
                mItems = items;
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mItems.get(position);
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

            itemImg.setImageBitmap(mItems.get(position).getItemImage());
            itemName.setText(mItems.get(position).getItemName());
            itemSubname.setText(LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME.get(mItems.get(position).getItemType()));

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    constraint = constraint.toString();
                    FilterResults result = new FilterResults();
                    if (mOriginalItems == null) {
                        synchronized (this){
                            mOriginalItems = new ArrayList<LuxuryItem>(mItems);
                        }
                    }
                    if (constraint != null && constraint.toString().length() > 0) {
                        ArrayList<LuxuryItem> filteredItem = new ArrayList<LuxuryItem>();
                        for (int i = 0; i < mOriginalItems.size(); ++i) {
                            String title = mOriginalItems.get(i).getItemName();
                            String typeName = LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME.get(mOriginalItems.get(i).getItemType());
                            if (title.contains(constraint) || typeName.contains(constraint)) {
                                filteredItem.add(mOriginalItems.get(i));
                            }
                        }
                        result.count = filteredItem.size();
                        result.values = filteredItem;
                    }
                    else {
                        synchronized (this) {
                            ArrayList<LuxuryItem> list = new ArrayList<LuxuryItem>(mOriginalItems);
                            result.values = list;
                            result.count = list.size();
                        }
                    }

                    return result;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mItems = (ArrayList<LuxuryItem>)results.values;
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
