package redrock.tongji.redrockexam.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import redrock.tongji.redrockexam.App
import redrock.tongji.redrockexam.R

/**
 * @Author Tongji
 * @Description
 * @Date create in 2022/7/22 23:17
 */
class PhotoShowAdapter(private val data: ArrayList<String>) :
    RecyclerView.Adapter<PhotoShowAdapter.InnerHolder>() {

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val showPhoto: PhotoView = view.findViewById(R.id.photo_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_photo, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.apply {
            Glide.with(App.context).load(data[position]).centerCrop().into(showPhoto)
        }
    }

    override fun getItemCount(): Int = data.size
}