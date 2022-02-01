package com.example.mappaingredienti

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptRicetta internal constructor(context: Context?, data: List<Ricetta>) :
        RecyclerView.Adapter<AdaptRicetta.ViewHolder>() {
    private val mData: List<Ricetta>
    private val mInflater: LayoutInflater
    private var mClickListener: ItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.ingredient_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe: Ricetta = mData[position]
        holder.textViewFoodName.setText(
                recipe.getName().substring(0, 1).toUpperCase().concat(recipe.getName().substring(1))
        )
        holder.textViewDifficulty.setText(
                recipe.getDescription().substring(0, 1).toUpperCase()
                        .concat(recipe.getDescription().substring(1))
        )
        Picasso.get().load(recipe.getImg()).into(holder.imageviewFoodItem)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    //Definisco elementi template
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        var textViewFoodName: TextView
        var textViewDifficulty: TextView
        var imageviewFoodItem: ImageView
        override fun onClick(v: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(v, adapterPosition)
        }

        init {
            textViewFoodName = itemView.findViewById(R.id.textView_foodname)
            textViewDifficulty = itemView.findViewById(R.id.textView_difficulty)
            imageviewFoodItem = itemView.findViewById(R.id.imageview_fooditem)
            itemView.setOnClickListener(this)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    init {
        mInflater = LayoutInflater.from(context)
        mData = data
    }
}
