package com.example.refit.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.databinding.ItemChatRoomBinding

class ChatRoomRVAdapter(private val dataList: List<ChatRoom>): RecyclerView.Adapter<ChatRoomRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = ItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class DataViewHolder(private val binding: ItemChatRoomBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChatRoom) {

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, data, pos)
                }
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: ChatRoom, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}