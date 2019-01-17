package activity;

import android.app.Activity;
import android.os.Bundle;

import com.jack.imageview.R;
import com.jack.webview.WebViewAlertDialog;

/**
 * Created by liuyang on 2018/1/4.
 */

public class TestWebViewCornerActivity extends Activity implements WebViewAlertDialog.OnCloseDialogListener{
    WebViewAlertDialog dialog = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_web_view_layout);
        showDialog();
    }

    private void showDialog() {
            WebViewAlertDialog.Builder builder = new WebViewAlertDialog.Builder(this);
            builder.setUrl("https://www.baidu.com")
                    .setOnCloseDialogListener(this);
            dialog = builder.create();
            dialog.show();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCloseDialog() {
//        finish();
        dialog.dismiss();
    }
}
