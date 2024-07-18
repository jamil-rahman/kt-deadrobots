package com.example.deadrobotrestaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CartAdapter(options: FirebaseRecyclerOptions<CartItem>, private val userId: String)
    : FirebaseRecyclerAdapter<CartItem, CartAdapter.MyViewHolder>(options) {

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.row_cart, parent, false)) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imgBurger)
        val txtTitle: TextView = itemView.findViewById(R.id.txtBurger)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val btnRemove: Button = itemView.findViewById(R.id.btnSub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: CartItem) {
        holder.txtTitle.text = model.title
        holder.txtQuantity.text = "Quantity: ${model.quantity}"
        holder.txtPrice.text = "Total Price: C\$ ${model.price}"

        val theImage: String = model.image

        if (theImage.contains("gs://")) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(theImage)
            Glide.with(holder.imgPhoto.context)
                .load(storageReference)
                .into(holder.imgPhoto)
        } else {
            Glide.with(holder.imgPhoto.context)
                .load(theImage)
                .into(holder.imgPhoto)
        }

        // FUNC Remove
        holder.btnRemove.setOnClickListener {
            // Get the item key
            val itemKey = getRef(position).key
            if (itemKey != null) {
                val itemRef = FirebaseDatabase.getInstance().reference
                    .child("cart")
                    .child(userId)
                    .child(itemKey)
                itemRef.removeValue()
            }
        }
    }
}
