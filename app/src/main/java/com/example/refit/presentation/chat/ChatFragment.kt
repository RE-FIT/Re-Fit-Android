package com.example.refit.presentation.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.BuildConfig
import com.example.refit.R
import com.example.refit.data.model.chat.Chat
import com.example.refit.databinding.FragmentChatBinding
import com.example.refit.presentation.chat.adapter.ChatRVAdapter
import com.example.refit.presentation.chat.viewmodel.ChatViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.time.ZonedDateTime
import java.util.Locale


class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private lateinit var socket: Socket
    private val viewModel: ChatViewModel by sharedViewModel()

    val args: ChatFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = args.userId.toString()
        val roomId = args.roomId.toString()
        val sellerId = args.sellerId.toString()

        viewModel.setUserStatus(userId, sellerId)

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }


        viewModel.room_detail(roomId)

        viewModel.delete.observe(viewLifecycleOwner) {
            if (it === true) {
                viewModel.initDelete()
                findNavController().popBackStack()
            }
        }

        viewModel.chats.observe(viewLifecycleOwner) {

            val dataRVAdapter = ChatRVAdapter(it.toMutableList())
            binding.rv.adapter = dataRVAdapter
            binding.rv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            socket = IO.socket(BuildConfig.SUB_URL)
            socket.connect()

            chatEtcMenuDropdown()

            socket.on(Socket.EVENT_CONNECT, Emitter.Listener {

                //채팅방에 조인
                socket.emit("joinRoom", roomId, userId)

                //메시지 이벤트를 받으면 진행
                socket.on("message", Emitter.Listener { args ->

                    activity?.runOnUiThread {

                        val data = args[0] as JSONObject
                        val content = data.getString("content")
                        val username = data.getString("username")
                        val time = data.getString("time")

                        val isMy = username == userId

                        val newMessage = Chat(content, username, time, isMy)
                        dataRVAdapter.addChat(newMessage)
                        binding.rv.scrollToPosition(dataRVAdapter.getItemCount() - 1)
                    }
                })
            })

            //작성 누를 시 글 작성
            binding.writeButton.setOnClickListener {
                sendMessage(binding.edit.text.toString().trim())
                binding.edit.text.clear()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        viewModel.room_detail(args.roomId.toString())
    }

    fun sendMessage(message: String) {
        if (message != "") {
            socket.emit(
                "message",
                args.roomId.toString(),
                args.userId.toString(),
                args.otherId.toString(),
                message
            )
        }
    }

    override fun onPause() {
        super.onPause()

        socket.emit("leaveRoom", args.roomId.toString(), args.userId.toString())

        if (socket.connected()) {
            socket.disconnect()
        }
    }


    private fun chatEtcMenuDropdown() {
        binding.out.setOnClickListener {
            val listPopupWindow = when (viewModel.userStatus.value) {
                0 -> getPopupMenu( // 판매자
                    it,
                    R.array.chat_overflow_seller
                )

                1 -> getPopupMenu( // 구매 희망자
                    it,
                    R.array.chat_overflow_user
                )

                else -> throw IllegalArgumentException("Invalid Status Value")
            }
            setPopupItemClickListener(listPopupWindow)
            listPopupWindow.show()
        }
    }

    private fun getPopupMenu(
        anchorView: View,
        @ArrayRes items: Int,
    ): ListPopupWindow {
        return DropdownMenuManager.createPopupMenu(
            anchorView,
            R.style.ListPopupMenuWindow_CommunityInfoOption,
            R.layout.list_popup_window_item_white,
            items
        )
    }

    private fun setPopupItemClickListener(popupMenu: ListPopupWindow) {
        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            when (itemDescription) {
                "채팅방 나가기" -> {
                    viewModel.roomId.value?.let { viewModel.room_delete(it) }
                }

                "거래 완료" -> {

                }
            }
            Timber.d(itemDescription)
            popupMenu.dismiss()
        }
    }

}
