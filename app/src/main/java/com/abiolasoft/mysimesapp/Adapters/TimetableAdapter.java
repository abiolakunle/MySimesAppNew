package com.abiolasoft.mysimesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abiolasoft.mysimesapp.Activities.UpdatePeriodActivity;
import com.abiolasoft.mysimesapp.Models.Course;
import com.abiolasoft.mysimesapp.Models.ImeClass;
import com.abiolasoft.mysimesapp.Models.TimeTablePeriod;
import com.abiolasoft.mysimesapp.R;
import com.abiolasoft.mysimesapp.Utils.DbPaths;
import com.abiolasoft.mysimesapp.Utils.ImeClassSharedPref;
import com.abiolasoft.mysimesapp.Utils.LetterImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {
    private List<TimeTablePeriod> timeTableList;
    private Context context;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<ImeClass> imeClass;


    public TimetableAdapter(List<ImeClass> imeClass, List<TimeTablePeriod> list) {
        timeTableList = list;
        this.imeClass = imeClass;
    }

    @NonNull
    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_single_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimetableAdapter.ViewHolder holder, final int position) {
        String startAmPm = timeTableList.get(position).getStartHour() > 12 ? "PM" : "AM";
        String endAmPm = timeTableList.get(position).getEndHour() > 12 ? "PM" : "AM";

        Course courses = timeTableList.get(position).getCourse();
        holder.timetableCourse.setText(courses.getCourseName());
        holder.timetableCode.setText(courses.getCourseCode());
        holder.timetableUnit.setText(String.valueOf(courses.getCourseUnit()));
        holder.timetableLecturer.setText(timeTableList.get(position).getLecturer());
        holder.timetableStart.setText(timeTableList.get(position).getStartHour() + " : " + timeTableList.get(position).getStartMin() + startAmPm);
        holder.timetableEnd.setText(timeTableList.get(position).getEndHour() + " : " + timeTableList.get(position).getEndMin() + endAmPm);
        holder.letterImageView.setLetter(timeTableList.get(position).getCourse().getCourseName().charAt(0));
        if (timeTableList.get(position).isPractical()) {
            holder.isPracticalText.setVisibility(View.VISIBLE);
        }


        holder.editTimetableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImeClassSharedPref imeClassSharedPref = new ImeClassSharedPref();
                imeClassSharedPref.setObj("IME_CLASS", imeClass.get(0));
                Intent editIntent = new Intent(context, UpdatePeriodActivity.class);
                editIntent.putExtra("COURSE_POSITION", position);
                context.startActivity(editIntent);
            }
        });

        holder.timetableDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeTableList.remove(timeTableList.get(position));
                imeClass.get(0).setTimeTable(timeTableList);

                firebaseFirestore.collection(DbPaths.Classes.toString()).document(String.valueOf(imeClass.get(0).getClassCode())).set(imeClass.get(0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        notifyForBtn();
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return timeTableList.size();
    }

    private void notifyForBtn() {
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LetterImageView letterImageView;
        TextView timetableCourse, timetableCode, timetableUnit, timetableStart, timetableEnd, timetableLecturer, isPracticalText;
        Button editTimetableBtn, timetableDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            letterImageView = itemView.findViewById(R.id.timetable_letter_imageView);
            timetableCourse = itemView.findViewById(R.id.timetable_course);
            timetableCode = itemView.findViewById(R.id.timetable_course_code);
            timetableUnit = itemView.findViewById(R.id.timetable_course_unit);
            timetableLecturer = itemView.findViewById(R.id.timetable_lecturer);
            timetableStart = itemView.findViewById(R.id.timetable_start_time);
            timetableEnd = itemView.findViewById(R.id.timetable_end_time);
            editTimetableBtn = itemView.findViewById(R.id.edit_timetable_btn);
            timetableDelete = itemView.findViewById(R.id.timetable_delete_btn);
            isPracticalText = itemView.findViewById(R.id.is_practical_text);

        }
    }
}