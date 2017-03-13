package fiu.com.skillcourt.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.entities.Game;
import fiu.com.skillcourt.game.SkillCourtGame;

import static android.R.attr.id;

/**
 * Created by pedrocarrillo on 11/18/16.
 */

public class GamesPlayedRecyclerViewAdapter extends RecyclerView.Adapter<GamesPlayedRecyclerViewAdapter.GamesPlayedViewHolder> {

    public List<Game> gamesList;
    SimpleDateFormat simpleDateFormat;

    public GamesPlayedRecyclerViewAdapter(List<Game> gameList) {
        this.gamesList = gameList;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public GamesPlayedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_games_row, parent, false);
        return new GamesPlayedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GamesPlayedViewHolder holder, int position) {
        Game game = gamesList.get(position);
        holder.tvScore.setText(holder.tvScore.getContext().getString(R.string.score_placeholder, String.valueOf(game.getScore())));
        int second = game.getGameTimeTotal();
        long minutes = (second / 60);
        long seconds = second % 60;
        String time = String.format("%02d:%02d", minutes, seconds);
        holder.tvTime.setText(holder.tvTime.getContext().getString(R.string.game_time_placeholder, time));
        holder.tvAccuracy.setText(holder.tvAccuracy.getContext().getString(R.string.accuracy_placeholder, String.valueOf(game.getAccuracy())));
        String gameModeString = game.getGameMode().replace("_", " ");
        holder.tvGamemode.setText(holder.tvGamemode.getContext().getString(R.string.game_mode_placeholder, gameModeString));
        Date date = new Date(game.getDate());
        holder.tvDate.setText(simpleDateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    static class GamesPlayedViewHolder extends RecyclerView.ViewHolder {

        TextView tvScore;
        TextView tvTime;
        TextView tvDate;
        TextView tvAccuracy;
        TextView tvGamemode;

        public GamesPlayedViewHolder(View itemView) {
            super(itemView);
            tvScore = (TextView) itemView.findViewById(R.id.tvScore);
            tvTime = (TextView) itemView.findViewById(R.id.tv_game_time);
            tvAccuracy = (TextView) itemView.findViewById(R.id.tv_accuracy);
            tvGamemode = (TextView) itemView.findViewById(R.id.tv_game_mode);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }

    }

}
