package com.github.ali77gh.unitools.uI.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.uI.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class StoragePacksPicksListViewAdapter extends BaseAdapter {

    private Activity _activity;
    private File[] files;

    public StoragePacksPicksListViewAdapter(Activity activity, File[] filePacks) {

        this._activity = activity;
        this.files = filePacks;
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewGroup cview = (ViewGroup) _activity.getLayoutInflater().inflate(R.layout.item_file_pack_pics, null);
        File file = files[i];

        ImageView preview = (ImageView) cview.getChildAt(0);
        TextView name = (TextView) cview.getChildAt(1);

        name.setText(file.getName());

        new BitmapLoader(file, preview).start();

        //last item
        if (i == files.length - 1) {
            cview.setPadding(0, 0, 0, tools.DpToPixel(80));
        }

        return cview;
    }

    private HashMap<File, Bitmap> bitmapCache = new HashMap<>();

    private class BitmapLoader extends Thread {

        private File file;
        private ImageView imageView;

        BitmapLoader(File file, ImageView imageView) {
            this.file = file;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            super.run();
            Bitmap bitmap;
            if (bitmapCache.containsKey(file)) {
                bitmap = bitmapCache.get(file);
            }else {
                bitmap = decodeFile(file, 100, 100);
                bitmapCache.put(file,bitmap);
            }

            imageView.post(() -> imageView.setImageBitmap(bitmap));
        }

        private Bitmap decodeFile(File f, int WIDTH, int HIGHT) {
            try {
                //Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f), null, o);

                //The new size we want to scale to
                final int REQUIRED_WIDTH = WIDTH;
                final int REQUIRED_HIGHT = HIGHT;
                //Find the correct scale value. It should be the power of 2.
                int scale = 1;
                while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                    scale *= 2;

                //Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            } catch (FileNotFoundException e) {
            }
            return null;
        }
    }

    public void onLowMemory(){
        bitmapCache.clear();
    }
}