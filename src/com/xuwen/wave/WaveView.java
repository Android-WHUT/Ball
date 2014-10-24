package com.xuwen.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
	
	private Paint p;
	
	public WaveView(Context context){
		super(context);
		init();
	}
	
	public WaveView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}

	public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		
		
		invalidate();
	}
	
	private void init(){
		p = new Paint();
		p.setColor(Color.BLUE);
	}

}
