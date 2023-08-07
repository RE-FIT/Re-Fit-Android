package com.example.refit.presentation.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.R
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.databinding.FragmentChatRoomBinding
import com.example.refit.presentation.chat.adapter.ChatRoomRVAdapter
import com.example.refit.presentation.chat.viewmodel.ChatRoomViewModel
import com.example.refit.presentation.common.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val chatRoomViewModel: ChatRoomViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = chatRoomViewModel

        chatRoomViewModel.get_rooms();

        chatRoomViewModel.rooms.observe(viewLifecycleOwner) {
            val dataRVAdapter = ChatRoomRVAdapter(it)
            binding.rv.adapter = dataRVAdapter
            binding.rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            dataRVAdapter.setOnItemClickListener(object: ChatRoomRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ChatRoom, pos: Int) {
                    val action = ChatRoomFragmentDirections.actionChatRoomFragmentToChatFragment(
                        data.username.toString(), data.roomId.toString(), data.other.toString())
                    Navigation.findNavController(view).navigate(action)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }
}