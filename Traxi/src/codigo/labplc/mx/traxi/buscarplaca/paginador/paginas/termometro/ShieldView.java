package codigo.labplc.mx.traxi.buscarplaca.paginador.paginas.termometro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.paginador.paginas.utlileria.Utils;
import codigo.labplc.mx.traxi.fonts.fonts;

public class ShieldView extends View {
    private int DefaultWidth = 120;
    private int imageSize;
    
    private int startProgress = 0;
    private int progress = 50;
    public static final int JUMP_PROGRESS_ANIMATION = 10;
    
    private int duration = 2000; //duration in millis
    
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    
    private int backgroundColor = Color.rgb(0xE0, 0xE0, 0xE0);
    private int progressColor = Color.rgb(0xEF, 0x41, 0x47);
    
    private Bitmap bitmap;
    Context context;
    
    /**
     * Constructor
     * 
     * @param context
     */
	public ShieldView(Context context)
	{

		super(context);
		ShieldView.this.context=context;
		
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public ShieldView(Context context, AttributeSet attrs)
	{
		
		super(context, attrs);
		ShieldView.this.context=context;
		
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public ShieldView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		
		ShieldView.this.context=context;
	}

	/**
	 * Initialize view parameters
	 * @param i 
	 * @param j 
	 */
	public void initUI(int i, int j)
	{
		LayoutParams params = new LayoutParams(i, j);
		params.leftMargin = (int) Utils.convertDpToPixel(getContext(), 0);
		params.rightMargin = (int) Utils.convertDpToPixel(getContext(), 0);
		params.topMargin = (int) Utils.convertDpToPixel(getContext(), 0);
		params.bottomMargin = (int) Utils.convertDpToPixel(getContext(), 0);
		params.gravity= Gravity.CENTER|Gravity.BOTTOM;
		setLayoutParams(params);
		
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.FILL);
        
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        
        textPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
		textPaint.setColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		textPaint.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		textPaint.setTextSize(35);
		
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.escudo);
	}
	
	/**
	 * Get a new progress with jump if jump is biggest than zero
	 * 
	 * @param progress
	 * @param jumpProgress
	 * @return
	 */
	public int getProgressWithJump(int progress, int jumpProgress) {
		int res = progress % jumpProgress;
		
		int newValue = 0;
		if(res >= (jumpProgress / 2)) {
			newValue = progress + jumpProgress - res;
		} else if(res < (jumpProgress / 2)) {
			newValue = progress - res;
		}
		
		return newValue;
	}
	
	@Override
    protected void onDraw(final Canvas canvas)
    {
        super.onDraw(canvas);
        
        canvas.drawPaint(backgroundPaint);
        
        updateProgress(canvas);
    }
	
	/**
	 * Update the progress in a animated form
	 * 
	 * @param canvas
	 */
	public void updateProgress(Canvas canvas)
	{
		if(startProgress < progress) {
            startProgress += JUMP_PROGRESS_ANIMATION;
            
        } else if(startProgress > progress) {
        	startProgress -= JUMP_PROGRESS_ANIMATION;
        }
		
		int width = canvas.getWidth() * startProgress / 100;
        
		//canvas.drawRect(10, 10, width, imageSize / 0.7f, progressPaint); // Progress Left to Right
		//canvas.drawRect(10, 10, imageSize / 0.7f, width, progressPaint); // Progress Up to Down
		canvas.drawRect(10, imageSize, imageSize, imageSize - width, progressPaint); // Progress Down to Up
        
		canvas.drawText(String.valueOf(startProgress) + "%", canvas.getWidth() / 2 - 20, canvas.getHeight() / 2 + 5, textPaint);
		
		canvas.drawBitmap(getResizedBitmap(bitmap, imageSize, imageSize), 0, 0, null);
		
		if(startProgress != progress) {
			postInvalidateDelayed((int) Math.abs(progress - startProgress) / duration);
        }
	}
	
	/**
	 * Get a resized bitmap
	 * 
	 * @param bitmap
	 * @param newHeight
	 * @param newWidth
	 * @return
	 */
	public Bitmap getResizedBitmap(Bitmap bitmap, int newHeight, int newWidth) {
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    
	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	/**
	 * Set progress to the thermometer
	 * 
	 * @param progress
	 */
	public void setProgress(int progress)
    {
    	this.progress = (int) Math.max(0, Math.min(progress, 100));
        invalidate();
    }
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        imageSize = width < DefaultWidth ? DefaultWidth : width;
        setMeasuredDimension(imageSize, imageSize);
    }
}
