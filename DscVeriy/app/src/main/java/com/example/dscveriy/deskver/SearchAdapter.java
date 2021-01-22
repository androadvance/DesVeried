package com.example.dscveriy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<SearchList> {

    Context context;
    int resource, textViewResourceId;
    List<SearchList> items, tempItems, suggestions;


    public SearchAdapter(Context context, int resource, int textViewResourceId, List<SearchList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<SearchList>(items); // this makes the difference.
        suggestions = new ArrayList<SearchList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.searchlist_empno, parent, false);
        }
        SearchList searchList = items.get(position);
        if (searchList != null) {
            TextView lblName = (TextView) view.findViewById(R.id.tv_empno);
            if (lblName != null)
                lblName.setText(searchList.getEmpno());
        }
        return view;
    }

    @Override
    public Filter getFilter() {

        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((SearchList) resultValue).getEmpno();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SearchList searchList : tempItems) {
                    if (searchList.getEmpno().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(searchList);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<SearchList> filterList = (ArrayList<SearchList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SearchList searchList : filterList) {
                    add(searchList);
                    notifyDataSetChanged();
                }
            }
        }
    };
}