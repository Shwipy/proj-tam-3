package estgoh.andre.tam_proj.Stuff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import estgoh.andre.tam_proj.DataBase.Quiz
//import estgoh.andre.tam_proj.DataBase.Quiz.QuizRoom
import estgoh.andre.tam_proj.R

//https://www.geeksforgeeks.org/kotlin/android-recyclerview-in-kotlin/
class QuizAdapter(private val itemsList: List<Quiz>) : RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

    var onClick: OnClickListener? = null
    var onEditClick: OnEditClickListener? = null
    interface OnClickListener{ fun onClick(quizId: Long)}
    interface OnEditClickListener{fun onEditClick(quizId: Long, owned: Boolean)}


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val quiz = itemsList[position]


        // sets the text to the textview from our itemHolder class
        holder.tvTitle.text = quiz.title
        if (!quiz.owned){
            holder.editButton.text = "View"
        }

        holder.tvTitle.setOnClickListener { onClick?.onClick(quiz.id) }
        holder.editButton.setOnClickListener { onEditClick?.onEditClick(quiz.id, quiz.owned) }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return itemsList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = itemView.findViewById(R.id.item_name)
        var editButton: Button = itemView.findViewById(R.id.btn_edit_item)
    }
}