package com.rtersou.dropandfly.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class DropAndFlyWidget extends AppWidgetProvider {

    int enAttente = 0;
    int accepte = 0;
    int enCours = 0;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("reservations")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                switch (document.get("statut").toString()){
                                    case "0" :
                                        enAttente++;
                                        break;
                                    case "1" :
                                        accepte++;
                                        break;
                                    case "2" :
                                        enCours++;
                                        break;
                                    case "3" :
                                        break;
                                    default :
                                        break;
                                }
                            }
                            // Retrouver tous les id
                            ComponentName thisWidget = new ComponentName(context,
                                    DropAndFlyWidget.class);
                            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                            for (int widgetId : allWidgetIds) {
                                // créer des données aléatoires

                                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                        R.layout.drop_and_fly_widget);

                                // Définir le texte
                                remoteViews.setTextViewText(R.id.appwidget_text1, String.valueOf(enAttente) + " Réservations en attente");
                                remoteViews.setTextViewText(R.id.appwidget_text2, String.valueOf(accepte)+ " Réservations acceptées");
                                remoteViews.setTextViewText(R.id.appwidget_text3, String.valueOf(enCours)+ " Réservations en cours");

                                // Enregistrer un onClickListener
                                Intent intent = new Intent(context, DropAndFlyWidget.class);

                                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                remoteViews.setOnClickPendingIntent(R.id.appwidget_text1, pendingIntent);
                                remoteViews.setOnClickPendingIntent(R.id.appwidget_text2, pendingIntent);
                                remoteViews.setOnClickPendingIntent(R.id.appwidget_text3, pendingIntent);
                                appWidgetManager.updateAppWidget(widgetId, remoteViews);
                            }

                        } else {
                            Log.w(Helper.DB_EVENT_GET, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}


