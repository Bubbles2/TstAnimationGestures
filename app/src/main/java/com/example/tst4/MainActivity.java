package com.example.tst4;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	//
	private GestureDetector gestureDetector;
	public static String DEBUG_TAG = "DF My Debug Main";

	//
	Rect imageRect = new Rect();
	Rect globalRect = new Rect();
	Point globalOffset = new Point();
	int lastBottom;
	//
	float hide=0;
	boolean down;
	boolean up;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//
		//============================================
		// Gesture Detector 
		//============================================
		   gestureDetector = new GestureDetector( new SwipeGestureDetector());
		// We want to put a movement just on the top linear layout
		findViewById(R.id.topLL).setOnTouchListener(new MyTouchListener());
	}
	
	public void myAnimate(View v) {
		// Get reference to image and its container in this case the relative layout
		ImageView image = (ImageView)findViewById(R.id.imageView1);
		View container = findViewById(R.id.container);
		//
		
		// This will return the view Rect in which the view is drawn
		// this is a weird method it returns a boolean but it set Rect 
		// values on on-bjects passed
		//
		image.getGlobalVisibleRect(imageRect);
		container.getGlobalVisibleRect(globalRect,globalOffset);
		imageRect.offset(-globalOffset.x, -globalOffset.y);
		//
		Animation translate
		  = new TranslateAnimation(imageRect.left,
		                           imageRect.left+2*imageRect.width(),
		                           imageRect.top,
		                           imageRect.top);
		translate.setInterpolator(new AnticipateInterpolator());
		//
		Animation scale
		  = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
		                       Animation.RELATIVE_TO_SELF, 0.5f,
		                       Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setInterpolator(new AccelerateInterpolator());
		Animation alpha = new AlphaAnimation(1.0f, 0.0f);
		alpha.setInterpolator(new LinearInterpolator());
		//
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(translate);
		set.addAnimation(scale);
		set.addAnimation(alpha);
		//
		image.setVisibility(View.INVISIBLE);

		ImageView animatedImage = (ImageView) findViewById(R.id.imageView1);
		animatedImage.setImageDrawable(image.getDrawable());
		animatedImage.setVisibility(View.VISIBLE);

		set.setFillAfter(true);
		set.setDuration(800);
		set.start();
		animatedImage.setAnimation(set);

		
	}
	
	public void moveImg(View v) {
		//
		ImageView image = (ImageView)findViewById(R.id.imageView1);
		image.setX(200);
		image.setY(200);
		
	}
	
	public void myDrop(View v) {
		//
		LinearLayout tll = (LinearLayout) findViewById(R.id.topLL);
		tll.getGlobalVisibleRect(imageRect);
		//
		Log.v("Animation  Bottom  -->",""+imageRect.bottom);
		//
		hide =  (imageRect.bottom/3)*-1;
		if(tll.getY()==0)
		{tll.setY((imageRect.bottom/3)*-1);}
		else {tll.setY(0);}				
		}
	public void myDrop() {
		//
		LinearLayout tll = (LinearLayout) findViewById(R.id.topLL);
		tll.getGlobalVisibleRect(imageRect);
		//
		Log.v("Animation  Bottom  -->",""+imageRect.bottom);
		//
		hide =  (imageRect.bottom/3)*-1;
		if(tll.getY()==0)
		{tll.setY((imageRect.bottom/3)*-1);}
		else {tll.setY(0);}				
		}
	
	
	  //============================================
	  // Gesture Detector Methods
	  //============================================
	  @Override
	  public boolean onTouchEvent(MotionEvent event) {

		  if (gestureDetector.onTouchEvent(event)) {
	      return true;
	    }
	    return super.onTouchEvent(event);
	  }

	  private void onLeftSwipe() {
	    // Do something
	  }
	  private void onDownSwipe() {
		    // Do something
	    	Log.i(DEBUG_TAG,"==== Down or Up ===== "); 
		  }


	  private void onRightSwipe() {
	    // Do something
	  }

	  // Private class for gestures
	  private class SwipeGestureDetector 
	          extends SimpleOnGestureListener {
	    // Swipe properties, you can change it to make the swipe 
	    // longer or shorter and speed
	    private static final int SWIPE_MIN_DISTANCE = 120;
	    private static final int SWIPE_MAX_OFF_PATH = 200;
	    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2,
	                         float velocityX, float velocityY) {
	      try {
	        float diffAbs = Math.abs(e1.getY() - e2.getY());
	        float diff = e1.getX() - e2.getX();
	        float diffY = e2.getY() - e1.getY();
	        
	        // Test if movement is up or down add a minimum movement of 20
	        down = false;
	        up = false;	        
	        if(e1.getY() < e2.getY() ){
	        	if(diffAbs>20)
	        		{
	        		down=true;
	        		MainActivity.this.myDrop();	        		
	        		}
	        }
	        if(e1.getY() > e2.getY() ){
	        	if(diffAbs>20)up=true;
	        }
	        

	        if (diffAbs > SWIPE_MAX_OFF_PATH)	        
	         return false;
	        //
	        if (diffAbs > 0){
	        	MainActivity.this.onDownSwipe();
	        }
	        
	        // Left swipe
	        if (diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	MainActivity.this.onLeftSwipe();

	        // Right swipe
	        } else if (-diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	MainActivity.this.onRightSwipe();
	        }
	      } catch (Exception e) {
	        Log.e("YourActivity", "Error on gestures");
	      }
	      return false;
	    }
	  }
	  private final class MyTouchListener implements OnTouchListener {
			 
			 
		    public boolean onTouch(View view, MotionEvent motionEvent) {
		    	
		      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
		        ClipData data = ClipData.newPlainText("", "");
		        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		        view.startDrag(data, shadowBuilder, view, 0);
		        view.setVisibility(View.INVISIBLE);
		        //shadowBuilder.
		            return true;
		      } else {
		        return false;
		      }
		    }
		  }
		
}
