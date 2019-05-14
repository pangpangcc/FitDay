package com.example.fitday.Gallery

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fitday.R
import android.net.Uri
import android.support.v4.content.FileProvider
import com.example.fitday.BuildConfig
import java.io.File
import java.lang.Math.pow
import java.net.URI
import kotlin.concurrent.thread

const val THUMBNAIL_SIZE = 128

class GalleryAdapter(private var activity: Activity, private var items: ArrayList<String>) : BaseAdapter() {
    private class ViewHolder(row: View?) {
        var date: TextView? = null
        var image: ImageView? = null

        init {
            this.date = row?.findViewById<TextView>(R.id.date_gallery)
            this.image = row?.findViewById<ImageView>(R.id.image_gallery)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.gallery_row, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var imgFile = File(items[position])


        var parts = items[position].split("||")
        var date = if(parts.size>1) {
            parts[1]
        }
        else
        {
            parts[0]
        }
            viewHolder.date?.text = date

        //val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath().toString() )
        val myBitmap2 = getPreview(items[position])

        viewHolder.image?.setImageBitmap(myBitmap2)

        view!!.setOnLongClickListener{view1 ->
            imgFile.delete()
            items.removeAt(position)
            this.notifyDataSetChanged()
            return@setOnLongClickListener true
        }

        view!!.setOnClickListener { view2 ->
            var myDialog = PhotoDialog(activity,imgFile,date)
            myDialog.show()
        }
        return view as View
    }

    fun getPreview(uri: String): Bitmap? {
        val image = File(uri)

        val bounds = BitmapFactory.Options()
        bounds.inJustDecodeBounds = true
        BitmapFactory.decodeFile(image.getPath(), bounds)
        if (bounds.outWidth == -1 || bounds.outHeight == -1)
            return null

        val originalSize = if (bounds.outHeight > bounds.outWidth)
            bounds.outHeight
        else
            bounds.outWidth

        val opts = BitmapFactory.Options()
        opts.inSampleSize = originalSize / THUMBNAIL_SIZE
        return BitmapFactory.decodeFile(image.getPath(), opts)
    }
    override fun getItem(i: Int): String {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}
