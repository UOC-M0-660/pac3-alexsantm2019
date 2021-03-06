package edu.uoc.pac3.data.streams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.uoc.pac3.R

class StreamAdapter(private var streams: MutableList<Stream>?) :
    RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    fun setStreams(streams: MutableList<Stream>) {
        this.streams = streams
        // Reloads the RecyclerView with new adapter data
        notifyDataSetChanged()
    }

    fun addStreams(streams: List<Stream>) {
        val previousSize = this.streams?.size
        this.streams?.addAll(streams)
        if (previousSize != null) {
            notifyItemRangeInserted(previousSize, streams.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stream_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = streams?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = streams?.get(position)
        if (stream != null) {
            holder.userName.text = stream.userName
            holder.title.text = stream.title

            if (!stream.thumbnailUrl.isNullOrBlank()) {
                val imageThumbnail: String =
                    stream.thumbnailUrl.replace("{width}", "100").replace("{height}", "100")

                Glide.with(holder.thumbnailUrl.getContext())
                    .load(imageThumbnail)
                    .placeholder(R.drawable.ic_user)
                    .apply(RequestOptions().override(600, 200))
                    .into(holder.thumbnailUrl);
            }
        }
    }

    // Holds an instance to the view for re-use
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.tvUserName)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val thumbnailUrl: ImageView = view.findViewById(R.id.ivThumbnail)
    }

}

