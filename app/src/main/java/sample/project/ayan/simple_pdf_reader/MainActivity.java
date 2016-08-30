package sample.project.ayan.simple_pdf_reader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

    public static int GET_FILE_PATH = 1;
    public Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setTitle("Simple PDF Reader");

        setContentView(R.layout.activity_main);
    }

    public void selectFile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent,"Open"), GET_FILE_PATH);
    }

    public void viewFile(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Viewer.class);
        intent.putExtra("uri",uri.toString());
        startActivity(intent);
    }

    protected void  onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode== GET_FILE_PATH && resultCode==RESULT_OK)
        {
            uri = intent.getData();
            EditText text = (EditText)findViewById(R.id.editText);
            text.setText(uri.toString());
            findViewById(R.id.button2).setEnabled(true);
        }
    }
}