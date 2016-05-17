package ryancheng.progressbar;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Create time: 2016/5/5.
 */
public class ProgressView extends View {
    private Paint paint;
    private RectF rectF;
    private int radius;
    private int bgColor, strokeColor;
    private int[] progressColor;
    private int strokeWidth;
    //private Path clipPath;
    private int padding;
    private int max = 10000;
    private int progress;
    private ObjectAnimator progressAnim;
    private Interpolator interpolator = new DecelerateInterpolator(1.5f);
    private Shader shader;
    private Matrix matrix = new Matrix();
    private int translateX;
    private int speed = 4;

    public ProgressView(Context context) {
        super(context);
        init(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
        bgColor = ContextCompat.getColor(context, R.color.bgColor);
        strokeColor = ContextCompat.getColor(context, R.color.strokeColor);
        progressColor = new int[2];
        progressColor[0] = ContextCompat.getColor(context, R.color.progressColor);
        progressColor[1] = ContextCompat.getColor(context, R.color.progressColor2);
        strokeWidth = dp2px(context, 2);
        padding = dp2px(context, 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = w;
        rectF.bottom = h;
        radius = h / 2;
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        radius = h / 2 - strokeWidth - padding;
        rectF.left = strokeWidth + padding;
        rectF.top = strokeWidth + padding;
        rectF.right = w * progress / max - strokeWidth - padding;
        if (rectF.right < rectF.left + radius * 2) {
            rectF.right = rectF.left + radius * 2;
        }
        rectF.bottom = h - strokeWidth - padding;
        paint.setStyle(Paint.Style.FILL);
//        if (clipPath == null) {
//            clipPath = new Path();
//        }
//        clipPath.reset();
//        clipPath.addRoundRect(rectF, radius, radius, Path.Direction.CCW);
//        canvas.save();
//        canvas.clipPath(clipPath, Region.Op.REPLACE);
//        canvas.drawColor(progressColor);
//        canvas.restore();
        translateX += speed;
        if (translateX >= 2 * 50) {
            translateX = 0;
        }
        matrix.setTranslate(translateX, 0);
        if (shader == null) {
            shader = new LinearGradient(0, 0, 50, 50,
                    new int[] {progressColor[0], progressColor[1], progressColor[0]},
                    new float[] {0, 0.4f, 0.7f},
                    Shader.TileMode.REPEAT);
        }
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setShader(null);

        rectF.left = strokeWidth / 2;
        rectF.top = strokeWidth / 2;
        rectF.right = w - strokeWidth / 2;
        rectF.bottom = h - strokeWidth / 2;
        radius = (h - strokeWidth) / 2;
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void updateProgress(int progress) {
        if (progress > max || progress < 0) {
            return;
        }
        if (this.progress != progress) {
            if (progressAnim != null) {
                progressAnim.cancel();
            }
            if (this.progress == max) {
                this.progress = 0;
            }
            PropertyValuesHolder pvh = PropertyValuesHolder.ofInt("progress", this.progress, progress);
            progressAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvh)
                    .setDuration(300);
            progressAnim.setInterpolator(interpolator);
            progressAnim.start();
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
