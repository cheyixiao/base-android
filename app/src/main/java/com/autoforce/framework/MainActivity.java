package com.autoforce.framework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.*;
import android.widget.Toast;
import com.autoforce.framework.component.MainTabGroup;
import com.autoforce.framework.config.*;
import org.jetbrains.annotations.NotNull;

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

        bindViews();

        mConfigResolver.loadTabsInfo(this, null);
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

        mRgMain.setData(config);

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
