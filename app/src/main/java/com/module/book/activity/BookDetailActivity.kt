package com.module.book.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.module.book.databinding.ActivityBookDetailBinding
import com.module.book.model.Book

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取传递的图书信息并显示
        val book = intent.getParcelableExtra<Book>("book")
        book?.let {
            // 显示图书详细信息
            binding.tvBookTitle.text = it.title
            binding.tvBookAuthor.text = it.author
            // 其他信息
        }
    }
} 