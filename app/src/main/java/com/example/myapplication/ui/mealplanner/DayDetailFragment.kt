package com.example.myapplication.ui.mealplanner

import MealPlannerViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentDayDetailBinding
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DayDetailFragment : Fragment() {

    private var _binding: FragmentDayDetailBinding? = null
    private val binding get() = _binding!!

    private val mealPlannerViewModel: MealPlannerViewModel by activityViewModels {
        val mealPlanDao = AppDatabase.getDatabase(requireContext()).mealPlanDao()
        MealPlannerViewModelFactory(mealPlanDao)
    }

    private lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = arguments?.getString("date") ?: return
        binding.dateTextView.text = date

        setupRecyclerView()

        // Observe meals for the selected date
        mealPlannerViewModel.getMealsForDate(date).observe(viewLifecycleOwner) { meals ->
            mealAdapter.updateMeals(meals)
        }
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter(emptyList())
        binding.mealsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mealAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
