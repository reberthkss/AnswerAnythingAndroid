package com.answer.anything.fragments.questions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.answer.anything.data.ResearchQuestion

class QuestionsViewPager(fragmentActivity: Fragment, val questions: List<ResearchQuestion>?) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = questions?.size ?: 0
    override fun createFragment(position: Int): Fragment = QuestionsFragmentHolder(questions?.get(position) ?: null, position == this.getItemCount()-1)
}