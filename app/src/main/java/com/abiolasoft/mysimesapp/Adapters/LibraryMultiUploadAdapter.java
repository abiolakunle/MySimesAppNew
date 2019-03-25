package com.abiolasoft.mysimesapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
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

public class LibraryMultiUploadAdapter extends RecyclerView.Adapter<LibraryMultiUploadAdapter.ViewHolder> {

    private static Map<Integer, ArrayList> eBookTagList = new HashMap<Integer, ArrayList>();
    Context context;
    private List<String> fileNames;
    private List<Double> progress;
    private ArrayList<MultiSelectModel> courses;
    private FragmentManager fragmentManager;

    public LibraryMultiUploadAdapter(FragmentManager fragmentManager, List<String> fileNames, List<Double> progress, ArrayList<MultiSelectModel> courses) {
        this.fragmentManager = fragmentManager;
        this.fileNames = fileNames;
        this.progress = progress;
        this.courses = courses;
    }

    public static Map<Integer, ArrayList> getEBookTagList() {
        return eBookTagList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.upload_multi_library_single_item, parent, false);
        return new ViewHolder(view);
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

        //MultiSelectModel
        final MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Add tags to book") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(20) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(new ArrayList<Integer>()) //List of ids that you need to be selected
                .multiSelectList(courses) // the multi select model list with ids and name
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

        holder.addBookTagTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSelectDialog.show(fragmentManager, "multiSelectDialog" + position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LetterImageView letterImageView;
        private TextView bookTitle, progTextView, addBookTagTv;
        private ProgressBar progressBar;
        private Spinner eBookTagsLv;

        public ViewHolder(View itemView) {
            super(itemView);

            letterImageView = itemView.findViewById(R.id.lib_multi_letter_image);
            bookTitle = itemView.findViewById(R.id.upload_lib_multi_filename);
            progressBar = itemView.findViewById(R.id.lib_multi_upload_prog);
            progTextView = itemView.findViewById(R.id.lib_multi_prog_tv);
            eBookTagsLv = itemView.findViewById(R.id.lib_multi_tags_spin);
            addBookTagTv = itemView.findViewById(R.id.lib_multi_add_tag_tv);
        }
    }
}
