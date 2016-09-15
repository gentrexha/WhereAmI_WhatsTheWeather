package rks.gentrexha.whereamiwhatstheweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void btnMoreOnClick(View v) {
        Button btnMore = (Button)findViewById(R.id.btnMore);
        Intent intMap = new Intent(this, mapActivity.class);
        startActivity(intMap);
    }
}
