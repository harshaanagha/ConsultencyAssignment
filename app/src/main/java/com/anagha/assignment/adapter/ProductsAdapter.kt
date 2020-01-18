package com.anagha.assignment.adapter

import android.graphics.Bitmap
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anagha.assignment.R
import com.anagha.assignment.models.Row
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader

class ProductsAdapter(internal var data: List<Row>) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {
    private val options: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_launcher_foreground)
            .showImageForEmptyUri(R.drawable.ic_launcher_foreground)
            .showImageOnFail(R.drawable.ic_launcher_foreground)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val myViewHolder: ProductsViewHolder
        val itemLayoutView = LayoutInflater.from(parent.context).inflate(
                R.layout.list_row, parent, false)
        myViewHolder = ProductsViewHolder(itemLayoutView)
        return myViewHolder
    }

    override fun onBindViewHolder(myHolder: ProductsViewHolder, position: Int) {
        if (getItem(position).title != null) {
            myHolder.listRowNameTV.text = getItem(position).title
        } else {
            myHolder.listRowNameTV.text = "No Title"
        }
        if (getItem(position).description != null) {
            myHolder.listRowDescTV.text = Html.fromHtml(getItem(position).description)
        } else {
            myHolder.listRowDescTV.text = "No Description"
        }
        if (getItem(position).imageHref != null) {
            try {
                ImageLoader.getInstance().displayImage(getItem(position).imageHref, myHolder.listRowIM, options)
            } catch (npe: NullPointerException) {
                ImageLoader.getInstance().displayImage(R.drawable.ic_launcher_foreground.toString(), myHolder.listRowIM, options)
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getItem(position: Int): Row {
        return data[position]
    }

    inner class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listRowNameTV: TextView
        var listRowDescTV: TextView
        var listRowIM: ImageView

        init {
            listRowNameTV = itemView.findViewById<View>(R.id.list_row_nameTV) as TextView
            listRowDescTV = itemView.findViewById<View>(R.id.list_row_descTV) as TextView
            listRowIM = itemView.findViewById<View>(R.id.list_row_IM) as ImageView
        }
    }
}
