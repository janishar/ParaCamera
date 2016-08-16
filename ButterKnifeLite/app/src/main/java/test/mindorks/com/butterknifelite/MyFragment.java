package test.mindorks.com.butterknifelite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;

/**
 * Created by janisharali on 17/08/16.
 */
public class MyFragment extends Fragment {

    @BindView(R.id.txtView)
    public TextView txtView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View promptsView = inflater.inflate(R.layout.my_fragment, container, false);
        ButterKnifeLite.bind(this, promptsView);
        return promptsView;
    }

    @OnClick(R.id.btn1)
    public void onBtn1Click(){
        txtView.setText("Btn 1 frag click");
    }

    @OnClick(R.id.btn2)
    public void onBtn2Click(){
        txtView.setText("Btn 2 frag click");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnifeLite.unbind(this);
    }
}
