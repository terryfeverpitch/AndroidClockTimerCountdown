package com.ClockTimerCountdown;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

public class ClockTimerCountdownActivity extends Activity {
    /** Called when the activity is first created. */
	private Dialog dialog;
	// Timer
	private Handler handler = new Handler();
	private int state = 0;
	private Button timer_btn_start, timer_btn_stop;
	private int s = 0, m = 0, h = 0;
	// Countdown
	private Handler handler3 = new Handler();
	private EditText countdown_et_msg;
	private Button countdown_btn_restart, countdown_btn_start;
	private TextView countdown_tv_h, countdown_tv_m, countdown_tv_s;
	private int countdown_h = 0, countdown_m = 0, countdown_s = 0;
	private int[] numArray;
	private int flag = 0;
	private int[] save = new int[3];
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        numArray = new int[100];
        for(int i = 0; i <= 99 ; i++)
        	numArray[i] = i;
        
        TextView tx;
        tx = (TextView)findViewById(R.id.timer_h);
        tx.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/triple_dot_digital.ttf"));
        tx = (TextView)findViewById(R.id.timer_m);
        tx.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/triple_dot_digital.ttf"));
        tx = (TextView)findViewById(R.id.timer_s);
        tx.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/triple_dot_digital.ttf"));
        
        tx = (TextView)findViewById(R.id.countdown_h);
        tx.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/triple_dot_digital.ttf"));
        tx = (TextView)findViewById(R.id.countdown_m);
        tx.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/triple_dot_digital.ttf"));
        tx = (TextView)findViewById(R.id.countdown_s);
        tx.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/triple_dot_digital.ttf"));
        
        
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Msg : ");
        
