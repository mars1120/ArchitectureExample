package tw.sonet.picktime.ui.eventlist.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marschen.architectureexample.DramaInfoActivity
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

        fun bind(dramaData: Drama) {
            itemView.drama_name.text = dramaData.name
            Glide.with(itemView.context).load(dramaData.thumb).into(itemView.drama_thumbnail)
            itemView.drama_score.text =
                itemView.resources.getString(R.string.score, "" + dramaData.rating)
            itemView.drama_created_at.text = dramaData.created_at
            itemView.drama_cardview.setOnClickListener {
                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (itemView.context as Activity),
                        itemView.drama_thumbnail,
                        "profile"
                    )
                val intent = DramaInfoActivity.intentFor(itemView.context, dramaData)
                itemView.context.startActivity(intent, options.toBundle())
            }

        }
    }


}