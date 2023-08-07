package com.example.refit.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.chat.Chat
import com.example.refit.databinding.ItemChattingBinding

class ChatRVAdapter(private val dataList: MutableList<Chat>): RecyclerView.Adapter<ChatRVAdapter.DataViewHolder>() {

    fun addChat(chat: Chat) {
        this.dataList.add(chat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = ItemChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class DataViewHolder(private val binding: ItemChattingBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Chat) {

            // 뷰를 초기화합니다.
            binding.other.isVisible = true
            binding.my.isVisible = true
            binding.otherConnect.isVisible = true
            binding.otherFirst.isVisible = true

            //내 글일 경우
            if (data.isMy == true) {
                binding.other.isVisible = false
                binding.myChat.text = data.content
                binding.myTime.text = data.time
            } else {
                binding.my.isVisible = false
                binding.otherName.text = data.username
                if (position == 0) {
                    binding.otherConnect.isVisible = false
                    binding.otherChat.text = data.content
                    binding.otherTime.text = data.time
                } else {
                    val previousData = dataList[position - 1]

                    //메시지를 보낸 사람이 같은 경우
                    if (previousData.username == data.username) {

                        binding.otherFirst.isVisible = false
                        binding.otherChatConnect.text = data.content
                        binding.otherTimeConnect.text = data.time
                    } else { //다른 경우

                        binding.otherConnect.isVisible = false
                        binding.otherChat.text = data.content
                        binding.otherTime.text = data.time
                    }
                }
            }
        }
    }
}