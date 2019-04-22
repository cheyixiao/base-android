package com.autoforce.framework;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.*;
import android.widget.Toast;
import com.autoforce.common.utils.DrawableUtils;
import com.autoforce.common.view.tab.MainTabGroup;
import com.autoforce.common.view.tab.config.*;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xialihao on 2018/11/15.
 */
public class MainActivity extends AppCompatActivity implements OnMainConfigCallback {

    private MainConfigResolveInterface mConfigResolver = new MainConfigResolver(this);
    private MainTabGroup mRgMain;
    private int currentTabIndex = -1;
    private long exitTime = 0;
    private Bundle savedInstanceState;

    private static final String FRAGMENT_TAG_PREFIX = "MainActivityFragment_";
    private static final String STATE_CURRENT_TAB_INDEX = "StateCurrentTabIndex";

    public static void start(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.savedInstanceState = savedInstanceState;

        copyHtmlToInternal();

        bindViews();
        mConfigResolver.loadTabsInfo(this, "http://192.168.3.245:8080/main_config.json");
    }

    private void copyHtmlToInternal() {

        try {
            InputStream is = getAssets().open("test.html");
            FileOutputStream fos = openFileOutput("test.html", MODE_PRIVATE);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt(MainActivity.STATE_CURRENT_TAB_INDEX, currentTabIndex);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            currentTabIndex = savedInstanceState.getInt(MainActivity.STATE_CURRENT_TAB_INDEX);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - exitTime > 3000) {
                Toast.makeText(MainActivity.this, R.string.double_click_quit, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void bindViews() {

        mRgMain = findViewById(R.id.rg_main);
    }


    @Override
    public void onConfigDataGot(@NotNull MainConfigResult config) {

        mRgMain.setData(config, iconPath -> {
            try {
                if (!TextUtils.isEmpty(iconPath)) {
                    // 网络图片
                    if (iconPath.startsWith("http")) {
                        Bitmap bitmap = Picasso.get().load(iconPath).get();
                        return new BitmapDrawable(getResources(), bitmap);
                    } else {
                        // 本地drawable图片
                        int index = iconPath.lastIndexOf(".");
                        String name = iconPath;
                        if (index != -1) {
                            name = iconPath.substring(0, index);
                        }

                        int resourceId = DrawableUtils.getImageResourceId(MainActivity.this, name);
                        if (resourceId != 0) {
                            return ContextCompat.getDrawable(MainActivity.this, resourceId);
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        });

        int index;
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(MainActivity.STATE_CURRENT_TAB_INDEX);
        } else {
            index = config.getDefaultCheck();
        }

        showTab(index);
        mRgMain.check(ConfigUtils.getIdByIndex(index));

        mRgMain.setOnCheckedChangeListener((group, checkedId) -> {
            showTab(ConfigUtils.getIndexById(checkedId));
        });


    }

    private void showTab(int index) {

        if (index != currentTabIndex) {
            changeFragment(index, currentTabIndex);
            currentTabIndex = index;
        }

    }

    private void changeFragment(int newTabIndex, int oldTabIndex) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment currentFragment = null;
        if (oldTabIndex >= 0) {
            currentFragment = getSupportFragmentManager().findFragmentByTag(genFragmentTag(oldTabIndex));
        }

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        Fragment targetFragment = getSupportFragmentManager().findFragmentByTag(genFragmentTag(newTabIndex));

        if (targetFragment == null) {
            targetFragment = createFragment(newTabIndex);
            transaction.add(R.id.fl_content, targetFragment, genFragmentTag(newTabIndex));
        } else {
            transaction.show(targetFragment);
        }
        transaction.commit();

    }

    private String genFragmentTag(int index) {
        return MainActivity.FRAGMENT_TAG_PREFIX + index;
    }

    private Fragment createFragment(int index) {

        View childView = mRgMain.getChildAt(index);
        MainConfigResult.TabInfoBean info = (MainConfigResult.TabInfoBean) childView.getTag();

        if (TextUtils.isEmpty(info.getWebUrl())) {
            //通过pageName获得对应的原生Fragment对象
            return new NativeFragment();
        } else {
            return CommonWebFragment.newInstance(info.getWebUrl());
        }
    }

}
