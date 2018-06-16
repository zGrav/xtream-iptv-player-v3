package z.xtreamiptv.playerv3.v2api.view.exoplayer2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.SelectionOverride;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection.Factory;
import z.xtreamiptv.playerv3.R;
import java.util.Arrays;

final class TrackSelectionHelper implements OnClickListener, DialogInterface.OnClickListener {
    private static final Factory FIXED_FACTORY = new FixedTrackSelection.Factory();
    private static final Factory RANDOM_FACTORY = new RandomTrackSelection.Factory();
    private final Factory adaptiveTrackSelectionFactory;
    private CheckedTextView defaultView;
    private CheckedTextView disableView;
    private CheckedTextView enableRandomAdaptationView;
    private boolean isDisabled;
    private SelectionOverride override;
    private int rendererIndex;
    private final MappingTrackSelector selector;
    private TrackGroupArray trackGroups;
    private boolean[] trackGroupsAdaptive;
    private MappedTrackInfo trackInfo;
    private CheckedTextView[][] trackViews;

    public TrackSelectionHelper(MappingTrackSelector selector, Factory adaptiveTrackSelectionFactory) {
        this.selector = selector;
        this.adaptiveTrackSelectionFactory = adaptiveTrackSelectionFactory;
    }

    public void showSelectionDialog(Activity activity, CharSequence title, MappedTrackInfo trackInfo, int rendererIndex) {
        this.trackInfo = trackInfo;
        this.rendererIndex = rendererIndex;
        this.trackGroups = trackInfo.getTrackGroups(rendererIndex);
        this.trackGroupsAdaptive = new boolean[this.trackGroups.length];
        int i = 0;
        while (i < this.trackGroups.length) {
            boolean z;
            boolean[] zArr = this.trackGroupsAdaptive;
            if (this.adaptiveTrackSelectionFactory == null || trackInfo.getAdaptiveSupport(rendererIndex, i, false) == 0 || this.trackGroups.get(i).length <= 1) {
                z = false;
            } else {
                z = true;
            }
            zArr[i] = z;
            i++;
        }
        this.isDisabled = this.selector.getRendererDisabled(rendererIndex);
        this.override = this.selector.getSelectionOverride(rendererIndex, this.trackGroups);
        Builder builder = new Builder(activity);
        builder.setTitle(title).setView(buildView(builder.getContext())).setPositiveButton(17039370, this).setNegativeButton(17039360, null).create().show();
    }

