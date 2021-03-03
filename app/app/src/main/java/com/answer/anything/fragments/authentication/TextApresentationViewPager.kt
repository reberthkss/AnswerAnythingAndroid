package com.answer.anything.fragments.authentication

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.answer.anything.data.ResearchQuestion

class TextApresentationViewPager(fragmentActivity: Fragment, val texts: List<String>) : FragmentStateAdapter(fragmentActivity) {
    val TAG = "TextApresentationVP"
    override fun getItemCount(): Int = texts.size

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "Position => ${position} - Text => ${texts[position]}")
        return TextViewHolder(texts[position]);
    }


}