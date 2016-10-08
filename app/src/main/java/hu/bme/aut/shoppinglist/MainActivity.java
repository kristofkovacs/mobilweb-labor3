package hu.bme.aut.shoppinglist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NewShoppingItemDialogFragment.INewShoppingItemDialogListener {
    private RecyclerView recyclerView;
    private ShoppingAdapter adapter;

    private void initRecyclerView() {
        recyclerView = (RecyclerView)
                findViewById(R.id.MainRecyclerView);
        adapter = new ShoppingAdapter();
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<ShoppingItem>>() {
            @Override
            protected List<ShoppingItem> doInBackground(Void... voids) {
                return ShoppingItem.listAll(ShoppingItem.class);
            }

            @Override
            protected void onPostExecute(List<ShoppingItem> shoppingItems) {
                super.onPostExecute(shoppingItems);
                adapter.update(shoppingItems);
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewShoppingItemDialogFragment().show(getSupportFragmentManager(),
                        NewShoppingItemDialogFragment.TAG);
            }
        });

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShoppingItemCreated(ShoppingItem newItem) {
        adapter.addItem(newItem);
    }
}
