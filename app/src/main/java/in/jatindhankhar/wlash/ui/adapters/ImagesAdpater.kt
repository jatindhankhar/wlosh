package `in`.jatindhankhar.wlash.ui.adapters

import `in`.jatindhankhar.wlash.R
import `in`.jatindhankhar.wlash.model.Response
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_card.view.*

/**
 * Created by jatin on 2/5/18.
 */
class ImagesAdpater( mContext: Context) : RecyclerView.Adapter<ImagesAdpater.ViewHolder>() {

    private var mPicasso: Picasso = Picasso.with(mContext)
    private var responses: List<Response>? = null

    override fun onBindViewHolder(holder: ImagesAdpater.ViewHolder?, position: Int) {
        val imageUrl:String? = responses?.get(position)?.urls?.small
        holder?.bindItems(mPicasso,imageUrl);
    }

    override fun getItemCount(): Int {
        if(responses != null)
            return responses!!.count()
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.image_card, parent, false);
        return ViewHolder(v);
    }


    /*class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindItems(mPicasso: Picasso)  = with(v){
            mPicasso.load("http://i.imgur.com/DvpvklR.png").into(v.image);
        }
    }*/

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(picasso: Picasso, imageUrl: String?) {
            //picasso.load("http://i.imgur.com/DvpvklR.png").into(view.image);
            imageUrl.let { picasso.load(it).into(view.image) }
        }
    }

    fun loadData(body: List<Response>) {
        this.responses = body
        notifyDataSetChanged()
    }
}