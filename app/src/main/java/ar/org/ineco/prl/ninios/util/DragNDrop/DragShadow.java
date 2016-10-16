package ar.org.ineco.prl.ninios.util.DragNDrop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

public class DragShadow extends View.DragShadowBuilder {

    ColorDrawable GreyBox;

    public DragShadow(View view) {
        super(view);
        GreyBox = new ColorDrawable(Color.LTGRAY);
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {

        View v = getView();

        int height = (int) v.getHeight();
        int width = (int) v.getWidth();

        GreyBox.setBounds(0, 0, width, height);
        shadowSize.set(width, height);
        shadowTouchPoint.set(width / 2, height / 2);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        super.onDrawShadow(canvas);
    }
}
