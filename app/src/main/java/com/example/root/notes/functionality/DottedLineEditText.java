package com.example.root.notes.functionality;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: Add a class header comment!
 */

public class DottedLineEditText extends AppCompatEditText{

    private Paint dottedLinePaint;
    private Rect rectangle;
    private DashPathEffect dashPathEffect;

    public DottedLineEditText(Context context) {
        super(context);
        init();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public DottedLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public DottedLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void init()
    {
        rectangle = new Rect();

        dottedLinePaint = new Paint();
        dottedLinePaint.setARGB(100, 0, 0, 0);
        dottedLinePaint.setStyle(Paint.Style.STROKE);

        dashPathEffect = new DashPathEffect(new float[]{10, 5}, 0);
        dottedLinePaint.setPathEffect(dashPathEffect);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawLine(100, 100, 200, 100, dottedLinePaint);

        super.onDraw(canvas);
    }
}
