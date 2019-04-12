package com.abiolasoft.mysimesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.LetterImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryMultiUploadAdapter extends RecyclerView.Adapter<LibraryMultiUploadAdapter.ViewHolder> {

    private static Map<Integer, ArrayList> eBookTagList = new HashMap<Integer, ArrayList>();
    Context context;
    private List<String> fileNames;
    private List<Double> progress;
    private ArrayList<MultiSelectModel> courses;
    private FragmentManager fragmentManager;
    private List<MultiSelectDialog> tagDialogs;


    public LibraryMultiUploadAdapter(FragmentManager fragmentManager, List<String> fileNames, List<Double> progress, ArrayList<MultiSelectModel> courses, List<MultiSelectDialog> tagDialogs) {
        this.fragmentManager = fragmentManager;
        this.fileNames = fileNames;
        this.progress = progress;
        this.courses = courses;
        this.tagDialogs = tagDialogs;
    }

    public static Map<Integer, ArrayList> getEBookTagList() {
        return eBookTagList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.upload_multi_library_single_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        //viewHolder.setIsRecyclable(false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.bookTitle.setText(fileNames.get(position));
        holder.letterImageView.setLetter(fileNames.get(position).charAt(0));
        holder.progressBar.setProgress(progress.get(position).intValue());
        holder.progTextView.setText(progress.get(position).intValue() + "%");

        /*final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, courses);
        //holder.eBookTagsLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        holder.eBookTagsLv.setAdapter(adapter);
        //SparseBooleanArray pos = holder.eBookTagsLv.getCheckedItemPositions();
        adapter.notifyDataSetChanged();*/
        tagDialogs.get(position)
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        eBookTagList.put(position, selectedNames);
                        holder.addBookTagTv.setText(selectedNames.size() + ((selectedNames.size() > 1) ? " tags added, tap to modify" : " tag added, tap to modify"));
                    }

                    @Override
                    public void onCancel() {
                        //Log.d(TAG,"Dialog cancelled");
                    }
                });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagDialogs.get(position).show(fragmentManager, "multiSelectDialog" + fileNames.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LetterImageView letterImageView;
        private TextView bookTitle, progTextView, addBookTagTv;
        private ProgressBar progressBar;
        private Spinner eBookTagsLv;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            letterImageView = itemView.findViewById(R.id.lib_multi_letter_image);
            bookTitle = itemView.findViewById(R.id.upload_lib_multi_filename);
            progressBar = itemView.findViewById(R.id.lib_multi_upload_prog);
            progTextView = itemView.findViewById(R.id.lib_multi_prog_tv);
            eBookTagsLv = itemView.findViewById(R.id.lib_multi_tags_spin);
            addBookTagTv = itemView.findViewById(R.id.lib_multi_add_tag_tv);
            cardView = itemView.findViewById(R.id.multi_upload_cv);
        }


    }
}
