package rohan.rao.whatsappclone.Chat;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;

import rohan.rao.whatsappclone.R;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private  ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> messageList){
        this.messageList =messageList;
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MessageViewHolder rcv = new MessageViewHolder(layoutView);

        return  rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        holder.mMessage.setText(messageList.get(position).getMessage());
        holder.mSender.setText(messageList.get(position).getSenderId());
        if(messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty())
            holder.mViewMedia.setVisibility(View.GONE);
        holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mediaUriList = messageList.get(holder.getAdapterPosition()).getMediaUrlList();
                for (int i = 0; i < mediaUriList.size(); i++){
                    File a = new File(mediaUriList.get(i));
                    System.out.println(isImageFile(mediaUriList.get(i)) +"     g          =========> "+ a+ " jjjjjjjjj");
                }


                new ImageViewer.Builder(view.getContext(), mediaUriList)
                        .setStartPosition(0)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return  messageList.size();
    }

     class MessageViewHolder  extends  RecyclerView.ViewHolder{
        TextView mMessage, mSender;
        Button mViewMedia;
         LinearLayout mLayout;
         MessageViewHolder(View view){
            super(view);
            mMessage = view.findViewById(R.id.message);
             mSender = view.findViewById(R.id.sender);
             mViewMedia = view.findViewById(R.id.viewMedia );
             mLayout = view.findViewById(R.id.layout2);
        }
    }
}