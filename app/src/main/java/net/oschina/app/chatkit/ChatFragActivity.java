package net.oschina.app.chatkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import net.oschina.app.R;
/**
 * Created by 大灯泡 on 2018/10/26.
 * <p>
 * 实际上就是mainActivity那套，针对Fragment用
 */

public class ChatFragActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ChatFragment())
                    .commit();
        }
    }

}
