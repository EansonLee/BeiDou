package com.module.book.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.book.adapter.BookAdapter
import com.module.book.data.BookDatabase
import com.module.book.databinding.FragmentHomeBinding
import com.module.book.model.Book
import com.module.book.activity.BookDetailActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        bookAdapter = BookAdapter { book ->
            // 处理点击事件，跳转到图书详情页面
            navigateToBookDetail(book)
        }
        binding.rvBooks.layoutManager = LinearLayoutManager(context)
        binding.rvBooks.adapter = bookAdapter
    }

    private fun initData() {
        lifecycleScope.launch {
            val bookDao = BookDatabase.getDatabase(requireContext()).bookDao()
            val books = bookDao.getAllBooks()
            bookAdapter.submitList(books)
        }
    }

    private fun navigateToBookDetail(book: Book) {
        val intent = Intent(requireContext(), BookDetailActivity::class.java).apply {
            putExtra("book", book)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}