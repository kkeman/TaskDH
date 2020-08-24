package com.service.codingtest.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.service.codingtest.R
import com.service.codingtest.manager.AppDB
import com.service.codingtest.model.response.FavoriteEntity
import kotlinx.android.synthetic.main.item_image.view.*

class FavoriteAdapter(private var mData: List<FavoriteEntity>, private val mAct: FragmentActivity) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(), View.OnClickListener {

    private val mInflater = LayoutInflater.from(mAct)

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_thumb = itemView.iv_thumb
        val cb_favorite = itemView.cb_favorite
        val tv_title = itemView.tv_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_image, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = mData[position]
        Glide.with(mAct).load(entity.thumbnail)
                .transition(withCrossFade())
                .into(holder.iv_thumb)

        holder.cb_favorite.isChecked = true
        holder.cb_favorite.setOnCheckedChangeListener { compoundButton, b ->
            if(!b)
                AppDB.getInstance(mAct).favoriteDao().delete(entity)
        }

        holder.tv_title.text = entity.name
    }

    override fun onClick(v: View) {

        val position = v.tag as Int

//        val file = File(getItem(position))
//        file.delete()

        notifyItemRemoved(position)
    }

    companion object {
        val ChatDiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int = mData.size
}