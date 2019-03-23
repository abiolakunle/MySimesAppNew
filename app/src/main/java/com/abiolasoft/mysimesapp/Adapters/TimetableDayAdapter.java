package com.abiolasoft.mysimesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abiolasoft.mysimesapp.Activities.TimeTableActivity;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.LetterImageView;


public class TimetableDayAdapter extends RecyclerView.Adapter<TimetableDayAdapter.ViewHolder> {

    private String[] days;
    private Context context;
    public static final String DAY_KEY = "day";

    public TimetableDayAdapter(String[] days) {
        this.days = days;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_day_single_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.dayText.setText(days[position]);
        holder.dayLetter.setLetter(days[position].charAt(0));

        holder.dayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent periodIntent = new Intent(context, TimeTableActivity.class);
                periodIntent.putExtra(DAY_KEY, days[position]);
                context.startActivity(periodIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dayText;
        private LetterImageView dayLetter;
        private CardView dayCard;

        public ViewHolder(View itemView) {
            super(itemView);

            dayText = itemView.findViewById(R.id.day_text_view);
            dayLetter = itemView.findViewById(R.id.day_letter_imageView);
            dayCard = itemView.findViewById(R.id.days_card_view);
        }
    }
}
