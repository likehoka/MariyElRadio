package com.hoka.mariyelradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.hoka.mariyelradio.presenter.PresenterMainActivity;
import com.hoka.mariyelradio.utils.RadioPlayer;
import com.hoka.mariyelradio.view.IMainActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MvpAppCompatActivity implements IMainActivity {
    @InjectPresenter
    PresenterMainActivity mPresenterMainActivity;

    RadioPlayer mRadioPlayer;

    MediaPlayer mediaPlayer;
    Playertask mPlayertask;

    boolean prepared;
    boolean started = false;
    String stream = "http://radio.teleradio.info:8000/mari";
    @BindView(R.id.play_button)
    Button play_Button;
    @BindView(R.id.stop_button)
    Button stop_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        play_Button.setEnabled(true);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //mRadioPlayer = new RadioPlayer(mediaPlayer);
        //mRadioPlayer.execute(stream);

        mPlayertask = new Playertask();
        mPlayertask.cancel(true);
        mPlayertask.execute(stream);
    }

    @Override
    public void playRadio() {

        //mRadioPlayer.execute(stream);
        if (started) {
            mediaPlayer.start();
            started = false;
        }
        mPresenterMainActivity.clearStatus();
    }

    @Override
    public void stopRadio() {

        if (!started) {
            mediaPlayer.pause();
            started = true;
        }
        mPresenterMainActivity.clearStatus();
    }

    @Override
    public void clearStatus() {
    }

    @OnClick({R.id.play_button, R.id.stop_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_button:
                mPresenterMainActivity.playRadio();
                break;
            case R.id.stop_button:
                mPresenterMainActivity.stopRadio();
                break;
        }
    }


    class Playertask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                mediaPlayer.setDataSource(params[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mediaPlayer.start();
            //b_play.setEnabled(true);
            //b_play.setText("PLAY");
        }
    }
    /*

    @Override
    protected void onPause() {
        super.onPause();
        if (started) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (started) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prepared) {
            mediaPlayer.release();
        }
    } */
}
