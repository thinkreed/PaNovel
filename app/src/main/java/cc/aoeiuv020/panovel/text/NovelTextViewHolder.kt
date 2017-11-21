package cc.aoeiuv020.panovel.text

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import cc.aoeiuv020.panovel.IView
import cc.aoeiuv020.panovel.R
import cc.aoeiuv020.panovel.api.NovelChapter
import cc.aoeiuv020.panovel.api.NovelText
import cc.aoeiuv020.panovel.local.Settings
import cc.aoeiuv020.panovel.util.hide
import cc.aoeiuv020.panovel.util.show
import kotlinx.android.synthetic.main.novel_text_page_item.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class NovelTextViewHolder(private val ctx: NovelTextActivity, private val presenter: NovelTextPresenter.NTPresenter) : IView, AnkoLogger {
    val itemView: View = View.inflate(ctx, R.layout.novel_text_page_item, null)
    var position: Int = 0
    private val textRecyclerView = itemView.textRecyclerView
    private val layoutManager: LinearLayoutManager = LinearLayoutManager(ctx)
    private val progressBar: ProgressBar = itemView.progressBar
    private val textListAdapter = NovelTextRecyclerAdapter(ctx)
    private var textProgress: Int? = null

    init {
        textRecyclerView.layoutManager = layoutManager
        textRecyclerView.setOnTouchListener(object : View.OnTouchListener {
            private var previousAction: Int = MotionEvent.ACTION_UP
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (previousAction == MotionEvent.ACTION_DOWN
                        && event.action == MotionEvent.ACTION_UP) {
                    ctx.toggle()
                }
                previousAction = event.action
                return false
            }
        })
        textRecyclerView.adapter = textListAdapter
        // itemView可能没有初始化高度，所以用decorView,
        // 更靠谱的是GlobalOnLayoutListener，但要求api >= 16,
        textRecyclerView.apply {
            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(leftMargin,
                        Settings.topSpacing.run { (toFloat() / 100 * ctx.window.decorView.height).toInt() },
                        rightMargin,
                        Settings.bottomSpacing.run { (toFloat() / 100 * ctx.window.decorView.height).toInt() })
            }
        }
    }

    fun apply(chapter: NovelChapter) {
        progressBar.show()
        textListAdapter.clear()
        textListAdapter.setChapterName(chapter.name)
        presenter.attach(this)
        presenter.requestNovelText(chapter)
    }

    fun destroy() {
        presenter.detach()
    }

    fun showText(novelText: NovelText) {
        textListAdapter.data = novelText.textList
        textProgress?.let {
            textRecyclerView.run {
                post { scrollToPosition(it) }
            }
            textProgress = null
        }
        progressBar.hide()
    }

    fun showError(message: String, e: Throwable) {
        itemView.progressBar.hide()
        ctx.showError(message, e)
    }

    fun refreshCurrentChapter() {
        progressBar.show()
        presenter.refresh()
    }

    fun setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
        textRecyclerView.apply {
            post {
                layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    setMargins(leftMargin,
                            top?.run { (toFloat() / 100 * itemView.height).toInt() } ?: topMargin,
                            rightMargin,
                            bottom?.run { (toFloat() / 100 * itemView.height).toInt() } ?: bottomMargin)
                }
            }
        }
        textListAdapter.setMargins(left, right)
    }

    fun setTextSize(size: Int) {
        textListAdapter.setTextSize(size)
    }

    fun setLineSpacing(size: Int) {
        textListAdapter.setLineSpacing(size)
    }

    fun setParagraphSpacing(size: Int) {
        textListAdapter.setParagraphSpacing(size)
    }

    fun setTextColor(color: Int) {
        textListAdapter.setTextColor(color)
    }

    fun setTextProgress(textProgress: Int) {
        debug { "setTextProgress $textProgress" }
        // 存起来，recyclerView可能还没得到数据，
        this.textProgress = textProgress
        textRecyclerView.scrollToPosition(textProgress)
    }

    fun getTextProgress(): Int {
        return layoutManager.findLastVisibleItemPosition().also {
            debug { "getTextProgress $it" }
        }
    }

    fun getTextCount(): Int = textListAdapter.itemCount
}