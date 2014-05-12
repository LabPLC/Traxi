package codigo.labplc.mx.traxi.buscarplaca.paginador.paginas.termometro;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout.LayoutParams;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.paginador.paginas.utlileria.Utils;

public class ThermometerView extends View {
	private int DefaultHeight = 20;
    private int DefaultWidth = 120;
    
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    
    private int startProgress = 0;
    private int progress = 50;
    public static final int JUMP_PROGRESS_ANIMATION = 10;
    
    private int duration = 2000; //duration in millis
    
    private int shadowColor = getResources().getColor(R.color.gris_obscuro);
    private int startShadowColor = getResources().getColor(R.color.gris_claro);
    
    private int backgroundColor = getResources().getColor(R.color.rojo_logo);
    private int progressColor = getResources().getColor(R.color.gris_obscuro);
    private int startProgressColor = getResources().getColor(R.color.gris_obscuro);

	private ValueAnimator shadowColorAnimation;
    
	public ThermometerView(Context context)
	{
		super(context);
		
		initUI();
	}

	public ThermometerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		initUI();
	}
	
	public ThermometerView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		
		initUI();
	}

	public void initUI()
	{
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = (int) Utils.convertDpToPixel(getContext(), 16);
		params.rightMargin = (int) Utils.convertDpToPixel(getContext(), 16);
		params.topMargin = (int) Utils.convertDpToPixel(getContext(), 32);
		params.bottomMargin = (int) Utils.convertDpToPixel(getContext(), 16);
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
		textPaint.setColor(Color.BLACK);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setTextSize(30);
        
        startShadowAnimation(startShadowColor, shadowColor);
	}
	
    public int getProgress()
    {
		return progress;
	}
    
	public void setProgress(int progress)
	{
		this.progress = progress;
	}
    
    public void setThermometerProgress(int progress)
    {
    	this.progress = (int) Math.max(0, Math.min(progress, 100));
        invalidate();
    }
	
    public int getThermometerProgress()
    {
    	return progress;
    }
    
	public void setShadowColor(int shadowColor)
	{
		this.shadowColor = shadowColor;
	}
	
	public int getShadowColor()
	{
		return shadowColor;
	}
	
	public void setStartShadowColor(int startShadowColor)
	{
		this.startShadowColor = startShadowColor;
	}
	
	public int getStartShadowColor()
	{
		return startShadowColor;
	}
	
	public int getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	
	public int getProgressColor()
	{
		return progressColor;
	}

	public void setProgressColor(int progressColor)
	{
		this.progressColor = progressColor;
		
		//shadowColorAnimation = null;
		//startShadowAnimation(startShadowColor, progressColor);
	}
	
	public int getStartProgressColor()
	{
		return startProgressColor;
	}

	public void setStartProgressColor(int startProgressColor)
	{
		this.startProgressColor = startProgressColor;
	}
	
	public void startShadowAnimation(int startShadowColor, int shadowColor)
	{
		shadowColorAnimation = ValueAnimator.ofInt(startShadowColor, shadowColor);
		shadowColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		    @Override
		    public void onAnimationUpdate(ValueAnimator animation) {
		    	setStartShadowColor(Integer.parseInt(animation.getAnimatedValue().toString()));
		    }
		});
		shadowColorAnimation.setDuration(1000);
		shadowColorAnimation.setEvaluator(new ArgbEvaluator());
		shadowColorAnimation.setRepeatCount(Animation.INFINITE);
		shadowColorAnimation.setRepeatMode(Animation.REVERSE);
		shadowColorAnimation.start();
	}
	
	public int getNewShadowColor(int startProgressColor, int progressColor, int progress) {
		//String newColor = Integer.toHexString(backgroundColor & 0x00FFFFFF);
		int r1 = (startProgressColor >> 16) & 0xff;
		int g1 = (startProgressColor >> 8) & 0xff;
		int b1 = startProgressColor & 0xff;
		
		int r2 = (progressColor >> 16) & 0xff;
		int g2 = (progressColor >> 8) & 0xff;
		int b2 = progressColor & 0xff;
		
		int r = r2 - r1 / 10 * progress;
		int g = g2 - g1 / 10 * progress;
		int b = b2 - b1 / 10 * progress;
		
		return Color.rgb(r, g, b);
	}
	
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
	
	public void updateProgress(Canvas canvas)
	{
		if(startProgress < progress) {
            startProgress += JUMP_PROGRESS_ANIMATION;
            
        } else if(startProgress > progress) {
        	startProgress -= JUMP_PROGRESS_ANIMATION;
        }
		
		progressPaint.setShadowLayer(10, 0, 0, startShadowColor);
		
		int width = canvas.getWidth() * startProgress / 100;
        
		canvas.drawRect(10, 10, width, DefaultHeight / 0.7f, progressPaint);
        
		canvas.drawText(String.valueOf(startProgress) + "%", canvas.getWidth() / 2, canvas.getHeight() / 2 + 5, textPaint);
		
        postInvalidateDelayed((int) Math.abs(progress - startProgress) / duration);
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width < DefaultWidth ? DefaultWidth : width, DefaultHeight * 2);
    }
}
