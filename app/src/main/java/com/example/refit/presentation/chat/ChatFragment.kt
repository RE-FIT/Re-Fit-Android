package com.example.refit.presentation.chat

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.BuildConfig
import com.example.refit.R
import com.example.refit.data.model.chat.Chat
import com.example.refit.data.model.chat.Trade
import com.example.refit.databinding.FragmentChatBinding
import com.example.refit.presentation.chat.adapter.ChatRVAdapter
import com.example.refit.presentation.chat.viewmodel.ChatViewModel
import com.example.refit.presentation.chat.viewmodel.TradeViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.showChatConfirmDialog
import com.example.refit.presentation.common.DialogUtil.showChatDeletionConfirmDialog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import kotlin.random.Random


class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private lateinit var socket: Socket
    private val viewModel: ChatViewModel by sharedViewModel()
    private val tradeViewModel: TradeViewModel by sharedViewModel()

    val args: ChatFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        val userId = args.userId.toString()
        val roomId = args.roomId.toString()
        val sellerId = args.sellerId.toString()
        val otherImage = args.otherImage.toString()

        binding.name.text = args.otherId.toString()

        viewModel.setUserStatus(userId, sellerId, args.postState)

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

            val dataRVAdapter = ChatRVAdapter(it.toMutableList(), otherImage, requireContext())
            binding.rv.adapter = dataRVAdapter
            binding.rv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            dataRVAdapter.setOnItemClickListener(object: ChatRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: String, pos: Int) {
                    if (data != BuildConfig.IMAGE_URL) {
                        val action = ChatFragmentDirections.actionChatFragmentToImageFragment(data)
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            })

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
                        val id = data.getString("notificationId")

                        val isMy = username == userId

                        val newMessage = Chat(content, username, time, isMy, id)
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
                message,
                Random.nextInt(0, 1000001).toString()
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
                    showChatDeletionConfirmDialog()
                }

                "거래 완료" -> {
                    showChatConfirmDialog()
                }
            }
            Timber.d(itemDescription)
            popupMenu.dismiss()
        }
    }

    private fun showChatDeletionConfirmDialog() {
        showChatDeletionConfirmDialog(
            resources.getString(R.string.chat_dialog_chat_delete_title),
            resources.getString(R.string.chat_dialog_chat_delete_subTtle),
            resources.getString(R.string.chat_dialog_chat_delete_positive),
            resources.getString(R.string.chat_dialog_chat_delete_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    viewModel.room_delete(args.roomId.toString())
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun showChatConfirmDialog() {
        showChatConfirmDialog(
            resources.getString(R.string.chat_dialog_chat_confirm_title),
            resources.getString(R.string.chat_dialog_chat_confirm_subTitle),
            resources.getString(R.string.chat_dialog_chat_confirm_positive),
            resources.getString(R.string.chat_dialog_chat_confirm_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    tradeViewModel.trade(Trade(args.postId, args.otherId.toString()))
                    viewModel.changeStatus()
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.init()
    }
}
