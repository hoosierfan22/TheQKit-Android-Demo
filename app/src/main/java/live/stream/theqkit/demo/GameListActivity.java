package live.stream.theqkit.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.List;
import live.stream.theq.theqkit.TheQKit;
import live.stream.theq.theqkit.data.sdk.ApiError;
import live.stream.theq.theqkit.data.sdk.GameResponse;
import live.stream.theq.theqkit.listener.GameResponseListener;

public class GameListActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener, GameResponseListener,
    GameRecyclerAdapter.GameClickedListener {

  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView recyclerView;
  private Button cashoutButton;

  private GameRecyclerAdapter gameRecyclerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game_list);

    swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    recyclerView = findViewById(R.id.recyclerView);
    cashoutButton = findViewById(R.id.cashoutButton);

    swipeRefreshLayout.setOnRefreshListener(this);

    cashoutButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        TheQKit.getInstance().launchCashoutDialog(GameListActivity.this);
      }
    });

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    gameRecyclerAdapter = new GameRecyclerAdapter(this, this);
    recyclerView.setAdapter(gameRecyclerAdapter);

    loadGames();
  }

  private void loadGames() {
    swipeRefreshLayout.setRefreshing(true);
    TheQKit.getInstance().fetchGames(this);
  }

  @Override public void onGameClicked(GameResponse game) {
    TheQKit.getInstance().launchGameActivity(this, game);
  }

  @Override public void onSuccess(@NonNull List<GameResponse> games) {
    swipeRefreshLayout.setRefreshing(false);
    gameRecyclerAdapter.setGames(games);
  }

  @Override public void onFailure(@NonNull ApiError apiError) {
    swipeRefreshLayout.setRefreshing(false);
  }

  @Override public void onRefresh() {
    loadGames();
  }
}
