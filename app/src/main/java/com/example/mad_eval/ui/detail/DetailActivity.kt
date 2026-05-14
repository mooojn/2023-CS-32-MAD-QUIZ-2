package com.example.mad_eval.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mad_eval.R
import com.example.mad_eval.data.model.Article
import com.example.mad_eval.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val article = intent.getParcelableExtra<Article>(EXTRA_ARTICLE)
        if (article == null) {
            finish()
            return
        }

        bindArticle(article)
    }

    private fun bindArticle(article: Article) {
        Glide.with(this)
            .load(article.image)
            .placeholder(R.drawable.placeholder_news)
            .error(R.drawable.placeholder_news)
            .centerCrop()
            .into(binding.imageDetail)

        binding.textDetailTitle.text = article.title
        binding.textDetailSourceDate.text = getString(
            R.string.source_date,
            article.source.name,
            formatDate(article.publishedAt)
        )
        binding.textDetailDescription.text = article.description ?: getString(R.string.not_available)
        binding.textDetailContent.text = article.content ?: getString(R.string.not_available)

        binding.buttonReadFull.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(browserIntent)
        }
    }

    private fun formatDate(isoDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val parsedDate = inputFormat.parse(isoDate)
            parsedDate?.let { outputFormat.format(it) } ?: isoDate
        } catch (exception: Exception) {
            isoDate
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}
