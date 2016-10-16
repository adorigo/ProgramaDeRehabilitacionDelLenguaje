package ar.org.ineco.prl.ninios.util.DragNDrop;

import android.content.ClipData;
import android.view.View;

public class LongClickListener implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View v) {

        DragShadow dragShadow = new DragShadow(v);
        ClipData data = ClipData.newPlainText("", "");

        v.startDrag(data, dragShadow, v, 0);

        return false;
    }
}
