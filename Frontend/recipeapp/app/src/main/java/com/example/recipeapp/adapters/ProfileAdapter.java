package com.example.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.recipeapp.objects.ProfileItemObject;
import com.example.recipeapp.R;

import java.util.List;
/**
 * Adapter class for a list of profiles
 *
 * @author Ryan McFadden
 */
public class ProfileAdapter extends ArrayAdapter<ProfileItemObject> {
    /**
     * Constructor for ProfileAdapter
     * @param context application's current context
     * @param items List of ProfileItemObjects
     */
    public ProfileAdapter(Context context, List<ProfileItemObject> items) {
        super(context, 0, items);
    }

    /**
     * Method to get update and return the updated View
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProfileItemObject item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_item, parent, false);
        }

        // Lookup view for data population
        TextView itemUsername = convertView.findViewById(R.id.itemUsername);


        // Populate the data into the template view using the data object
        itemUsername.setText(item.getUsername());

        // Return the completed view to render on screen
        return convertView;
    }

//    @Override
//    public ListItemObject getItem(int position){
//        return items.get(position);
//    }
}


