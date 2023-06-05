package com.theflexproject.thunder.fragments.homeNew
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theflexproject.thunder.R
import com.theflexproject.thunder.databinding.MediaItemBinding
import com.theflexproject.thunder.databinding.ParentRecyclerBinding
import com.theflexproject.thunder.model.MyMedia

class ParentAdapter(private val onItemClick : (Int,Boolean) -> Unit) : RecyclerView.Adapter<ParentAdapter.ViewHolder>(){

    private val parents : MutableList<ParentModel> = mutableListOf()

    fun setData(data : List<ParentModel>){
        parents.clear()
        parents.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ParentRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val v = LayoutInflater.from(parent.context).inflate(R.layout.parent_recycler,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return parents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parents[position]
        holder.bind(parent)
    }


    inner class ViewHolder(private val binding: ParentRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        fun  bind (parent: ParentModel){
            if(parent.children.isEmpty()){
                binding.textView.visibility = View.GONE
                binding.rvChild.visibility = View.GONE
                return
            }
            binding.textView.text = parent.title
            binding.rvChild.apply {
                layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
                var shouldBeBanner = false
                if(parent.title == "Recently Added" || parent.title == "Recently Released TV Shows"){
                    shouldBeBanner = true
                }
                val mAdapter = ChildAdapter(shouldBeBanner){id, isTV ->
                    onItemClick(id, isTV)
                }
                mAdapter.setData(parent.children)
                adapter = mAdapter

            }
        }
    }
}

