package com.abiolasoft.mysimesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abiolasoft.mysimesapp.Activities.EBookViewerActivity;
import com.abiolasoft.mysimesapp.Models.EBook;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.LetterImageView;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ELibraryAdapter extends RecyclerView.Adapter<ELibraryAdapter.ViewHolder> {

    private List<EBook> ebooks;
    private Context context;
    public static String BOOK_ID = "bookId";


    public ELibraryAdapter(List<EBook> eBooks) {
        this.ebooks = eBooks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.e_library_single_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.libLetterImage.setLetter(ebooks.get(position).getBook_title().charAt(0));
        holder.eBookTitle.setText(ebooks.get(position).getBook_title());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        holder.eBookSize.setText(String.valueOf(decimalFormat.format(ebooks.get(position).getFile_size())) + "KB");
        holder.eBookDesc.setText(ebooks.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent bookViewerIntent = new Intent(context, EBookViewerActivity.class);
                bookViewerIntent.putExtra(BOOK_ID, ebooks.get(position).getBook_id());
                context.startActivity(bookViewerIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return ebooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eBookTitle, eBookSize, eBookDesc;
        private LetterImageView libLetterImage;

        public ViewHolder(View itemView) {
            super(itemView);

            eBookTitle = itemView.findViewById(R.id.e_book_title);
            eBookSize = itemView.findViewById(R.id.e_book_size);
            eBookDesc = itemView.findViewById(R.id.e_book_desc);
            libLetterImage = itemView.findViewById(R.id.lib_letter_imageView);
        }
    }
}
