package z.xtreamiptv.playerv3.view.nstplayer;

import android.view.View;
import android.widget.MediaController.MediaPlayerControl;

public interface IMediaController {
    void hide();

    boolean isShowing();

    void setAnchorView(View view);

    void setEnabled(boolean z);

    void setMediaPlayer(MediaPlayerControl mediaPlayerControl);

    void show();

    void show(int i);

    void showOnce(View view);
}
