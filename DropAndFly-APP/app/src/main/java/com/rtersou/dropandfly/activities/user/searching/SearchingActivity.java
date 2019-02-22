package com.rtersou.dropandfly.activities.user.searching;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.rtersou.dropandfly.Adapaters.ShopAdapter;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.user.reservation.ReservationActivity;
import com.rtersou.dropandfly.models.Shop;

import java.util.ArrayList;

public class SearchingActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView listView;
    private SearchView searchView;
    private ShopAdapter shopAdapter;
    private ArrayList<Shop> shopes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        initFileds();
        loadShop();
    }

    private void initFileds(){
        listView   = findViewById(R.id.search_listview);
        searchView = findViewById(R.id.search_searchview);
    }

    private void loadShop() {
        /*
        Shop shop1 = new Shop("Paris","France","75012","273","Faubourg Saint Antoine","ESGI",100,1);
        Shop shop2 = new Shop("Villejuif","France","94800","145","Rue de Chevilly","La casa",10,2);
        Shop shop3 = new Shop("Chevilly Larue","France","94580","38","Rue du Séminaire","2BSystem",20,3);
        Shop shop4 = new Shop("Gif-Sur-Yvette","France","91190","29","Allée des graviers","Parents",4,4);
        Shop shop5 = new Shop("New York","USA","3459823","23","2nd Street","Starbucks",1000,5);
        Shop shop6 = new Shop("Nulle Part","Je sais pas","Nope","0","Rue inconnue","Existe pas",1,6);

        shopes.add(shop1);
        shopes.add(shop2);
        shopes.add(shop3);
        shopes.add(shop4);
        shopes.add(shop5);
        shopes.add(shop6);

        shopAdapter = new ShopAdapter(SearchingActivity.this, shopes);
        listView.setAdapter(shopAdapter);

        searchView.setOnQueryTextListener(this);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("id : " + id + " pos : " +position);
                Intent ReservationActivity = new Intent(SearchingActivity.this, com.rtersou.dropandfly.activities.user.reservation.ReservationActivity.class);
                ReservationActivity.putExtra("shop",shopes.get((int)id));
                startActivity(ReservationActivity);
            }
        });
        */

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        shopAdapter.filter(text);

        return false;
    }
}
