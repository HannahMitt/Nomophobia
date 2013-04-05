package com.hannah.nomophobia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.hannah.nomophobia.R;

public class TimeMarkerView extends View {
	private Context context;
	private Paint paint;
	private int position;

	public TimeMarkerView(Context context, int position) {
		super(context);
		this.context = context;
		this.position = position;
		paint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		float density = context.getResources().getDisplayMetrics().density; 
		paint.setColor(Color.RED);
		paint.setStrokeWidth(1 * density);
		
		float drawPosition = position * 2 * density;
		canvas.drawLine(drawPosition, 0, drawPosition, context.getResources().getDimensionPixelSize(R.dimen.time_cell_height), paint);
	}

}
