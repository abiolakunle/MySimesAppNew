package com.abiolasoft.mysimesapp.Activities;

import android.os.Bundle;

import com.abiolasoft.mysimesapp.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class EBookViewerActivity extends BaseActivity {
    public static String eBookViewerUrl = "FILE_URL";
    private PDFView library_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_viewer);
        library_pdf = findViewById(R.id.library_pdfView);

        try {
            URL url = new URL(eBookViewerUrl);
            File file = new File(url.getFile());
            library_pdf.fromFile(file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean useToolbar() {
        return super.useToolbar();
    }


}