        final TextView msg = (TextView) dialog.findViewById(R.id.dialog_tv_msg);
        msg.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Ubuntu.ttf"));
        Button btn = (Button) dialog.findViewById(R.id.dialog_btn_ok);
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
        	
        });
        
        countdown_et_msg = (EditText) findViewById(R.id.countdown_et_msg);
        countdown_et_msg.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				msg.setText(countdown_et_msg.getText());
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        	
        });
        
        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        
        TabHost.TabSpec spec;
        
        //Dialog d = new Dialog(this);
        //LayoutInflater.from(this).inflate(R.layout.clock, null);

        //int id = view.findViewById(R.id.clock).getId();
        spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.clock);
        spec.setIndicator("Clock");
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("tag2");
        spec.setContent(R.id.timer);
        spec.setIndicator("Timer");
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("tag3");
        spec.setContent(R.id.countdown);
        spec.setIndicator("Countdown");
        tabs.addTab(spec);
        
        timer_btn_start = (Button) findViewById(R.id.timer_btn_start);
        timer_btn_stop  = (Button) findViewById(R.id.timer_btn_stop);    
        timer_btn_start.setOnClickListener(startTimer);
        timer_btn_stop.setOnClickListener(stopTimer);
        
        countdown_btn_restart = (Button) findViewById(R.id.countdown_btn_restart);
        countdown_btn_start   = (Button) findViewById(R.id.countdown_btn_start);
        
        countdown_btn_restart.setEnabled(false);
        countdown_btn_restart.setOnClickListener(restartCountdown);
        countdown_btn_start.setOnClickListener(startCountdown);
        
        countdown_tv_h = (TextView) findViewById(R.id.countdown_h);
        countdown_tv_m = (TextView) findViewById(R.id.countdown_m);
        countdown_tv_s = (TextView) findViewById(R.id.countdown_s);
        
        countdown_tv_h.setOnTouchListener(changeTimes);
        countdown_tv_m.setOnTouchListener(changeTimes);
        countdown_tv_s.setOnTouchListener(changeTimes);
    }
    
    private String fix(int n) {
		if(n < 10)
			return "0" + n;
		else
			return "" + n;
    }
    
    // timer
    private Runnable updateTimer = new Runnable() {
    	public void run() {
    		handler.postDelayed(this, 1000);
    		if(state == 2) return;
    		
    		TextView timer_s = (TextView) findViewById(R.id.timer_s);
    		TextView timer_m = (TextView) findViewById(R.id.timer_m);
    		TextView timer_h = (TextView) findViewById(R.id.timer_h);
    		
    		s++;
    		
    		if(s >= 60) {
    			m++;
    			s = 0;
    		}
    		if(m >= 60) {
    			h++;
    			m = 0;
    		}
    		
    	    timer_s.setText(fix(s) + "s");
    	    timer_m.setText(fix(m) + "m");
    	    timer_h.setText(fix(h) + "h");
    	}
    };
    
    View.OnClickListener startTimer = new View.OnClickListener() {
        public void onClick(View v) {
        	if(state == 0) {
        		state = 1;
        		timer_btn_start.setText("start");
	        	// startTime = System.currentTimeMillis();
	        	handler.removeCallbacks(updateTimer);
	        	handler.postDelayed(updateTimer, 1000);
        	}
        	else if(state == 1) {
        		return;
        	}
        	else if(state == 2) {
        		state = 0;
        		timer_btn_start.setText("start");
        		timer_btn_stop.setText("stop");
        		TextView timer_s = (TextView) findViewById(R.id.timer_s);
        		TextView timer_m = (TextView) findViewById(R.id.timer_m);
        		TextView timer_h = (TextView) findViewById(R.id.timer_h);
        		s = 0; m = 0; h = 0;
        		timer_s.setText(fix(s) + "s");
        	    timer_m.setText(fix(m) + "m");
        	    timer_h.setText(fix(h) + "h");
	        	handler.removeCallbacks(updateTimer);
        	}
        }
    };
    
    View.OnClickListener stopTimer = new View.OnClickListener() {
        public void onClick(View v) {
        	if(state == 1) {
        		state = 2;
        		timer_btn_start.setText("restart");
        		timer_btn_stop.setText("continue");
        	}
        	else if(state == 2) {
        		state = 1;
        		timer_btn_start.setText("start");
        		timer_btn_stop.setText("stop");
        	}
        }
    };
    //countdown
    View.OnTouchListener changeTimes = new View.OnTouchListener() {
    	private int pressed = 0;
        private int origin = 0;
        private int dist = 0;
        private int h = 0, m = 0, s = 0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(flag == 1) return false;
			
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				origin = (int) event.getY();
				switch(v.getId()) {
					case R.id.countdown_h:
						pressed = 1;
						break;
					case R.id.countdown_m:
						pressed = 2;
						break;
					case R.id.countdown_s:
						pressed = 3;
						break;
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_MOVE)
            {
                dist = origin - (int) event.getY();
                
                switch(pressed) {
                	case 1: //hour
                		countdown_h = h + dist / 15;
                		if(countdown_h < 0) {
                			countdown_h = 100 - numArray[(-countdown_h) % 100];  
                		}
                		else 
                			countdown_h = numArray[countdown_h % 100];  
                		countdown_tv_h.setText("" + fix(countdown_h) + "h");
                		break;
                	case 2: //min
                		countdown_m = m + dist / 15;
                		if(countdown_m < 0) {
                			countdown_m = 60 - numArray[(-countdown_m) % 60];  
                		}
                		else 
                			countdown_m = numArray[countdown_m % 60];                		
                		countdown_tv_m.setText("" + fix(countdown_m) + "m");
                		break;
                	case 3: //sec
                		countdown_s = s + dist / 15;
                		if(countdown_s < 0) {
                			countdown_s = 60 - numArray[(-countdown_s) % 60];  
                		}
                		else 
                			countdown_s = numArray[countdown_s % 60];  
                		countdown_tv_s.setText("" + fix(countdown_s) + "s");
                		break;
                	default:
                		return false;
                }
            }
			else if(event.getAction() == MotionEvent.ACTION_UP) {
				if(pressed == 1)
					h = countdown_h; 
				else if(pressed == 2)
					m = countdown_m; 
				else if(pressed == 3)
					s = countdown_s;
				
				pressed = 0;
		        origin = 0;
		        dist = 0;
			}
			return true;
		}
	};
	
	private Runnable updateCountdown = new Runnable() {
    	public void run() {
    		ClockTimerCountdownActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(flag != 1) return;
					handler3.postDelayed(this, 1000);

		    		countdown_s--;
		    		if(countdown_s < 0) {
		    			countdown_m--;
		    			if(countdown_m < 0) {
		    				countdown_h--;
		    				if(countdown_h < 0) {
		    					handler3.removeCallbacks(updateCountdown);
		    					flag = 0;
		    					countdown_h = save[0];
		    					countdown_m = save[1];
		    					countdown_s = save[2];
		    					countdown_tv_h.setText("" + fix(countdown_h) + "h");
		    					countdown_tv_m.setText("" + fix(countdown_m) + "m");
		    					countdown_tv_s.setText("" + fix(countdown_s) + "s");
		    					countdown_btn_start.setText("start");
		    					countdown_btn_restart.setEnabled(false);
		    	                dialog.show();
		    					return;
		    				}
		    				else countdown_tv_h.setText("" + fix(countdown_h) + "h");
		    				countdown_m = 59;
		        			countdown_tv_m.setText("" + countdown_m + "m");
		    			}
		    			else countdown_tv_m.setText("" + fix(countdown_m) + "m");
		    			
		    			countdown_s = 59;
		    			countdown_tv_s.setText("" + countdown_s + "s");
		    		}
		    		else countdown_tv_s.setText("" + fix(countdown_s) + "s");
				}
    			
    		});
    		
    	}
    };
    
    View.OnClickListener startCountdown = new View.OnClickListener() {
        public void onClick(View v) {
        	if(flag == 0) {
        		flag = 1;
        		countdown_btn_restart.setEnabled(true);
        		save[0] = countdown_h;
            	save[1] = countdown_m;
            	save[2] = countdown_s;
        		handler3.removeCallbacks(updateCountdown);
            	handler3.postDelayed(updateCountdown, 1000);
            	countdown_btn_start.setText("pause");
        	}
        	else if(flag == 1) {
        		flag = 2;
        		countdown_btn_start.setText("continue");
        	}
        	else if(flag == 2) {
        		flag = 1;
        		countdown_btn_start.setText("start");
        		handler3.removeCallbacks(updateCountdown);
        		handler3.postDelayed(updateCountdown, 1000);
        	}
        }
    };
    
    View.OnClickListener restartCountdown = new View.OnClickListener() {
        public void onClick(View v) {
        	handler3.removeCallbacks(updateCountdown);
        	flag = 0;
			countdown_h = save[0];
			countdown_m = save[1];
			countdown_s = save[2];
			countdown_tv_h.setText("" + fix(countdown_h) + "h");
			countdown_tv_m.setText("" + fix(countdown_m) + "m");
			countdown_tv_s.setText("" + fix(countdown_s) + "s");
			countdown_btn_start.setText("start");
			countdown_btn_restart.setEnabled(false);
        }
    };
}