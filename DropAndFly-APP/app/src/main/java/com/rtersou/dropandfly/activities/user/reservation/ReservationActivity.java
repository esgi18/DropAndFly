package com.rtersou.dropandfly.activities.user.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;
import com.rtersou.dropandfly.models.Shop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name;
    TextView address1;
    TextView address2;
    EditText date_start;
    EditText time_start;
    EditText date_end;
    EditText time_end;
    EditText luggages;
    Button reservation;

    Shop shop;


    private FirebaseFirestore db;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        initFields();
        initListeners();
        setAddress();
        setDateTimeField();
    }

    private void initFields() {
        name        = findViewById(R.id.res_name);
        address1    = findViewById(R.id.res_address1);
        address2    = findViewById(R.id.res_address2);
        date_start  = findViewById(R.id.date_picker_depose);
        time_start  = findViewById(R.id.hour_picker_depose);
        date_end    = findViewById(R.id.date_picker_retrait);
        time_end    = findViewById(R.id.hour_picker_retrait);
        luggages    = findViewById(R.id.res_nb_luggages);
        reservation = findViewById(R.id.res_btn);
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date_start.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date_end.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                time_start.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }

        }, hour, minute, true);


        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                time_end.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }

        }, hour, minute, true);
    }


    private void initListeners() {
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reservation();
            }
        });

        date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTimePickerDialog.show();
            }
        });

        time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTimePickerDialog.show();
            }
        });

    }

    private void setAddress() {
        Intent intent = getIntent();
        shop = (Shop) intent.getSerializableExtra("shop");
        name.setText(shop.getName());
        address1.setText(shop.getAddress_number() + " " + shop.getAddress_street());
        address2.setText(shop.getAddress_city() + ", " + shop.getAddress_cp());
    }

    private void Reservation() {
        if(verifFields()){
            addReservation();
            ReservationActivity.this.finish();
        }
    }

    private Boolean verifFields() {
        String dateStart = date_start.getText().toString();
        String dateEnd = date_end.getText().toString();
        String timeStart = time_start.getText().toString();
        String timeEnd = time_end.getText().toString();

        if( !dateStart.equalsIgnoreCase("") && !dateEnd.equalsIgnoreCase("") && !timeStart.equalsIgnoreCase("") && !timeEnd.equalsIgnoreCase("") && !luggages.getText().toString().equalsIgnoreCase("")) {

            int nb_luggages = Integer.parseInt(luggages.getText().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                Date dDateStart = sdf.parse(dateStart + " " + timeStart);
                Date dDateEnd = sdf.parse(dateEnd + " " + timeEnd);

                if( new Date().after(dDateStart) ) {
                    showError("La date de dépose ne doit pas être passée");
                    return false;
                }

                if( dDateStart.after(dDateEnd) ) {
                    showError("La date de retrait doit être ultérieure à la date de dépose");
                    return false;
                }
            } catch (ParseException e) {
                showError("La date est incorrecte");
                return false;
            }

            //Trop de baggages
            if (nb_luggages > shop.getNb_luggage()) {
                showError("Seulement " + shop.getNb_luggage() + " places disponibles chez " + shop.getName());

                return false;
            }
            else {
                return true;
            }
        }
        else {
            showError("Veuillez renseigner tout les champs");
            return false;
        }
    }

    private Reservation createReservation() {

        Reservation reservation = new Reservation(
                date_start.getText().toString(),
                time_start.getText().toString(),
                date_end.getText().toString(),
                time_end.getText().toString(),
                Integer.parseInt(luggages.getText().toString()),
                0,
                10,
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                shop.getId()

        );

        return reservation;
    }



    private void addReservation() {
        db.collection("reservations")
                .add(createReservation())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Helper.DB_EVENT_ADD, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Helper.DB_EVENT_ADD, "Error adding document", e);
                    }
                });

    }

    private void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showOk(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ok");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        if(view == date_start) {
            fromDatePickerDialog.show();
        } else if(view == date_end) {
            toDatePickerDialog.show();
        } else if(view == time_start) {
            fromTimePickerDialog.show();
        } else if(view  == time_end) {
            toTimePickerDialog.show();
        }
    }
}
