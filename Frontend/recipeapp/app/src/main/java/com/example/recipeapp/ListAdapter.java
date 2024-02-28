package com.example.recipeapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * @author Ryan McFadden
 */
public class ListAdapter extends ArrayAdapter<ListItemObject> {

    public ListAdapter(Context context, List<ListItemObject> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListItemObject item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        TextView itemTitle = convertView.findViewById(R.id.itemTitle);
        TextView itemAuthor = convertView.findViewById(R.id.itemAuthor);
        TextView itemDescription = convertView.findViewById(R.id.itemDescription);

        // Populate the data into the template view using the data object
        itemTitle.setText(item.getTitle());
        itemAuthor.setText(item.getAuthor());
        itemDescription.setText(item.getDescription());

        // Return the completed view to render on screen
        return convertView;
    }

//    @Override
//    public ListItemObject getItem(int position){
//        return items.get(position);
//    }
}


