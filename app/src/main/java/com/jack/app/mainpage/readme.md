当前包用于组织所有页面的分类、入口；用于展示不同的类别的事例的入口

package com.jack.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.jack.analysis.crash.NullpointerCrashTextActivity;
import com.jack.analysis.recycler.activity.AnalyseRecyclerViewActivity;
import com.jack.app.customviews.SkinShareImageScoreView;
import com.jack.app.test.anim.TestDragFollowAnimationActivity;
import com.jack.app.test.anim.TestWaveViewActivity;
import com.jack.app.test.anim.TestWaveEffectActivity;
import com.jack.app.test.image.TestViewToBitmapActivity;
import com.jack.app.test.textview.TestHtml2StringActivity;
import com.jack.app.test.viewgroup.TestRelativeLayoutLayoutRules;
import com.jack.analysis.activity.lifecircle.FirstActivity;
import com.jack.app.time.TestTimerActivity;

/**
 * @author liuyang
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SkinShareImageScoreView skinShareImageScoreView = (SkinShareImageScoreView) findViewById(R.id.ssis_share_score_view);
        skinShareImageScoreView.updateScoreInfo(88, "你和完美只差0.01毫米的距离", "");

        skinShareImageScoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvBottomView = (TextView) findViewById(R.id.tv_bottom_view);
                String string = "常年保持了快速女乘客尽量不吃进出口量多少年才离开简单处理接口都不能吃进出口量年才离开家啊但是白菜价啦的说不出来的说不出来就是对彼此了解手到病除觉得食不充饥啦巴丹吉林还不擦就来电话说不出来喝酒啊但是波罗的海就撒比福利哈就是打败了哈的军事报道 ";
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                Drawable spannableDrawable = getResources().getDrawable(R.drawable.skin_score_emoji_icon);
                spannableDrawable.setBounds(0, 0, (int) tvBottomView.getTextSize(), (int) tvBottomView.getTextSize());
                ImageSpan imageSpan = new ImageSpan(spannableDrawable, ImageSpan.ALIGN_BASELINE);
                spannableStringBuilder.setSpan(imageSpan, string.length() - 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvBottomView.setText(spannableStringBuilder);
            }
        });

        findViewById(R.id.tv_go_test_span).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, TestTypefontSpanActivity.class);
//                startActivity(intent);
            }
        });

        TextView phoneInfo = (TextView) findViewById(R.id.tv_go_test_span);
        String phoneInfostr = Build.BRAND + "   " + Build.MODEL + "   " + Build.PRODUCT;
        phoneInfo.setText(phoneInfostr);

        findViewById(R.id.go_to_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.go_to_drag_follow_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_wave_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestWaveEffectActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_wave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestWaveViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_drag_wave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestDragFollowAnimationActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_v_2_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestViewToBitmapActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_timer_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestTimerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_recyclerview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AnalyseRecyclerViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_drop_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

        findViewById(R.id.go_to_drop_down_vlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });


        findViewById(R.id.go_to_drop_down_vlayout_comp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_textView_with_Arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, TestTextArrowActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_webview_with_Arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_crash_with_Arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NullpointerCrashTextActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_autosize_view_with_Arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_go_to_shopmall_view_with_Arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_go_to_string_splite_with_Arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestHtml2StringActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_test_frame_back_ground_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_test_compnoent_span_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_test_relative_rules_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestRelativeLayoutLayoutRules.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_go_to_test_double_activity_start_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });
    }
}
