package com.example.assignment.ui.user.favUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.databinding.FragmentUserBinding
import com.example.assignment.db.AppDatabase
import com.example.assignment.db.UserDao
import com.example.assignment.ui.user.UserAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentFavouriteUser : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val adapter = UserAdapter()

        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        binding.titleLayout.tvTitle.text = "Favourite Users"
        GlobalScope.launch(Dispatchers.Main) {
            if (userDao.getFavUsers().isNotEmpty()) {
                adapter.setData(userDao.getFavUsers(),"FragmentFav")
            } else {
                binding.tvNoUser.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}