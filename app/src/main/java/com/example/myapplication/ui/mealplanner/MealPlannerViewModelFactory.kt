import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.db.MealPlanDao
import com.example.myapplication.ui.mealplanner.MealPlannerViewModel

class MealPlannerViewModelFactory(private val mealPlanDao: MealPlanDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealPlannerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealPlannerViewModel(mealPlanDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
