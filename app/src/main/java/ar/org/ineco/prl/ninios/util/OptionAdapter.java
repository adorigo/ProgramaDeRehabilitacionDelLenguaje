package ar.org.ineco.prl.ninios.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ar.org.ineco.prl.ninios.R;
import ar.org.ineco.prl.ninios.classes.ApplicationContext;
import ar.org.ineco.prl.ninios.classes.Option;
import ar.org.ineco.prl.ninios.classes.SoundFile;

public class OptionAdapter extends BaseAdapter {

    private ArrayList<Option> _items;
    private Context _context;
    private LayoutInflater inflaInflater;

    public OptionAdapter(Context context, ArrayList<Option> images) {
        super();
        _context = context;
        _items = images;
        inflaInflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean hasStableIds () {

        return true;
    }

    @Override
    public int getCount () {

        return _items.size();
    }

    @Override
    public Object getItem (int position) {

        return _items.get(position);
    }

    @Override
    public long getItemId (int position) {

        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        Option option = (Option) getItem(position);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.item_diccionario, null);
        }

        convertView.setTag(option);

        AsyncLoadSound task = new AsyncLoadSound();
        task.doInBackground(new Object[] {option.getSnd()});

        ImageView img = (ImageView) convertView.findViewById(R.id.itemImage);
        img.setImageResource(_context.getResources().getIdentifier(option.getImg().getName(), "drawable", _context.getPackageName()));
        img.setAdjustViewBounds(true);


        return convertView;
    }

    private class AsyncLoadSound extends AsyncTask {

        @Override
        protected Object doInBackground (Object[] params) {

            int sndId = -1;

            try {

                sndId = _context.getResources().getIdentifier(((SoundFile) params[0]).getName(), "raw", _context.getPackageName());
                ApplicationContext.getSndUtil().loadSound(sndId);

            } catch (RuntimeException rE) {
                Log.d(OptionAdapter.class.getName(), rE.getMessage());
            }

            return sndId;
        }
    }
}
