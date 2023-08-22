package com.example.refit.presentation.chat.adapter

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.refit.data.model.chat.Chat
import com.example.refit.databinding.ItemChattingBinding
import java.time.ZonedDateTime
import java.util.Locale

class ChatRVAdapter(private val dataList: MutableList<Chat>,
                    private val otherImage: String?="", private val context: Context): RecyclerView.Adapter<ChatRVAdapter.DataViewHolder>() {

    fun addChat(chat: Chat) {
        this.dataList.add(chat)
        notifyDataSetChanged()
    }

    fun cancelNotification(notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
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

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                binding.profile.setOnClickListener {
                    listner?.onItemClick(it, otherImage!!, pos)
                }
            }

            resetViews()

            // 현재 메시지가 마지막 메시지가 아닌 경우 다음 메시지 가져오기
            val nextData: Chat? = if (adapterPosition < dataList.size - 1) dataList[adapterPosition + 1] else null

//            cancelNotification(data.notificationId.toInt())

            if (data.isMy === true) {
                handleMyMessage(data, nextData)
            } else {
                handleOtherMessage(data, nextData)
            }
        }

        private fun handleMyMessage(data: Chat, nextData: Chat?) {
            binding.other.isVisible = false
            binding.myChat.text = data.content.trim()
            binding.myTime.text = formatToKoreanTime(data.time)
            // 조건에 따라 현재 메시지의 시간 숨기기
            if (nextData != null && nextData.isMy == true && nextData.username == data.username && formatToKoreanTime(nextData.time) == formatToKoreanTime(data.time)) {
                binding.myTime.isVisible = false
            }
        }

        private fun handleOtherMessage(data: Chat, nextData: Chat?) {
            binding.my.isVisible = false

            if (adapterPosition == 0) {
                imageSet()
                binding.otherName.text = data.username
                binding.otherConnect.isVisible = false
                binding.otherChat.text = data.content.trim()
                binding.otherTime.text = formatToKoreanTime(data.time)
            } else {
                val previousData = dataList[adapterPosition - 1]

                // 메시지를 보낸 사람이 같은 경우
                if (previousData.username == data.username) {
                    binding.otherFirst.isVisible = false
                    binding.otherChatConnect.text = data.content.trim()
                    binding.otherTimeConnect.text = formatToKoreanTime(data.time)

                } else { // 다른 경우
                    imageSet()
                    binding.otherName.text = data.username
                    binding.otherConnect.isVisible = false
                    binding.otherChat.text = data.content.trim()
                    binding.otherTime.text = formatToKoreanTime(data.time)
                }
            }

            // 조건에 따라 현재 메시지의 시간 숨기기
            if (nextData != null && nextData.username == data.username && formatToKoreanTime(nextData.time) == formatToKoreanTime(data.time)) {
                binding.otherTimeConnect.isVisible = false
                binding.otherTime.isVisible = false
            }
        }

        private fun imageSet() {
            Glide.with(binding.root)
                .load(otherImage)
                .into(binding.profile)
        }

        private fun resetViews() {
            binding.other.isVisible = true
            binding.my.isVisible = true

            binding.myChat.isVisible = true
            binding.otherChat.isVisible = true
            binding.otherChatConnect.isVisible = true

            binding.otherConnect.isVisible = true
            binding.otherFirst.isVisible = true
            binding.otherTimeConnect.isVisible = true
            binding.otherTime.isVisible = true
            binding.myTime.isVisible = true
        }
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

    interface OnItemClickListner {
        fun onItemClick(v: View, data: String, pos: Int)
    }
    private var listner: ChatRVAdapter.OnItemClickListner?= null

    fun setOnItemClickListener(listner: ChatRVAdapter.OnItemClickListner) {
        this.listner = listner
    }
}