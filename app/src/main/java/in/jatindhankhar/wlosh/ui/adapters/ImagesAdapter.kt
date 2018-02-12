package `in`.jatindhankhar.wlosh.ui.adapters

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.model.Response
import `in`.jatindhankhar.wlosh.ui.listeners.ImageItemClickListener
import `in`.jatindhankhar.wlosh.utils.Constants.GRID_VIEW
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_card.view.*
import kotlinx.android.synthetic.main.image_card_landscape.view.*
import kotlinx.android.synthetic.main.uploader_info_view.view.*

/**
 * Created by jatin on 2/5/18.
 */
class ImagesAdapter(private var mContext: Context, private var mRecyclerView: RecyclerView?,private var mImageItemClickListener: ImageItemClickListener) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private var mPicasso: Picasso = Picasso.with(mContext)
    private var responses: MutableList<Response>? = null
    private var lastPosition: Int = -1


    override fun onBindViewHolder(holder: ImagesAdapter.ViewHolder?, position: Int) {
        val response = responses?.get(position)
        holder?.bindItems( mPicasso, responses?.get(position))
        holder?.itemView?.setOnClickListener{ response?.let { it1 -> mImageItemClickListener.OnItemClick(it1) } }
         setAnimation(holder?.itemView,position)
    }

    override fun getItemCount(): Int {
        if (responses != null)
            return responses!!.count()
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        return (mRecyclerView?.layoutManager as GridLayoutManager).spanCount
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        this.mRecyclerView = recyclerView

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layout: Int = if (viewType == GRID_VIEW)
            R.layout.image_card
        else
            R.layout.image_card_landscape
        val v = LayoutInflater.from(parent?.context)
                .inflate(layout, parent, false)
        return ViewHolder(v,viewType)
    }


    class ViewHolder(private val view: View,private val viewType: Int) : RecyclerView.ViewHolder(view) {


        fun bindItems(picasso: Picasso,response: Response?) {
            view
          //  view.setOnClickListener(ImagesAdapter.)
            if(viewType == GRID_VIEW) {
                response?.urls?.small.let { picasso.load(it).into(view.image) }

            }
            else
                {
                    response?.urls?.small.let { picasso.load(it).into(view.image_landscape) }
                    response?.user?.name.let { view.user_name.text = it }
                    response?.user?.profileImage?.medium.let { picasso.load(it).into(view.user_profile);  }
                }
            }

        }



    fun appendData(body: List<Response>) {
        if (this.responses == null) {
            this.responses = body.toMutableList()
        } else {
            this.responses?.addAll(body)
        }
        notifyDataSetChanged()
    }

    fun loadData(body: List<Response>) {
        this.responses = body.toMutableList()
        notifyDataSetChanged()
    }

    private fun setAnimation(targetView: View?, position: Int) {

        if (targetView != null) {
            Essentials.setslideUpAnimation(targetView, 350L)
        }
        lastPosition = position

    }

    override fun onViewDetachedFromWindow(holder: ImagesAdapter.ViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        holder?.itemView?.clearAnimation()
    }

    interface ImagesAdapterCallBack {
        fun changeStatusBarColor(color:Int)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}