package sample.project.ayan.simple_pdf_reader;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class Viewer extends Activity {

    private ImageView imageView;
    private EditText editText;
    private int pages=0, currPage=0;
    private Uri filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.hide();
        setContentView(R.layout.activity_viewer);
        Bundle bundle = getIntent().getExtras();
        filepath = Uri.parse(bundle.getString("uri"));
        imageView = (ImageView)findViewById(R.id.imageView);
        editText = (EditText)findViewById(R.id.editText2);
        renderPage(currPage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void renderPage(int pageNumber)
    {
        Matrix matrix = imageView.getImageMatrix();
        imageView.measure(imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        try {
            ParcelFileDescriptor parcelFileDescriptor = getApplicationContext().getContentResolver().openFileDescriptor(filepath,"r");
            PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            PdfRenderer.Page page = pdfRenderer.openPage(pageNumber);
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(),page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, new Rect(0,0,page.getWidth(),page.getHeight()),matrix, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            if(pages==0)
                pages = pdfRenderer.getPageCount();
            imageView.setMaxWidth(page.getWidth());
            imageView.setMaxHeight(page.getHeight());
            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
            editText.setText("Page : " + (currPage + 1) + " / " + pages);
            page.close();
            pdfRenderer.close();
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prevPage(View view)
    {
        if(currPage==0)
        {
            Toast.makeText(getApplicationContext(),"Beginning of Document",Toast.LENGTH_SHORT).show();
        }
        else
        {
            currPage--;
            renderPage(currPage);
        }
    }

    public void nextPage(View view)
    {
        if(currPage == pages-1)
        {
            Toast.makeText(getApplicationContext(),"End of Document",Toast.LENGTH_SHORT).show();
        }
        else
        {
            currPage++;
            renderPage(currPage);
        }
    }
}