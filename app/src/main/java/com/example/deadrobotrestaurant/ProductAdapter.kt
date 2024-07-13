package com.example.deadrobotrestaurant

import android.content.Intent
import android.graphics.BitmapFactory.Options
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage

class ProductAdapter(options: FirebaseRecyclerOptions<Product>)
    :FirebaseRecyclerAdapter<Product, ProductAdapter.MyViewHolder>(options)
{
    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup):RecyclerView.ViewHolder(inflater.inflate(R.layout.row_layout,parent,false)){
        val txtTitle : TextView = itemView.findViewById(R.id.txtTitle)
        val txtShortDesp : TextView = itemView.findViewById(R.id.txtShortDesp)
        var txtCalories : TextView = itemView.findViewById(R.id.txtCalories)
        val imgPhoto : ImageView = itemView.findViewById(R.id.imgBurger)
        val btnMore: Button = itemView.findViewById(R.id.btnMore)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ProductAdapter.MyViewHolder, position: Int, model: Product) {
        holder.txtTitle.text = model.title
        holder.txtShortDesp.text = model.short_description
        holder.txtCalories.text = "Cal:" + model.calories.toString()
        val theImage : String = model.image

        if(theImage.indexOf("gs://") > -1){
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(theImage)
            Glide.with(holder.imgPhoto.context)
                .load(storageReference)
                .into(holder.imgPhoto)
        }
        else{
            Glide.with(holder.imgPhoto.context)
                .load(theImage)
                .into(holder.imgPhoto)
        }

        holder.btnMore.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("product_id", model.product_id)
                putExtra("title", model.title)
                putExtra("longDescription", model.long_description)
                putExtra("image", model.image)
            }
            context.startActivity(intent)
        }
    }

}