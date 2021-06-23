package fr.juniorreox.disquette.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juniorreox.disquette.R
import fr.juniorreox.disquette.modele.DateUtils
import fr.juniorreox.disquette.modele.messageModele
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User


class chatAdapter(

): RecyclerView.Adapter<chatAdapter.ViewHolder>() {
    val VIEW_TYPE_MY_MESSAGE = 1
    val VIEW_TYPE_OTHER_MESSAGE = 2

    private val messages: ArrayList<messageModele> = ArrayList()

    fun addMessage(message: messageModele){
        messages.add(message)
        notifyDataSetChanged()
    }

    fun clear(){
        messages.clear()
    }

    //Boite pour ranger tout les composants a controler
     open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         open fun bind(message:messageModele) {}
    }

    inner class MyMessageViewHolder (view: View) : ViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.content_message_s)
        private var timeText: TextView = view.findViewById(R.id.time_message_s)

        override fun bind(message: messageModele) {
            messageText.text = message.message
            timeText.text = message.time?.let { DateUtils.fromMillisToTimeString(it) }
        }
    }

    inner class OtherMessageViewHolder (view: View) : ViewHolder(view) {
        private var messageText: TextView = view.findViewById(R.id.content_message_r)
        //private var userText: TextView = view.txtOtherUser
        private var timeText: TextView = view.findViewById(R.id.time_message_r)

        override fun bind(message: messageModele) {
            messageText.text = message.message
            //userText.text = message.uId
            timeText.text = message.time?.let { DateUtils.fromMillisToTimeString(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(messages[position].uId == User.uid) {
            VIEW_TYPE_MY_MESSAGE
        }
        else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.sender_message, parent, false)
            )
        } else {
            OtherMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.receiver_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]

        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}
