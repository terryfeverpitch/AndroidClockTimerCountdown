package com.ClockTimerCountdown;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class Clock extends View{
	// private ShapeDrawable mDrawable;
	public static final int MEG_INVALIDATE = 9527;
    private Paint mPaint;
    private Calendar calendar;
    private int hour, min, sec;
    private int year, month, date;
    private int hr_m_size = 60;
    private int date_size = 35;
    private int sec_size = 10;

	public Clock(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
        // TODO Auto-generated constructor stub
		mPaint = new Paint();

		calendar = Calendar.getInstance(); 
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		min = calendar.get(Calendar.MINUTE);
		sec = calendar.get(Calendar.SECOND);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		date = calendar.get(Calendar.DAY_OF_MONTH);	
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        // mDrawable.draw(canvas);
		calendar = Calendar.getInstance(); 
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		min = calendar.get(Calendar.MINUTE);
		sec = calendar.get(Calendar.SECOND);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		date = calendar.get(Calendar.DAY_OF_MONTH);
		
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        mPaint.setColor(0xAAFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        int w = this.getWidth();
        int h = this.getHeight();
        Point mid = new Point(w/2, h/2);
        canvas.drawCircle(mid.x, mid.y, 150, mPaint);
        
        mPaint.setTextSize(hr_m_size); mPaint.setTextAlign(Align.LEFT);
        canvas.drawText(fix(hour), mid.x - 80, mid.y - 20, mPaint);
        canvas.drawLine(mid.x, mid.y + 60, mid.x, mid.y - 60, mPaint);
        canvas.drawText(fix(min), mid.x + 70 - hr_m_size, mid.y - 20, mPaint);
        
        mPaint.setTextSize(date_size); mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("" + year, mid.x - 90, mid.y + 50, mPaint);
        canvas.drawText(fix(month) + "/" + fix(date), mid.x + 10, mid.y + 50, mPaint);
        
        mPaint.setTextSize(sec_size + 30);
        int xx = mid.x + (int)(150.0 * Math.cos((double)(sec - 15.0) * 6.0 * 3.14 / 180.0));
        int yy = mid.y + (int)(150.0 * Math.sin((double)(sec - 15.0) * 6.0 * 3.14 / 180.0));
   
        if(sec == 0 || sec == 30) {
        	mPaint.setTextAlign(Align.CENTER);
        }
        else if(sec < 30) {
        	mPaint.setTextAlign(Align.RIGHT);
        }
        else if(sec > 30){
        	mPaint.setTextAlign(Align.LEFT);
        }

        canvas.drawText(fix(sec), xx, yy, mPaint);
        canvas.drawPoint(xx, yy, mPaint);
        this.invalidate();
    }

	public String fix(int n) {
		if(n < 10) return "0" + n; 
		else return "" + n;
	}
}
