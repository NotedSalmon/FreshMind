package com.example.freshmind.UI.Calendar

import com.example.freshmind.R
import android.view.ViewGroup
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.UI.BaseFragment
import com.example.freshmind.UI.Calendar.Utils.layoutInflater
import com.example.freshmind.databinding.CalendarViewOptionsItemViewBinding


data class ExampleItem(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    val animation: Animation,
    val createView: () -> BaseFragment,
)

data class Animation(
    @AnimRes val enter: Int,
    @AnimRes val exit: Int,
    @AnimRes val popEnter: Int,
    @AnimRes val popExit: Int,
)

val vertical = Animation(
    enter = com.example.freshmind.R.anim.slide_in_up,
    exit = com.example.freshmind.R.anim.fade_out,
    popEnter = com.example.freshmind.R.anim.fade_in,
    popExit = com.example.freshmind.R.anim.slide_out_down,
)

val horizontal = Animation(
    enter = com.example.freshmind.R.anim.slide_in_right,
    exit = com.example.freshmind.R.anim.slide_out_left,
    popEnter = com.example.freshmind.R.anim.slide_in_left,
    popExit = com.example.freshmind.R.anim.slide_out_right,
)

class CalendarViewOptionsAdapter(val onClick: (ExampleItem) -> BaseFragment) :
    RecyclerView.Adapter<CalendarViewOptionsAdapter.HomeOptionsViewHolder>() {
    val examples = listOf(
        ExampleItem(
            com.example.freshmind.R.string.example_3_title,
            com.example.freshmind.R.string.example_3_subtitle,
            horizontal,
        )  {CalendarFragment()}
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOptionsViewHolder {
        return HomeOptionsViewHolder(
            CalendarViewOptionsItemViewBinding.inflate(
                parent.context.layoutInflater,
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(viewHolder: HomeOptionsViewHolder, position: Int) {
        viewHolder.bind(examples[position])
    }

    override fun getItemCount(): Int = examples.size

    inner class HomeOptionsViewHolder(private val binding: CalendarViewOptionsItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onClick(examples[bindingAdapterPosition])
            }
        }

        fun bind(item: ExampleItem) {
            val context = itemView.context
            binding.itemOptionTitle.text = context.getString(item.titleRes)
            binding.itemOptionSubtitle.text = context.getString(item.subtitleRes)
        }
    }
}
