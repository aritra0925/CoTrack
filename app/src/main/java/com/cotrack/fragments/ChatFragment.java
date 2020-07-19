package com.cotrack.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cotrack.R;
import com.cotrack.adaptors.MessageListAdapter;
import com.cotrack.models.Message;
import com.cotrack.models.User;
import com.cotrack.utils.APIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.cotrack.fragments.ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private List<Message> mMessageList;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private static ChatFragment instance = null;
    View view;

    public ChatFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    public static ChatFragment newInstance() {
        if(instance==null){
            instance = new ChatFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMessageList = getMessageList();
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        mMessageRecycler = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(view.getContext(), mMessageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);
        ImageButton sendMessage = (ImageButton) view.findViewById(R.id.button_chatbox_send);
        new DBConnect().execute("");
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Clicked on chat", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public List<Message> getMessageList(){
        List<Message> messageList = new ArrayList<>();
        User sender1 = new User();
        sender1.setNickname("Random");
        User sender2 = new User();
        sender2.setNickname("Halo");

        Message message1 = new Message();
        message1.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        message1.setMessage("Hello");
        message1.setSender(sender1);

        Message message2 = new Message();
        message2.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        message2.setMessage("Hi");
        message2.setSender(sender2);

        Message message3 = new Message();
        message3.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        message3.setMessage("Just checking");
        message3.setSender(sender1);

        Message message4 = new Message();
        message4.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        message4.setMessage("Still checking");
        message4.setSender(sender2);

        messageList.add(message1);
        messageList.add(message2);
        messageList.add(message3);
        messageList.add(message4);

        return messageList;
    }

    class DBConnect extends AsyncTask {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected String doInBackground(Object[] objects) {
            APIUtils.insertDocument("Test test text");
            return "";
        }

        protected void onPostExecute(String feed) {
            //Toast.makeText(view.getContext(), feed, Toast.LENGTH_LONG).show();
        }
    }
}