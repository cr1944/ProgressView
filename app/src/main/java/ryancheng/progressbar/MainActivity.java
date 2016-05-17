package ryancheng.progressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    ProgressView progressView;
    Handler handler;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (ProgressView) findViewById(R.id.progress);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                progressView.updateProgress(progress * 100);
                progress++;
                handler.sendEmptyMessageDelayed(0, 200);
                return true;
            }
        });
        handler.sendEmptyMessage(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 100, 0, "refresh");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 100) {
            progress = 0;
        }
        return super.onOptionsItemSelected(item);
    }
}
