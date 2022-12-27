package com.devajaykr.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.text
        holder.timestampTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val layoutParams = holder.itemView.layoutParams as ConstraintLayout.LayoutParams
        if (message.isFromUser) {
            holder.itemView.setBackgroundResource(R.drawable.user_message_background)
            layoutParams.horizontalBias = 1f
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bot_message_background)
            layoutParams.horizontalBias = 0f
        }
        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val messageTextView: TextView = itemView.findViewById(R.id.message_text)
    val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text)
}
