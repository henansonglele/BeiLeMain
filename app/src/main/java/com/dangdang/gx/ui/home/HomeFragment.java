package com.dangdang.gx.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.BigImage.LargeImageViewActivity;
import com.dangdang.gx.ui.utils.LaunchUtils;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LaunchUtils.launchStoreNormalHtmlActivity(getActivity(),"www.baidu.com","百度");
            }
        });

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        //glide 展示图片
        ImageView imageView = root.findViewById(R.id.image);
        String url = "https://pics1.baidu.com/feed/267f9e2f07082838a012520a33efbd094c08f114.jpeg?token=2213ac292dc9eff3e88a0b0c99253c12&s=738243AE40511BDC04236AB70300500A";
        Glide.with(getActivity()).load(url).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///展示大图
                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                imageView.setDrawingCacheEnabled(false);
                Log.d("count:",""+bitmap.getByteCount());


                startActivity(new Intent(getActivity(), LargeImageViewActivity.class));
            }
        });

        return root;//
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**Glide 的使用：

            Glide.with(viewHolder.p_w_picpathView.getContext()) // 不光接受Context，还接受Activity 和 Fragment

            .load(url) // 图片的加载地址

        .diskCacheStrategy(DiskCacheStrategy.ALL) // 缓存类型，ALL：让Glide既缓存全尺寸又缓存其他尺寸

        .error(R.drawable.ic_person)//加载失败是显示的Drawable

        .placeholder()//loading时的Drawable

        .animate()//设置load完的动画

        .centerCrop()//中心切圆, 会填满

        .fitCenter()//中心fit, 以原本图片的长宽为主

        .into(p_w_picpathView); // 显示图片的容器
4. 加载GIF图片：

            Glide.with(context)
            .load(url)
    .asGif()
    .into(p_w_picpathView)

5. 加载Bitmap：

    可以用在设大图的背景

    Bitmap theBitmap = Glide.with(context)
            .load(url)
            .asBitmap()
            .into(100, 100). // 宽、高
        .get();

6. 缩略图 Thumbnail：

    缩略图, 0.1f就是原本的十分之一

    Glide.with(context)
            .load(url)
        .thumbnail(0.1f)
        .into(p_w_picpathView)
7. 变换图片的形状：

            Glide.with(getApplicationContext())
            .load(URL)
        .transform(new CircleTransform(getApplicationContext())) // 显示为圆形图片
            .into(p_w_picpathView);

    public class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null)
                return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size,
                        Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    加载圆形图片时，不能设置.centerCrop()，否则圆形不起作用。是不是所有的Transform都是这样，我没有测试

        8. Glide的一下配置，比如图片默认的RGB_565效果，可以创建一个新的GlideModule将Bitmap格式转换到ARGB_8888：
        public class GlideConfiguration implements GlideModule {

     @Override
     public void applyOptions(Context context, GlideBuilder builder) {
     // Apply options to the builder here.

      Disk Cache 缓存配置

    // 配置缓存大小：InternalCacheDiskCacheFactory
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, yourSizeInBytes));
    // 配置内存缓存
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheDirectoryName,
            yourSizeInBytes));
    // 配置外部缓存
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheDirectoryName,
            yourSizeInBytes));

    // 自定义配置
    // If you can figure out the folder without I/O:
    // Calling Context and Environment class methods usually do I/O.
            builder.setDiskCache(new DiskLruCacheFactory(getMyCacheLocationWithoutIO(),
    yourSizeInBytes));

    // In case you want to specify a cache folder ("glide"):
            builder.setDiskCache(new DiskLruCacheFactory(getMyCacheLocationWithoutIO(), "glide",
    yourSizeInBytes));

    // In case you need to query the file system while determining the folder:
            builder.setDiskCache(new DiskLruCacheFactory(new CacheDirectoryGetter() {
        @Override
        public File getCacheDirectory() {
            return getMyCacheLocationBlockingIO();
        }
    }), yourSizeInBytes);

}

// 如果你要创建一个完全自定义的缓存，可以实现DiskCache.Factory接口，并且使用DiskLruCacheWrapper创建缓存位置
        builder.setDiskCache(new DiskCache.Factory() {
@Override public DiskCache build () {
        File cacheLocation = getMyCacheLocationBlockingIO();
        cacheLocation.mkdirs();
        return DiskLruCacheWrapper.get(cacheLocation, yourSizeInBytes);
        }
        });


         // Memory caches and pools 配置

        // Size 默认是MemorySizeCalculator控制的，可以自定义
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        //如果你想在应用程序的某个阶段动态调整缓存内存，可以通过选择一个memorycategory通过使用setmemorycategory()
        Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);

        // Memory Cache  可以通过setMemoryCache() 方法来设置缓存大小，或者使用自己的缓存；
        // LruResourceCache是Glide的默认实现，可以通过构造函数自定义字节大小
        builder.setMemoryCache(newLruResourceCache(yourSizeInBytes));

        // Bitmap Pool 通过setBitmapPool() 设置Bitmap池的大小，LruBitmapPool是Glide的默认实现类，通过该类的构造函数更改大小
        builder.setBitmapPool(new LruBitmapPool(sizeInBytes));

        // Bitmap Format 通过setDecodeFormat() 方法设置设置图片质量
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);


@Override
public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
        }
        }
        同时在AndroidManifest.xml中将GlideModule定义为meta-data
        meta-data android:name="com.inthecheesefactory.lab.glidepicasso.GlideConfiguration"
        android:value="GlideModule"/>


        9. 特性
        你可以做到几乎和Picasso一样多的事情，代码也几乎一样。

        Image Resizing尺寸

        // Glide
        .override(300, 200);
        Center Cropping


        // Glide
        .centerCrop();
        Transforming


        // Glide
        .transform(new CircleTransform(context))

        设置占位图或者加载错误图：
        // Glide
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.p_w_picpathnotfound)

        10. 混淆文件的配置
        -keepnames class com.mypackage.GlideConfiguration
        # or more generally:
        #-keep public class * implements com.bumptech.glide.module.GlideModule

        希望对刚开始使用Glide的猿友们有所帮助，后续使用中有什么问题，我会继续添加的！

     */
}