package com.autoforce.common.utils;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.autoforce.common.R;

import java.lang.reflect.Constructor;

/**
 * Created by xialihao on 2018/11/16.
 */
public class DrawableUtils {


    public static final int DRAWABLE_LEFT = 0;
    public static final int DRAWABLE_TOP = 1;
    public static final int DRAWABLE_RIGHT = 2;
    public static final int DRAWABLE_BOTTOM = 3;

    /**
     * 将drawable缩小至原来的三分之二
     *
     * @param targetView
     * @param position   可选值 DRAWABLE_LEFT  DRAWABLE_TOP  DRAWABLE_RIGHT  DRAWABLE_BOTTOM
     */
    public static void resizeDrawable(TextView targetView, int position, float factor) {

        if (targetView != null) {
            Drawable[] drawables = targetView.getCompoundDrawables();
            Rect rect = new Rect(0, 0, (int) (drawables[position].getMinimumWidth() * factor), (int) (drawables[position].getMinimumHeight() * factor));
            drawables[position].setBounds(rect);

            targetView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }
    }

    public static int getImageResourceId(String name) {
        //默认的id
        int resId = -1;
        try {
            Class<R.drawable> clazz = R.drawable.class;

            Constructor<R.drawable> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            R.drawable drawables = constructor.newInstance();

            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            java.lang.reflect.Field field = clazz.getField(name);
            //取值
            resId = (Integer) field.get(drawables);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;
    }


}
