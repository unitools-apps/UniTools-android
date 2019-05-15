package com.github.ali77gh.unitools.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.ui.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class StoragePacksPicksViewAdapter extends BaseAdapter {

    public static final int List = 0;
    public static final int Grid = 1;

    private int mode;
    private Activity _activity;
    private File[] files;
    private int screenWidth;

    public StoragePacksPicksViewAdapter(Activity activity, File[] filePacks,int mode) {
        this._activity = activity;
        this.files = filePacks;
        this.mode = mode;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
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

        ViewGroup cview;
        if (mode==List) cview = (ViewGroup) _activity.getLayoutInflater().inflate(R.layout.item_file_pack_pics_list, null);
        else if (mode==Grid) cview = (ViewGroup) _activity.getLayoutInflater().inflate(R.layout.item_file_pack_pics_grid, null);
        else throw new RuntimeException("invalid mode");
        File file = files[i];

        ImageView preview = (ImageView) cview.getChildAt(0);
        TextView name = (TextView) cview.getChildAt(1);

        name.setText(String.valueOf(file.getName().substring(0,file.getName().indexOf("."))));

        new BitmapLoader(file, preview).start();

        //last item
        if (i == files.length - 1) {
            cview.setPadding(0, 0, 0, Tools.DpToPixel(80));
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
            } else {
                if (mode == List)
                    bitmap = decodeFile(file, screenWidth, screenWidth);
                else if (mode==Grid)
                    bitmap = decodeFile(file, Tools.DpToPixel(90), Tools.DpToPixel(90));
                else throw new RuntimeException("invalid mode");
                bitmapCache.put(file, bitmap);
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

    public void onLowMemory() {
        bitmapCache.clear();
    }
}