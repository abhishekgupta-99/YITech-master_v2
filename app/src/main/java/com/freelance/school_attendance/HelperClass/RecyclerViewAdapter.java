package com.freelance.school_attendance.HelperClass;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.freelance.school_attendance.R;

import java.util.ArrayList;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<Student_Item_Card> mStudentItemCardList;
    private ArrayList<Student_Item_Card> mStudentItemCardListFull;
    public boolean loginAs;
    Context context;
    public ArrayList<Student_Item_Card> absentStudents=new ArrayList<>();
    public ArrayList<Student_Item_Card> presentStudents=new ArrayList<>();
    public ArrayList<Student_Item_Card> withPermission = new ArrayList<>();

    public ArrayList<Student_Item_Card> getPresentStudents() {
        return presentStudents;
    }

    public ArrayList<Student_Item_Card> getWithPermission() {
        return withPermission;
    }




    //public TextView status;

    //Status status=new Status();
    @Override
    public void onClick(View v) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        public TextView student_name;
        public TextView roll_no;

        public TextView lectures_attended;
        public  TextView status;
        CheckBox checkBox;
        CircularProgressIndicator cp;
        RadioGroup rg;


        public ViewHolder(View itemView) {
            super(itemView);


            student_name = itemView.findViewById(R.id.student);
            roll_no = itemView.findViewById(R.id.roll_no);
          //  checkBox = itemView.findViewById(R.id.custom_checkboxx);
            cp = itemView.findViewById(R.id.circular_progress);
            lectures_attended = itemView.findViewById(R.id.lectures_attended);
            rg=itemView.findViewById(R.id.rg);
            //status =itemView.findViewById(R.id.status);

//            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                    if ( lastCheckedRadioGroup != null && lastCheckedRadioGroup.getCheckedRadioButtonId()!= radioGroup.getCheckedRadioButtonId() && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
//                        lastCheckedRadioGroup.clearCheck();
//
////                        Toast.makeText(context,
////                                "Radio button clicked " + radioGroup.getCheckedRadioButtonId(),
////                                Toast.LENGTH_SHORT).show();
//
//                    }
//                    lastCheckedRadioGroup = radioGroup;
//                }
//            });
        }
    }

    public RecyclerViewAdapter(ArrayList<Student_Item_Card> exampleList) {
        mStudentItemCardList = exampleList;
        mStudentItemCardListFull = new ArrayList<>(mStudentItemCardList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item_updated, parent, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        Student_Item_Card currentItem = mStudentItemCardList.get(position);
        holder.student_name.setText(currentItem.get_student_name());
        holder.roll_no.setText(currentItem.get_roll_no());
        holder.cp.setMaxProgress(100);
        holder.lectures_attended.setText("Lectures :" + currentItem.getPresent_lecs() + "/" + currentItem.getTotallecs());
        //holder.status.setText(status.setstatus(currentItem.getPresent_lecs(),currentItem.getTotallecs()));
        show_hide_att_percentage(holder, currentItem );


       // holder.cp.setCurrentProgress(Double.parseDouble(currentItem.getPercent().trim()));

//        if(Integer.parseInt(currentItem.getPercent())< 75) {
//            holder.cp.setTextColor(Color.RED);
//          //  holder.cp.setGradient(gradientType, Color.RED);
//            Log.d("Entered  here",currentItem.getPercent());
//        }
//        else
//
//        {
//            holder.cp.setTextColor(Color.YELLOW);
//            //holder.cp.setGradient(gradientType, Color.RED);
//            Log.d("Entered  here red",currentItem.getPercent());
//
//        }

//in some cases, it will prevent unwanted situations
        // BOTTOM CODE WORKING #########################################33

//        holder.checkBox.setOnCheckedChangeListener(null);
//
//        //if true, your custom_checkbox will be selected, else unselected
//        holder.checkBox.setChecked(mStudentItemCardList.get(position).isSelected());
//
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mStudentItemCardList.get(holder.getAdapterPosition()).setSelected(isChecked);
//                Integer pos = (Integer) holder.checkBox.getTag();
//                if (mStudentItemCardList.get(holder.getAdapterPosition()).isSelected()) {
//                    // mStudentItemCardList.get(pos).setSelected(true);
//                    absentStudents.add(mStudentItemCardList.get(holder.getAdapterPosition()));
//                    //  Toast.makeText(context, absentStudents.toString(), Toast.LENGTH_SHORT).show();
//                } else if (!mStudentItemCardList.get(holder.getAdapterPosition()).isSelected()) {
//                     // mStudentItemCardList.get(pos).setSelected(false);
//                    absentStudents.remove(mStudentItemCardList.get(holder.getAdapterPosition()));
//                }
//
//
//            }
//        });




        //UP CODE WORKING #########################################################


        //holder.rg.setOnCheckedChangeListener(null);
       // Log.d("NULLLLLL", mStudentItemCardList.get(position).getSelectedRadioId()+"");
     //   holder.rg.clearCheck();
        // holder.rg.getChildAt(mStudentItemCardList.get(position).getSelectedRadioId()).setSelected(true);
         holder.rg.check(mStudentItemCardList.get(position).getSelectedRadioId());

       // holder.rg.getChildAt(mStudentItemCardList.get(holder.getAdapterPosition()).getSelectedRadioId()).setSelected(true);

        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // mStudentItemCardList.get(holder.getAdapterPosition()).setSe(i);
               // mStudentItemCardList.get(position).setSelectedRadioId(i);
//
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);
                Log.d("NULLLLL pos", idx+"");
                update_attendace_status_arrays(idx, mStudentItemCardList.get(holder.getAdapterPosition()));

                mStudentItemCardList.get(holder.getAdapterPosition()).setSelectedRadioId(i);



            }
        });



