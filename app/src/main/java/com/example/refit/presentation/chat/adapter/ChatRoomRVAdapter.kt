package com.example.refit.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.databinding.ItemChatRoomBinding
import java.time.ZonedDateTime
import java.util.Locale

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

            binding.itemHolder.isVisible = true
            if (data.remain == 0) { binding.remain.isVisible = false }

            binding.name.text = data.other
            binding.remain.text = data.remain.toString()

            data.message?.let {
                binding.content.text = data.message
                binding.time.text = formatToKoreanTime(data.time)
            }

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

    fun formatToKoreanTime(time: String): String {
        val dateTime = ZonedDateTime.parse(time)
        val adjustedDateTime = dateTime.plusHours(9)

        val hour = adjustedDateTime.hour
        val minute = adjustedDateTime.minute

        val period = if (hour < 12) "오전" else "오후"
        val hourIn12Format = if (hour > 12) hour - 12 else hour

        return String.format(Locale.getDefault(), "%s %02d:%02d", period, hourIn12Format, minute)
    }
}