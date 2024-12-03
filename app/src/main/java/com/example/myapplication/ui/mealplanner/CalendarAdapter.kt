import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.DayPlan

class CalendarAdapter(
    private var plans: List<DayPlan>,
    private val onDayClick: (String) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.dateText)
        val mealsText: TextView = view.findViewById(R.id.mealsText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val plan = plans[position]
        holder.dateText.text = plan.date
        holder.mealsText.text = plan.meals.joinToString("\n") { it.mealType }

        holder.itemView.setOnClickListener {
            onDayClick(plan.date)
        }
    }

    override fun getItemCount() = plans.size

    fun updatePlans(newPlans: List<DayPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }
}