    @SuppressLint({"InflateParams"})
    private View buildView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.track_selection_dialog, null);
        ViewGroup root = (ViewGroup) view.findViewById(R.id.root);
        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(new int[]{16843534});
        int selectableItemBackgroundResourceId = attributeArray.getResourceId(0, 0);
        attributeArray.recycle();
        this.disableView = (CheckedTextView) inflater.inflate(17367055, root, false);
        this.disableView.setBackgroundResource(selectableItemBackgroundResourceId);
        this.disableView.setText(R.string.selection_disabled);
        this.disableView.setFocusable(true);
        this.disableView.setOnClickListener(this);
        root.addView(this.disableView);
        this.defaultView = (CheckedTextView) inflater.inflate(17367055, root, false);
        this.defaultView.setBackgroundResource(selectableItemBackgroundResourceId);
        this.defaultView.setText(R.string.selection_default);
        this.defaultView.setFocusable(true);
        this.defaultView.setOnClickListener(this);
        root.addView(inflater.inflate(R.layout.list_divider, root, false));
        root.addView(this.defaultView);
        boolean haveAdaptiveTracks = false;
        this.trackViews = new CheckedTextView[this.trackGroups.length][];
        for (int groupIndex = 0; groupIndex < this.trackGroups.length; groupIndex++) {
            TrackGroup group = this.trackGroups.get(groupIndex);
            boolean groupIsAdaptive = this.trackGroupsAdaptive[groupIndex];
            haveAdaptiveTracks |= groupIsAdaptive;
            this.trackViews[groupIndex] = new CheckedTextView[group.length];
            for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                if (trackIndex == 0) {
                    root.addView(inflater.inflate(R.layout.list_divider, root, false));
                }
                CheckedTextView trackView = (CheckedTextView) inflater.inflate(groupIsAdaptive ? 17367056 : 17367055, root, false);
                trackView.setBackgroundResource(selectableItemBackgroundResourceId);
                trackView.setText(DemoUtil.buildTrackName(group.getFormat(trackIndex)));
                if (this.trackInfo.getTrackFormatSupport(this.rendererIndex, groupIndex, trackIndex) == 4) {
                    trackView.setFocusable(true);
                    trackView.setTag(Pair.create(Integer.valueOf(groupIndex), Integer.valueOf(trackIndex)));
                    trackView.setOnClickListener(this);
                } else {
                    trackView.setFocusable(false);
                    trackView.setEnabled(false);
                }
                this.trackViews[groupIndex][trackIndex] = trackView;
                root.addView(trackView);
            }
        }
        if (haveAdaptiveTracks) {
            this.enableRandomAdaptationView = (CheckedTextView) inflater.inflate(17367056, root, false);
            this.enableRandomAdaptationView.setBackgroundResource(selectableItemBackgroundResourceId);
            this.enableRandomAdaptationView.setText(R.string.enable_random_adaptation);
            this.enableRandomAdaptationView.setOnClickListener(this);
            root.addView(inflater.inflate(R.layout.list_divider, root, false));
            root.addView(this.enableRandomAdaptationView);
        }
        updateViews();
        return view;
    }

    private void updateViews() {
        boolean z;
        boolean z2 = true;
        this.disableView.setChecked(this.isDisabled);
        CheckedTextView checkedTextView = this.defaultView;
        if (this.isDisabled || this.override != null) {
            z = false;
        } else {
            z = true;
        }
        checkedTextView.setChecked(z);
        int i = 0;
        while (i < this.trackViews.length) {
            int j = 0;
            while (j < this.trackViews[i].length) {
                checkedTextView = this.trackViews[i][j];
                if (this.override != null && this.override.groupIndex == i && this.override.containsTrack(j)) {
                    z = true;
                } else {
                    z = false;
                }
                checkedTextView.setChecked(z);
                j++;
            }
            i++;
        }
        if (this.enableRandomAdaptationView != null) {
            boolean enableView = (this.isDisabled || this.override == null || this.override.length <= 1) ? false : true;
            this.enableRandomAdaptationView.setEnabled(enableView);
            this.enableRandomAdaptationView.setFocusable(enableView);
            if (enableView) {
                CheckedTextView checkedTextView2 = this.enableRandomAdaptationView;
                if (this.isDisabled || !(this.override.factory instanceof RandomTrackSelection.Factory)) {
                    z2 = false;
                }
                checkedTextView2.setChecked(z2);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        this.selector.setRendererDisabled(this.rendererIndex, this.isDisabled);
        if (this.override != null) {
            this.selector.setSelectionOverride(this.rendererIndex, this.trackGroups, this.override);
        } else {
            this.selector.clearSelectionOverrides(this.rendererIndex);
        }
    }

    public void onClick(View view) {
        if (view == this.disableView) {
            this.isDisabled = true;
            this.override = null;
        } else if (view == this.defaultView) {
            this.isDisabled = false;
            this.override = null;
        } else if (view == this.enableRandomAdaptationView) {
            setOverride(this.override.groupIndex, this.override.tracks, !this.enableRandomAdaptationView.isChecked());
        } else {
            this.isDisabled = false;
            Pair<Integer, Integer> tag = (Pair) view.getTag();
            int groupIndex = ((Integer) tag.first).intValue();
            int trackIndex = ((Integer) tag.second).intValue();
            if (this.trackGroupsAdaptive[groupIndex] && this.override != null && this.override.groupIndex == groupIndex) {
                boolean isEnabled = ((CheckedTextView) view).isChecked();
                int overrideLength = this.override.length;
                if (!isEnabled) {
                    setOverride(groupIndex, getTracksAdding(this.override, trackIndex), this.enableRandomAdaptationView.isChecked());
                } else if (overrideLength == 1) {
                    this.override = null;
                    this.isDisabled = true;
                } else {
                    setOverride(groupIndex, getTracksRemoving(this.override, trackIndex), this.enableRandomAdaptationView.isChecked());
                }
            } else {
                this.override = new SelectionOverride(FIXED_FACTORY, groupIndex, new int[]{trackIndex});
            }
        }
        updateViews();
    }

    private void setOverride(int group, int[] tracks, boolean enableRandomAdaptation) {
        Factory factory = tracks.length == 1 ? FIXED_FACTORY : enableRandomAdaptation ? RANDOM_FACTORY : this.adaptiveTrackSelectionFactory;
        this.override = new SelectionOverride(factory, group, tracks);
    }

    private static int[] getTracksAdding(SelectionOverride override, int addedTrack) {
        int[] tracks = override.tracks;
        tracks = Arrays.copyOf(tracks, tracks.length + 1);
        tracks[tracks.length - 1] = addedTrack;
        return tracks;
    }

    private static int[] getTracksRemoving(SelectionOverride override, int removedTrack) {
        int[] tracks = new int[(override.length - 1)];
        int trackCount = 0;
        for (int i = 0; i < tracks.length + 1; i++) {
            int track = override.tracks[i];
            if (track != removedTrack) {
                int trackCount2 = trackCount + 1;
                tracks[trackCount] = track;
                trackCount = trackCount2;
            }
        }
        return tracks;
    }
}
