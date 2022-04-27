package org.sopt.soptseminar.presentation.github.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.soptseminar.R
import org.sopt.soptseminar.base.BaseFragment
import org.sopt.soptseminar.databinding.FragmentRepositoryBinding
import org.sopt.soptseminar.models.RepositoryInfo
import org.sopt.soptseminar.presentation.github.adapters.RepositoryListAdapter
import org.sopt.soptseminar.presentation.home.ProfileViewModel
import org.sopt.soptseminar.util.ItemTouchHelperCallback

@AndroidEntryPoint
class RepositoryFragment : BaseFragment<FragmentRepositoryBinding>(R.layout.fragment_repository),
    RepositoryListAdapter.OnItemClickListener,
    RepositoryListAdapter.OnItemTouchListener {
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var adapter: RepositoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        addListeners()
    }

    private fun initLayout() {
        adapter = RepositoryListAdapter().apply {
            binding.repositoryList.adapter = this
            setOnItemClickListener(this@RepositoryFragment)
            setOnItemTouchListener(this@RepositoryFragment)
            ItemTouchHelper(ItemTouchHelperCallback(this)).attachToRecyclerView(binding.repositoryList)
        }
    }

    private fun addListeners() {
        viewModel.getRepositories().observe(viewLifecycleOwner) {
            if (it == null) return@observe
            adapter.submitList(it.toMutableList())
        }
    }

    override fun onItemClick(item: RepositoryInfo) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        viewModel.moveRepository(fromPosition, toPosition)
    }

    override fun onItemSwipe(position: Int) {
        viewModel.removeRepository(position)
    }

    companion object {
        private const val TAG = "RepositoryFragment"
    }
}