package com.application.gosporttest.views

import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.gosporttest.adapters.DataGoSportAdapter
import com.application.gosporttest.R
import com.application.gosporttest.databinding.FragmentHomeBinding
import com.application.gosporttest.room.Category
import com.application.gosporttest.viewmodels.GoSportViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GoSportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GoSportViewModel::class.java]

        val adapter = DataGoSportAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.dataList.observe(viewLifecycleOwner, Observer { data ->
            if (data.size==0){
                binding.imageEmpty.visibility = View.VISIBLE
            }else{
                binding.imageEmpty.visibility = View.GONE
            }
            adapter.submitList(data)
        })

        viewModel.listCategories.observe(viewLifecycleOwner, Observer { category ->
            createTextView(category)
        })

        viewModel.startWork()
    }

    private fun createTextView(listCategories:List<Category>){
        val textViewsList = mutableListOf<TextView>()
        for (i in listCategories.indices) {
            val textView = TextView(requireActivity())

            textView.id = View.generateViewId()
            textView.layoutParams = LinearLayout.LayoutParams(
                88.dpToPx(),
                32.dpToPx()
            )
            textView.gravity = Gravity.CENTER
            textView.text = listCategories[i].name
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            textView.setTypeface(null, Typeface.BOLD)
            textView.elevation = 3.dpToPxNew()
            textView.setTextColor(ContextCompat.getColor(requireActivity(),
                R.color.category_unselect_text
            ))
            textView.setBackgroundResource(R.drawable.back_category_unselect)

            val marginParams = textView.layoutParams as ViewGroup.MarginLayoutParams
            marginParams.marginStart = if (i == 0) 16.dpToPx() else 8.dpToPx()
            marginParams.marginEnd = if (i == listCategories.size-1) 16.dpToPx() else 0
            textView.layoutParams = marginParams
            textViewsList.add(textView)
            textView.setOnClickListener {
                setButtons(i, textViewsList)
                viewModel.showNecessaryData(i)
            }

            binding.layoutCategories.addView(textView)
        }
        setButtons(0, textViewsList)
    }

    private fun Int.dpToPxNew(): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return this * scale
    }

    private fun Int.dpToPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    private fun setButtons(num:Int,  textViewsList:MutableList<TextView>){
        for ((index, textView) in textViewsList.withIndex()) {
            if (index==num){
                textView.setTextColor(ContextCompat.getColor(requireActivity(),
                    R.color.category_select_text
                ))
                textView.setBackgroundResource(R.drawable.back_category_select)
            }else{
                textView.setTextColor(ContextCompat.getColor(requireActivity(),
                    R.color.category_unselect_text
                ))
                textView.setBackgroundResource(R.drawable.back_category_unselect)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}