package com.abiolasoft.mysimesapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class ImageCompressor {

    public static byte[] compressImageForThumb(Context context, Uri imageUri) {

        Bitmap compressedImageFile = null;
        File newImageFile = new File(imageUri.getPath());
        try {
            compressedImageFile = new Compressor(context)
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(5)
                    .compressToBitmap(newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, boas);

        return boas.toByteArray();
    }
}