//        holder.checkBox.setTag(position);
//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Integer pos = (Integer) holder.checkBox.getTag();
//               // Toast.makeText(context, mStudentItemCardList.get(pos).get_student_name() + " clicked!", Toast.LENGTH_SHORT).show();
////                if (mStudentItemCardList.get(pos).isSelected()) {
////                    mStudentItemCardList.get(pos).setSelected(false);
////                } else {
////                    mStudentItemCardList.get(pos).setSelected(true);
////                }
//
//                if(holder.checkBox.isChecked()){
//                    mStudentItemCardList.get(pos).setSelected(true);
//                    absentStudents.add(mStudentItemCardList.get(pos));
//                    Toast.makeText(context, absentStudents.toString(), Toast.LENGTH_SHORT).show();
//                }
//                else if(!holder.checkBox.isChecked())
//                {
//                    mStudentItemCardList.get(pos).setSelected(false);
//                    absentStudents.remove(mStudentItemCardList.get(pos));
//                }
//            }
//        });

    }

    private void update_attendace_status_arrays(int idx, Student_Item_Card student_item_card) {
        if(student_item_card!=null)
        {
            switch (idx)
            {
                case 0:
                    presentStudents.add(student_item_card);
                    break;
                case 1:
                    absentStudents.add(student_item_card);
                    break;
                case 2:
                    withPermission.add(student_item_card);
                    break;
//                default:
//                    presentStudents.add(student_item_card);
//                    break;
            }
        }

    }


    public void show_hide_att_percentage(ViewHolder holder, Student_Item_Card currentItem) {
        if (loginAs) {

            if(currentItem.getPercent().trim().equals("#NUM!"))
            {
                holder.cp.setCurrentProgress(0);
            }
            else {
                holder.cp.setCurrentProgress(Double.parseDouble(currentItem.getPercent().trim()));
            }
            try {

               // Log.d("PERCENTTT", currentItem.getPercent()+"");
               // holder.checkBox.setVisibility(View.INVISIBLE);
                holder.rg.setVisibility(View.INVISIBLE);
            } catch (Exception e) {

            }

        } else {
            holder.cp.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return mStudentItemCardList.size();
    }

    public ArrayList<Student_Item_Card> absentstudents() {


        return absentStudents;
    }


    public void empty_Students_arraylist() {
        absentStudents.clear();
        presentStudents.clear();
        withPermission.clear();
        Log.d("Abb", absentStudents.toString());
    }

    public void setLoginAs(boolean loginAs) {
        this.loginAs = loginAs;
    }


}

