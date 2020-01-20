package com.anagha.assignment

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments)
        val transaction = supportFragmentManager.beginTransaction()

        val fragment = ProductsFragment()
        transaction.add(R.id.products_fragment, fragment)
        //mToolbar.setTitle("Read Review");
        transaction.commit()

        val config = ImageLoaderConfiguration.Builder(this)
                .memoryCache(UsingFreqLimitedMemoryCache(25 * 1024 * 1024))// 25 Mb (delete most not used image)
                .build()
        ImageLoader.getInstance().init(config)
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    public override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        System.gc()
        super.onDestroy()
    }
}
