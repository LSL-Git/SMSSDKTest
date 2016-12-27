package lsl.com.smssdktest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et2;
    private EditText et1;
    private Button but4;
    private Button but3;
    private Button but2;
    private Button but1;

    private static final String TAG = "MainActivity";
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
        initLayout();

        // 注册回调
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initLayout() {
        but1 = (Button) findViewById(R.id.but1);
        but2 = (Button) findViewById(R.id.but2);
        but3 = (Button) findViewById(R.id.but3);
        but4 = (Button) findViewById(R.id.but4);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);

        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
        but3.setOnClickListener(this);
        but4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but1: // 验证码
                opeRegisterPager();
                break;
            case R.id.but2:
                openContractsPager();
                break;
            case R.id.but3: // 获取验证码
                String phone = et1.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    SMSSDK.getVerificationCode("86",phone);
                    Toast.makeText(mcontext, "手机号码是：" + phone, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.but4: // 发送验证码
                String phone2 =et1.getText().toString().trim();
                String code = et2.getText().toString().trim();
                if (TextUtils.isEmpty(phone2) || TextUtils.isEmpty(code)) {

                    Toast.makeText(mcontext, "手机号码和验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("+86",phone2,code);
                    Toast.makeText(mcontext, "手机号码" + phone2 + "验证码：" + code, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 提交验证码成功
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.e(TAG, "提交验证码成功--country:" + country + "--phone:" + phone);
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    // 获取验证码成功
                    Log.e(TAG,"获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };

    private void openContractsPager() {
        // 打开通信录好友列表页面
        ContactsPage contactsPage = new ContactsPage();
        contactsPage.show(mcontext);
    }

    private void opeRegisterPager() {
        // 打开注册页面.这个页面就是我们在androidMainfest.xml 中添加的结点com.mob.tools.MobUIShell
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.e(TAG, "opeRegisterPager()--country:" + country + "--phone:" + phone);
                }
            }
        });
        registerPage.show(mcontext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 防止内存泄漏
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
