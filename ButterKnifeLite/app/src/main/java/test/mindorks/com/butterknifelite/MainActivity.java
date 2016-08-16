package test.mindorks.com.butterknifelite;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txtView)
    public TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnifeLite.bind(this);
        txtView.setText("Testing");
    }

    @OnClick(R.id.btn1)
    public void onBtn1Click(){
        txtView.setText("Btn 1 click");
    }

    @OnClick(R.id.btn2)
    public void onBtn2Click(){
        txtView.setText("Btn 2 click");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnifeLite.unbind(this);
    }
}
