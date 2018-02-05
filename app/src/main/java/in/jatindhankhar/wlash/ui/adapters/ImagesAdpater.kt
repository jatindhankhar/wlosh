package `in`.jatindhankhar.wlash.ui.adapters

import `in`.jatindhankhar.wlash.R
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_card.view.*

/**
 * Created by jatin on 2/5/18.
 */
class ImagesAdpater(private var mContext: Context) : RecyclerView.Adapter<ImagesAdpater.ViewHolder>() {

    private var mPicasso: Picasso

    init {
        mPicasso = Picasso.with(mContext);
    }

    override fun onBindViewHolder(holder: ImagesAdpater.ViewHolder?, position: Int) {
        holder?.bindItems(mPicasso);
    }

    override fun getItemCount(): Int {
        return 5;
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

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(picasso: Picasso) {
            Log.d("DEBUG","Binding imageview")
            picasso.load("http://i.imgur.com/DvpvklR.png").into(view.image);
        }
    }
}