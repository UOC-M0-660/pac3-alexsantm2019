package edu.uoc.pac3.data.streams

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.uoc.pac3.R

class StreamAdapter(private var streams: List<Stream>?) : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    private fun getStreams(position: Int): Stream? {
        return streams?.get(position)
    }

    fun setStreams(streams: List<Stream>) {
        this.streams = streams
        // Reloads the RecyclerView with new adapter data
        notifyDataSetChanged()
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.stream_list, parent, false)
//    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    Log.d("Adapter", "************* onCreateViewHolder ******************* ")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stream_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = streams?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val stream = getStreams(position)
        val stream = streams?.get(position)
        if (stream != null) {
            holder.userName.text = stream.user_name
            holder.title.text = stream.title
            //holder.thumbnailUrl. = stream.thumbnail_url
            //val pattern = "\\{width}x\\{height}".toRegex()
            //val pattern = """\{width}\""".toRegex()


            //var imageThumbnail = stream.thumbnail_url?.replace(pattern, " XX ")
            //var imageThumbnail = stream.thumbnail_url
            Glide.with(holder.thumbnailUrl.getContext())
                .load(stream.thumbnail_url)
                //.load(imageThumbnail)
                .placeholder(R.drawable.ic_user)
                .apply(RequestOptions().override(600, 200))
                .into(holder.thumbnailUrl);
        }
    }

    // Holds an instance to the view for re-use
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.tvUserName)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val thumbnailUrl: ImageView = view.findViewById(R.id.ivThumbnail)

    }

}

