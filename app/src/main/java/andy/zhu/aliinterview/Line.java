package andy.zhu.aliinterview;

import android.graphics.PointF;

/**
 * Created by Andy.Zhu on 2017/8/27.
 */

public class Line {

    public float k;
    public float b;

    public Line(float k, float b) {
        this.k = k;
        this.b = b;
    }

    public Line(float k, PointF point) {
        this.k = k;
        this.b = point.y - k * point.x;
    }

    public void set(float k, float b) {
        this.k = k;
        this.b = b;
    }

    public PointF findHLineInterPoint(float y) {
        float x = (y - b) / k;
        return new PointF(x, y);
    }

    public PointF findVLineInterPoint(float x) {
        return new PointF(x, k * x + b);
    }

    public void otherSizePoint(float x, float y, PointF out) {
        float d = (x + (y - b) * k) / (1 + k * k);
        float x2 = 2 * d - x;
        float y2 = 2 * d * k - y + 2 * b;
        out.set(x2, y2);
    }
}
