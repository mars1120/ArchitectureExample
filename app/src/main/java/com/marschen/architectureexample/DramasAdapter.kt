package tw.sonet.picktime.ui.eventlist.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marschen.architectureexample.R
import com.marschen.architectureexample.db.Drama
import kotlinx.android.synthetic.main.drama_get_item.view.*

class DramasAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dramaList: List<Drama> = emptyList()


    fun update(updateList: List<Drama>) {
        dramaList = updateList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return DramaListHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.drama_get_item, parent, false)
        )

    }


    override fun getItemCount() = dramaList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is DramaListHolder -> {
                holder.bind(dramaList[position])
            }

        }
    }


    class DramaListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dramaObj: Drama) {
            itemView.drama_name.text = dramaObj.name

            Glide.with(itemView.context).load(dramaObj.thumb).into(itemView.drama_thumbnail)
            itemView.drama_score.text =
                itemView.resources.getString(R.string.score, "" + dramaObj.rating)
            itemView.drama_created_at.text = dramaObj.created_at

        }
    }


}