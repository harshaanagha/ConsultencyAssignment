package com.anagha.assignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        ProductsFragment fragment = new ProductsFragment();
        transaction.add(R.id.products_fragment, fragment);
        //mToolbar.setTitle("Read Review");
        transaction.commit();
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new UsingFreqLimitedMemoryCache(25 * 1024 * 1024))// 25 Mb (delete most not used image)
                .build();
        ImageLoader.getInstance().init(config);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}
