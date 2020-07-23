package com.cotrack.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cotrack.BuildConfig;
import com.cotrack.R;
import com.cotrack.adaptors.MessageListAdapter;
import com.cotrack.models.Message;
import com.cotrack.models.User;
import com.cotrack.utils.WatsonUtils;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.MessageContext;
import com.ibm.watson.assistant.v2.model.MessageResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private EditText input;
    MessageContext context;
    private Handler handler = new Handler();
    public ArrayAdapter<String> msgList;
    public ListView msgView;
    Assistant assistant;

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
        if (instance == null) {
            instance = new ChatFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMessageList = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        mMessageRecycler = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(view.getContext(), mMessageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);

        input = (EditText) view.findViewById(R.id.edittext_chatbox);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(view.getContext(), "Clicked on chat", Toast.LENGTH_LONG).show();
                    String inputText = input.getText().toString();
                    input.setText("");
                    new WatsonTask().execute(inputText);
                    Message message = new Message();
                    User sender = new User();
                    sender.setNickname("Random");
                    message.setSender(sender);
                    message.setMessage(inputText);
                    message.setCreatedAt(Calendar.getInstance().getTimeInMillis());
                    mMessageList.add(message);
                    mMessageAdapter.swapItems(mMessageList);
                    mMessageRecycler.smoothScrollToPosition(mMessageList.size());
                }
                return true;
            }
        });
        ImageButton sendMessage = (ImageButton) view.findViewById(R.id.button_chatbox_send);
        new DBConnect().execute("");
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Clicked on chat", Toast.LENGTH_LONG).show();
                String inputText = input.getText().toString();
                input.setText("");
                new WatsonTask().execute(inputText);
                Message message = new Message();
                User sender = new User();
                sender.setNickname("Random");
                message.setSender(sender);
                message.setMessage(inputText);
                message.setCreatedAt(Calendar.getInstance().getTimeInMillis());
                mMessageList.add(message);
                mMessageAdapter.swapItems(mMessageList);
                mMessageRecycler.smoothScrollToPosition(mMessageList.size());
            }
        });
        return view;
    }

    public void displayMsg(MessageResponse msg) {
        final MessageResponse mssg = msg;
        handler.post(new Runnable() {

            @Override
            public void run() {
                //from the WCS API response
                //extract the text from output to display to the user
                String text = mssg.getOutput().toString();//.get(0);

                //now output the text to the UI to show the chat history
                msgList.add(text);
                msgView.setAdapter(msgList);
                msgView.smoothScrollToPosition(msgList.getCount() - 1);

                //set the context, so that the next time we call WCS we pass the accumulated context
                context = mssg.getContext();
            }
        });

    }

    @SuppressWarnings("deprecation")
    class DBConnect extends AsyncTask {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected String doInBackground(Object[] objects) {
            //APIUtils.insertDocument("Test test text");
            return "";
        }

        protected void onPostExecute(String feed) {
            //Toast.makeText(view.getContext(), feed, Toast.LENGTH_LONG).show();
        }
    }

    class WatsonTask extends AsyncTask<String, Void, MessageResponse> {

        private Exception exception;

        /**
         * @param strings
         * @deprecated
         */
        @Override
        protected MessageResponse doInBackground(String... strings) {
            MessageResponse messageResponse = WatsonUtils.startService(BuildConfig.WATSON_API_KEY, BuildConfig.WATSON_ASSISTANT_URL, BuildConfig.WATSON_WORKSPACE, strings[0]);
            System.out.println("Response: " + messageResponse.toString());
            return messageResponse;
        }

        @Override
        protected void onPostExecute(MessageResponse feed) {
            Message message = new Message();
            User sender = new User();
            String jsonString = feed.getOutput().toString();
            JSONObject jsonRootObject;
            String text = null;

            try {
                jsonRootObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonRootObject.getJSONArray("generic");
                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    /*JSONArray jsonArray1 = jsonObject.getJSONArray("options");
                    for(int j=i; j<=jsonArray1.length();j++){

                    }*/
                    text = jsonObject.optString("text").toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(text);
            sender.setNickname("Halo");
            message.setSender(sender);
            message.setMessage(text);
            message.setCreatedAt(Calendar.getInstance().getTimeInMillis());
            mMessageList.add(message);
            mMessageAdapter.swapItems(mMessageList);
            System.out.println("Message List: " + mMessageList);

        }
    }
}