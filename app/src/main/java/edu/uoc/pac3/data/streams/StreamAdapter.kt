package edu.uoc.pac3.data.streams

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        }
        if (stream != null) {
            holder.title.text = stream.title
        }
    }

    // Holds an instance to the view for re-use
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.tvUserName)
        val title: TextView = view.findViewById(R.id.tvTitle)


    }

}

