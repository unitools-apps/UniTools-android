package com.github.ali77gh.unitools.core.pdf;

import android.graphics.BitmapFactory;

import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class PDFGen {

    public static void Generate(File[] images, File pdfPath, Promise promise) {

        new Thread(() -> {
            try {
                pdfPath.createNewFile();
                Document document = new Document(PageSize.A4, 0, 0, 0, 0);
                PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
                document.open();
                for (File imageFile : images) {
                    Image image = Image.getInstance(imageFile.getPath());

                    if (isLandscape(imageFile.getPath())) {
                        image.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight() / 2);
                    } else {
                        image.scaleAbsolute(PageSize.A4);
                    }
                    image.setAlignment(Image.MIDDLE | Image.TEXTWRAP);
                    image.setBorder(Image.BOX);
                    image.setBorderWidth(15);

                    document.add(image);
                }
                document.close();
                promise.onSuccess(null);
                MyDataBeen.onNewPdfGenerate();
            } catch (Exception e) {
                promise.onFailed("");
            }
        }).start();
    }

    private static boolean isLandscape(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        boolean orientation = true;
        try {
            BitmapFactory.decodeFile(path, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            if (imageHeight > imageWidth) {
                orientation = false;
            }
        } catch (Exception e) {
            //Do nothing
        }
        return orientation;
    }

}
