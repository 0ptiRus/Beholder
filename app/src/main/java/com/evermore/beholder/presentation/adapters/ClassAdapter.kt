import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.ItemClassEntryBinding
import com.evermore.beholder.presentation.classes.ClassEntry

class ClassAdapter(
    private val items: List<ClassEntry>,
    private val onItemClick: (ClassEntry) -> Unit
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(val binding: ItemClassEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: ClassEntry) {
            binding.className.text = entry.name
            binding.className.setCompoundDrawablesRelativeWithIntrinsicBounds(
                entry.iconResId,
                0,
                0,
                0
            )
            binding.root.setOnClickListener { onItemClick(entry) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClassEntryBinding.inflate(inflater, parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
