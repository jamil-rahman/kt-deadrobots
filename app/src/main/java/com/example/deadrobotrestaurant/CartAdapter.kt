package com.example.deadrobotrestaurant

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage

class CartAdapter(options: FirebaseRecyclerOptions<CartItem>)
    : FirebaseRecyclerAdapter<CartItem, CartAdapter.MyViewHolder>(options) {

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.row_cart, parent, false)) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imgBurger)
        val txtTitle: TextView = itemView.findViewById(R.id.txtBurger)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        val txtPrice : TextView = itemView.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int, model: CartItem) {
        holder.txtTitle.text = model.title
        Log.d("tofo", "userId: userid"+model)
        holder.txtQuantity.text = "Quantity: ${model.quantity}"
        holder.txtPrice.text = "Price: \$${model.price}"

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
    }
}