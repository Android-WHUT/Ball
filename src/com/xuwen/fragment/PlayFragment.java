package com.xuwen.fragment;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.xuwen.ball.R;
import com.xuwen.base.Ball;
import com.xuwen.base.Bar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class PlayFragment extends Fragment{
	//����С��ľ���С��20ʱ,��Ϊ��������
		//private final int  CRASH_BALL = 20;
		//������߽�ľ���С�ڵ���10ʱ��Ϊ��ײ
		//����ֵ���Ը��ģ�����ͨ����ȡͼƬ�߿���
		private final int CRASH_BORDER;
		//С����ƶ��ٶ�,��X���Y���ϵ��ƶ������Ϊ��ô��
		private int v;
		//����С���ʱ����
		private int newballtime;
		//�ƶ�С���ʱ����
		private int move_timeGap;
		
		//������¼�����˶���С�򣬷������
		private ArrayList<Ball> ballNum = new ArrayList<Ball>();
		//��ʾС��Ļ�����surfaceview
		private SurfaceView game_interface;
		private SurfaceHolder sfh;
		private TextView game_control;
		
		private Timer ball_produce;
		private Factory task;
		//�������С��ļ���
		private int count = 0;
		
		//���캯���Ĳ���
		private Activity activity;
		//��Ļ�Ŀ�Ⱥ͸߶�
		private int w,h;
		//���ظ�Activity��view
		private View view;
		//����һ��Bar����
		private Bar bar;
		private boolean isGameing = false;
		private boolean isStop = true;
		private boolean isDoubleClick = false;
		private Animation mAlphaAnimation;    
		//��Ϸ��������
		private int second = -1;
				
		public PlayFragment(Activity activity ,int w,int h){
			super();
			CRASH_BORDER = new Ball(activity).getBallWidth();
			this.activity = activity;
			this.w = w;
			this.h = h;
		}
		
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			init();
			
			game_interface.setOnTouchListener(touch);
			game_control.setOnTouchListener(l);
		}
		public View onCreateView(LayoutInflater inflater,ViewGroup container,
				Bundle savedInstanceState){
				view.setLayoutParams(new LayoutParams(w,h));
				
			return view;
		}
		
		private void init(){
			
			view = LayoutInflater.from(activity).inflate(R.layout.playfragment, null);
			
			game_interface = (SurfaceView)view.findViewById(R.id.geme_canvas);
			sfh = game_interface.getHolder();
			game_control = (TextView) view.findViewById(R.id.game_control);
			
			//���ñ���͸�� 	
			game_interface.setZOrderOnTop(true);
			sfh.setFormat(PixelFormat.TRANSLUCENT);
			Random r = new Random();
			v = r.nextInt(5)+5;
			newballtime = 0;//��ʾ1��
			move_timeGap = 10;
			//ע��ʱ����ٶȵĴ��䣬�����׳��ֶ���Ч��������˸
			//��������˸Ч������Ҫע�⡣����ϣ�������и��õķ�����������
			
			//ʵ����bar����
			bar = new Bar(activity, w/2-100, h/2);
			
			ball_produce = new Timer();
			task = new Factory();
						
		}
		private void changeDireX(Ball b){
			//��������ұ���Ļ����                                                                               -1�ڴ˴�ֻ�Ǳ�ʾ���򣬲�����
	          if(w-b.getCurrX() <= CRASH_BORDER) b.setAbsDireX(-1);
	          //�������������Ļ�ҷ���Ϊ��������
	          if(b.getCurrX() <= 0 && b.getDireX() == -1) b.setAbsDireX(1);
		}
		private void changeDireY(Ball b){
			if(Math.abs(h-b.getCurrY()) <= CRASH_BORDER) b.setAbsDireY(-1);
			if(b.getCurrY() <= 0 ) b.setAbsDireY(1);
			
		}
		private void mPaint(ArrayList<Ball> ball){


			for(int i = 0; i < ball.size()-1; i++){
				//����ΪС��ķ���Ч��
				//ball.get(i).setOffest(v);
				if(!ball.get(i).getIsCrashed()){
				for(int j = i; j <ball.size(); j++ ){
					if(j+1 <ball.size()){
						if(Math.abs(ball.get(i).getCurrY() - ball.get(j+1).getCurrY()) <= CRASH_BORDER
								&&Math.abs(ball.get(i).getCurrX() - ball.get(j+1).getCurrX()) <= CRASH_BORDER
								&& (!ball.get(j+1).getIsCrashed())){
							
							
                            //X��ͬYͬ
	                        if(ball.get(i).getDireX() != ball.get(j+1).getDireX() && 
	                        		ball.get(i).getDireY() == ball.get(j+1).getDireY()){
	                        	ball.get(i).setdireX(-1);
							    ball.get(j+1).setdireX(-1);
							    ball.get(i).setIsCrashed(true);
							    ball.get(j+1).setIsCrashed(true);
	                        }
	                        else if(ball.get(i).getDireX() == ball.get(j+1).getDireX()
	                        		&&ball.get(i).getDireY() != ball.get(j+1).getDireY()){ 
								    ball.get(i).setdireY(-1);
								    ball.get(j+1).setdireY(-1);
								    ball.get(i).setIsCrashed(true);
								    ball.get(j+1).setIsCrashed(true);
		                        }
	                        else{
	                        	    ball.get(i).setdireY(-1);
	                        	    ball.get(i).setdireX(-1);
								    ball.get(j+1).setdireY(-1);
								    ball.get(i).setIsCrashed(true);
								    ball.get(j+1).setIsCrashed(true);
	                        }
	                        ball.get(i).setOffest(v);
	                        ball.get(j+1).setOffest(v);
						}
					}
				}
			    }
				//�ж�ÿһ��С���bar֮��ľ����Լ�����
				//��ʱֻ��·bar������bar���������ײ
				//С����bar������
				
				//���ж�С��ķ���Ч�����ڿ���С��ͱ߽����ײ
				//changeDireX(ball.get(i));changeDireY(ball.get(i));

			}

			Paint p = new Paint();
			Paint p1 = new Paint();
			Typeface font = Typeface.create("����", Typeface.BOLD);
			p1.setColor(Color.WHITE);
			p1.setTypeface(font);
			p1.setTextSize(100);
			
			Canvas canvas = sfh.lockCanvas();
			if(canvas != null){
			canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);

			for(Ball b : ball){

				if(b.getCurrX() <= (bar.getCurrX()+bar.getBarWidth())
						&& b.getCurrX() >= bar.getCurrX()-b.getBallWidth()){
				  
					if(bar.getCurrY() - b.getCurrY() <= CRASH_BORDER
							&&bar.getCurrY() - b.getCurrY() > 0 ){
						b.setAbsDireY(-1);
					}
					else if(b.getCurrY() - bar.getCurrY() <= bar.getBarHeight()
							&&b.getCurrY() - bar.getCurrY()>0){
						b.setAbsDireY(1);
					}
				}
				changeDireX(b);changeDireY(b);
				b.setOffest(v);
				int currX = b.getCurrX();
				int currY = b.getCurrY();
				canvas.drawText(second+":"+(100-count),w/2-120, h/7, p1);
				
				canvas.drawBitmap(b.getBall(), currX, currY, p);
			    canvas.drawBitmap(bar.getBar(), bar.getCurrX(), bar.getCurrY(),p);				
				
				gameOver(b);
			}
			//����bar��λ��
			
			sfh.unlockCanvasAndPost(canvas);
			}
			if(!isGameing){ 
				ball_produce.cancel();
				ballNum.clear();
				
				Restart sf = new Restart(this.getActivity(),w,h); //��bundle����fragment֮��Ĳ������� 
				Bundle bundle = new Bundle();
                bundle.putString("second",second+"" );//second����Ϊint,�����Ϊstring  
                bundle.putString("count",count+"" );
                sf.setArguments(bundle);  
                

				//��������ת
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
	            transaction.replace(R.id.container, sf);
	            transaction.commit();
				
				return;
				
			}
		}
		
		
		
		private void gameOver(Ball b){
			if(b.getCurrY() > h-w*3/10){
				//ball_produce.cancel();
				//ballNum.clear();
				isGameing = false;
			}
		}
		private void gamePause(){
               
			   if(isStop){
				   isStop = false;
				   isDoubleClick = false;
				   game_control.setVisibility(View.GONE);
				   game_control.setText("Tap to Continue");
			   }
			   else{
				   isStop = true;
				   isDoubleClick = false;
				   game_control.setVisibility(View.VISIBLE);
			   }
			   if(!isGameing){
				   isGameing = true;
				   //������ʼ����
				   ball_produce.schedule(task, newballtime, move_timeGap);
			   }

		}
		private void stopBy2Click() {  
		    Timer tStop = null;  
		    if (isDoubleClick == false) {  
		        isDoubleClick = true;
		        tStop = new Timer();  
		        tStop.schedule(new TimerTask() {  
		            @Override  
		            public void run() {  
		                isDoubleClick = false;
		            }  
		        }, 200
		        ); // ���2������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����  
		  
		    } else {  
		    	gamePause();
		    	
		    }  
		}
		
private View.OnTouchListener touch = new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				
				 int x=(int)event.getX();
			     int y=(int)event.getY();
			     //����bar��λ��
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(isGameing) stopBy2Click();
						break;

					}
					if(y -120 > 3*h/4)
				    	 bar.setLocation(x-75, 3*h/4);
				    else bar.setLocation(x-75, y-120);
			
	
				return true;
			}
		};

		private View.OnTouchListener l = new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					gamePause();
					
					//break;
				}
				return false;
				
			}
		};
			
		
		class Factory extends TimerTask {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(count == 0){
					
					//�ô˹��캯��ʱ���ǵ����������С������������������쳣���ԡ�������ȼ�һЩ
					Ball b = new Ball(activity,w - CRASH_BORDER);
					ballNum.add(b);
					count = 100;
					second++;
				}
				
				if(ballNum.size() > 0){
					if(!isStop)
					  mPaint(ballNum);
				}
				if(!isStop) count--;
			}

		}
		
}
