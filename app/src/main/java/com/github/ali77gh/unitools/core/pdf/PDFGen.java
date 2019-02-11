package com.github.ali77gh.unitools.core.pdf;

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
                    image.setAlignment(Image.MIDDLE | Image.TEXTWRAP);
                    image.setBorder(Image.BOX);
                    image.setBorderWidth(15);
                    image.scaleAbsolute(PageSize.A4);
                    document.add(image);
                }
                document.close();
                promise.onSuccess(null);
            } catch (Exception e) {
                promise.onFailed("");
            }
        }).start();
    }

}
