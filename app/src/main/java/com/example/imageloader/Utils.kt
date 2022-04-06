package com.example.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.google.android.material.imageview.ShapeableImageView

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class Utils {
    companion object {
        private const val IMAGE_URL = "https://picsum.photos/500/500"

        /**
         * Returns true if device is connected to internet
         */
        private fun isInternetAvailable(
            context: Context
        ): Boolean {
            val connectivityManager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }

        /**
         * Load images from Lorem Picsum (https://picsum.photos/)
         */
        fun loadNewImage(
            context: Context,
            imageView: ShapeableImageView,
            progressBar: ProgressBar
        ) {
            if (!isInternetAvailable(context)) {
                Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
                return
            }
            val prefs = context.getSharedPreferences("imageloader", Context.MODE_PRIVATE)
            val blur = prefs.getBoolean("blur", false)
            val greyscale = prefs.getBoolean("greyscale", false)
            var url = IMAGE_URL
            if (greyscale && blur) {
                url += "?grayscale&blur"
            } else {
                if (blur) url += "?blur"
                if (greyscale) url += "?grayscale"
            }

            progressBar.visibility = View.VISIBLE

            Glide
                .with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(object : CustomTarget<Bitmap?>() {
                     override fun onResourceReady(
                         resource: Bitmap,
                         transition: Transition<in Bitmap?>?
                     ) {
                         progressBar.visibility = View.INVISIBLE
                         imageView.setImageBitmap(resource)
                         saveImage(context, resource)
                     }

                     override fun onLoadCleared(
                         placeholder: Drawable?
                     ) {
                     }
                })
        }

        /**
         * Save bitmap to app storage
         */
        private fun saveImage(
            context: Context,
            bitmap: Bitmap
        ) {
            val directory: File = context.getDir("saved_image", Context.MODE_PRIVATE)
            val filepath = File(directory, "image.png")
            try {
                val fos = FileOutputStream(filepath)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
            } catch (e: Exception) {
            }
        }

        /**
         *  Load saved bitmap from app storage
         */
        fun loadSavedImage(
            context: Context,
            imageView: ShapeableImageView
        ) {
            val directory: File = context.getDir("saved_image", Context.MODE_PRIVATE)
            val filepath = File(directory, "image.png")
            try {
                val fis = FileInputStream(filepath)
                imageView.setImageBitmap(BitmapFactory.decodeStream(fis))
                fis.close()
            } catch (e: Exception) {
            }
        }
    }
}