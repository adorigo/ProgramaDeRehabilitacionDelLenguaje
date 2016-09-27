package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import java.util.List;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.R;

public class PrefsHeaderAdapter extends ArrayAdapter<Header> {

    public static final int HEADER_TYPE_CATEGORY = 0;
    public static final int HEADER_TYPE_NORMAL = 1;

    private LayoutInflater mInflater;

    public PrefsHeaderAdapter(Context context, List<Header> objects) {
        super(context, 0, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PreferenceActivity.Header header = getItem(position);
        int headerType = getHeaderType(header);
        View view = null;

        switch (headerType) {
            case HEADER_TYPE_CATEGORY:
                view = mInflater.inflate(android.R.layout.preference_category, parent, false);
                ((TextView) view.findViewById(android.R.id.title)).setText(header.getTitle(getContext().getResources()));
                break;
            case HEADER_TYPE_NORMAL:
                view = mInflater.inflate(R.layout.preference_header_item, parent, false);
                ((ImageView) view.findViewById(R.id.icon)).setImageResource(header.iconRes);
                ((TextView) view.findViewById(R.id.title)).setText(header.getTitle(getContext().getResources()));
                ((TextView) view.findViewById(R.id.summary)).setText(header.getSummary(getContext().getResources()));
                break;
        }

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != HEADER_TYPE_CATEGORY;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static int getHeaderType(Header header) {
        if ((header.fragment == null) && (header.intent == null)) {
            return HEADER_TYPE_CATEGORY;
        } else {
            return HEADER_TYPE_NORMAL;
        }
    }
}
