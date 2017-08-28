package andy.zhu.aliinterview;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Andy.Zhu on 2017/8/26.
 */

public class Utils {

    public static float dp2pix(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float sp2pix(Context context, float sp) {
        return context.getResources().getDisplayMetrics().scaledDensity * sp;
    }
}
