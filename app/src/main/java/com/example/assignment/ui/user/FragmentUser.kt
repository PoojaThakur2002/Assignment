package com.example.assignment.ui.user

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.ApplicationAssignment
import com.example.assignment.R
import com.example.assignment.databinding.FragmentUserBinding
import com.example.assignment.repository.UserViewModelFactory
import com.example.assignment.db.AppDatabase
import com.example.assignment.db.UserDao
import com.example.assignment.network.ApiResponse
import com.example.assignment.ui.models.UserDataItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragmentUser : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: UserViewModel
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
        binding.titleLayout.tvTitle.text = "Users"
        val repository = (requireActivity().application as ApplicationAssignment).userRepository
        viewModel = ViewModelProvider(
            this, UserViewModelFactory(repository)
        )[UserViewModel::class.java]
        userDao = AppDatabase.getDatabase(requireContext()).userDao()

        val adapter = UserAdapter()
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    viewModel.getUsers()
                }
            }
        })

        viewModel.userData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (adapter.itemCount == 0){
                        adapter.setData(response.data,"")
                    } else {
                        adapter.appendData(response.data)
                    }
                }
                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        if (viewModel.userData.value == null) {
            viewModel.getUsers()
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                adapter.notifyItemChanged(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val position = viewHolder.adapterPosition
                val isSwiping = actionState == ItemTouchHelper.ACTION_STATE_SWIPE

                val userViewHolder =
                    recyclerView.findViewHolderForAdapterPosition(position) as? UserAdapter.ViewHolder

                userViewHolder?.let {
                    val user = adapter.getUser(it.adapterPosition)
                    var isUserMatched = false

                    GlobalScope.launch(Dispatchers.Main) {
                        val favUserList = userDao.getFavUsers()
                        val isUserFoundInFav = favUserList.firstOrNull {
                            it.email == user.email
                                    && it.id == user.id
                        }
                        isUserMatched = isUserFoundInFav != null
                        it.binding.tvFavorite.text =
                            if (isUserFoundInFav != null) "Remove Favourite" else "Add Favourite"

                        it.binding.tvSwipeLeft.visibility =
                            if (isSwiping) View.GONE else View.VISIBLE
                        it.binding.tvFavorite.visibility =
                            if (isSwiping) View.VISIBLE else View.GONE
                    }

                    it.binding.tvFavorite.setOnClickListener { view ->
                        GlobalScope.launch(Dispatchers.Main) {
                            if (!user.isFavorite && !isUserMatched) {
                                try {
                                    user.isFavorite = true
                                    userDao.insertFavUser(user)
                                    showSnackbar(recyclerView, "${user.name} Added as Favourite")
                                } catch (e: SQLiteConstraintException) {
                                    Log.e("INSERT ERROR", "Primary key conflict: ${e.message}")
                                }
                            } else {
                                user.isFavorite = false
                                userDao.deleteUser(user)
                                showSnackbar(recyclerView, "${user.name} Removed From Favourite")
                            }

                            adapter.notifyItemChanged(position)
                            recyclerView.postDelayed({
                                it.binding.tvFavorite.visibility = View.GONE
                                it.binding.tvSwipeLeft.visibility = View.VISIBLE
                                adapter.notifyItemChanged(position)
                            }, 1000)
                        }
                    }

                }

            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvUsers)
    }

    private fun showSnackbar(recyclerView: RecyclerView, message: String) {
        val snackbar = Snackbar.make(
            recyclerView,
            message,
            Snackbar.LENGTH_SHORT
        )
        val snackbarView = snackbar.view
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_members, 0, 0, 0)
        snackbar.show()
    }
}