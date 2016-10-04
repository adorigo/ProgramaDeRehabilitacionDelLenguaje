package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.R;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;

public class CategoryAdapter extends BaseExpandableListAdapter {

    private ArrayList<Category> _items;
    private Context _context;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        super();
        _context = context;
        _items = categories;
    }

    @Override
    public int getGroupCount() {
        return _items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _items.get(groupPosition).getLevels().size();
    }

    @Override
    public Category getGroup(int groupPosition) {
        return _items.get(groupPosition);
    }

    @Override
    public Level getChild(int groupPosition, int childPosition) {
        return _items.get(groupPosition).getLevels().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Category headerTitle = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_category, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.catName);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Level childText = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_category, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.catName);
        TextView pathListChild = (TextView) convertView.findViewById(R.id.catPath);
        txtListChild.setText(childText.getLvlName());
        pathListChild.setText(getGroup(groupPosition).getPath());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}