package com.example.assignment.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.databinding.ItemUserBinding
import com.example.assignment.ui.models.UserDataItem

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var usersList: MutableList<UserDataItem> = mutableListOf()
    private var type = ""

    fun setData(list: List<UserDataItem>,type:String) {
        usersList.clear()
        this.type = type
        usersList.addAll(list.toMutableList())
        notifyDataSetChanged()
    }

    fun appendData(newItems: List<UserDataItem>) {
        val startPosition = usersList.size
        usersList.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun getUser(position: Int): UserDataItem {
        return usersList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = usersList[position]
        holder.bind(userData,type)

    }

    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: UserDataItem, type: String) {
            binding.tvName.text =  userData.name
            binding.tvGender.text = userData.gender
            binding.tvFavorite.visibility = View.GONE

            if (userData.status == "active") {
                binding.ivStatusIcon.setImageResource(R.drawable.ic_active)
            } else {
                binding.ivStatusIcon.setImageResource(R.drawable.ic_inactive)
            }

            if (type.isNotEmpty()){
                binding.tvSwipeLeft.visibility = View.GONE
            }else{
                binding.tvSwipeLeft.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }


}
