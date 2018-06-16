package z.xtreamiptv.playerv3.v2api.view.utility.epg.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.common.collect.Maps;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import z.xtreamiptv.playerv3.v2api.model.database.LiveStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.EPGData;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.domain.EPGChannel;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.domain.EPGEvent;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.misc.EPGDataImpl;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.misc.EPGDataListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EPGService {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static SimpleDateFormat nowFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private Context context;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetail;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    LiveStreamDBHandler liveStreamDBHandler;
    LiveStreamsDatabaseHandler liveStreamsDatabaseHandler;
    private SharedPreferences loginPreferencesSharedPref_epg_channel_update;

    public EPGService(Context context) {
        this.context = context;
    }

    public EPGData getData(EPGDataListener listener, int dayOffset, String catID) {
        this.loginPreferencesSharedPref_epg_channel_update = this.context.getSharedPreferences(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, 0);
        try {
            if (this.loginPreferencesSharedPref_epg_channel_update.getString(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, "").equals("all")) {
                return parseDataall_channels(catID);
            }
            return parseData(catID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private EPGData parseData(String catID) {
        Throwable ex;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.liveStreamsDatabaseHandler = new LiveStreamsDatabaseHandler(this.context);
        EPGChannel firstChannel = null;
        EPGChannel prevChannel = null;
        EPGChannel currentChannel = null;
        EPGEvent prevEvent = null;
        Map<EPGChannel, List<EPGEvent>> map = Maps.newLinkedHashMap();
        ArrayList<LiveStreamsDBModel> allChannels = new LiveStreamsDatabaseHandler(this.context).getAllLiveStreasWithCategoryId(catID);
        this.categoryWithPasword = new ArrayList();
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || allChannels == null) {
            this.liveListDetailAvailable = allChannels;
        } else {
            this.listPassword = getPasswordSetCategories();
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(allChannels, this.listPassword);
            }
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        }
        if (this.liveListDetailAvailable != null) {
            int k = 0;
            int i = 0;
            EPGChannel currentChannel2 = null;
            while (i < this.liveListDetailAvailable.size()) {
                try {
                    String channelName = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getName();
                    String channelID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
                    String streamIcon = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamIcon();
                    String streamID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamId();
                    String num = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getNum();
                    String epgChannelId = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
                    if (!channelID.equals("")) {
                        ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(channelID);
                        Long epgTempStop = null;
                        if (!(xmltvProgrammePojos == null || xmltvProgrammePojos.size() == 0)) {
                            currentChannel = new EPGChannel(streamIcon, channelName, k, streamID, num, epgChannelId);
                            k++;
                            if (firstChannel == null) {
                                firstChannel = currentChannel;
                            }
                            if (prevChannel != null) {
                                currentChannel.setPreviousChannel(prevChannel);
                                prevChannel.setNextChannel(currentChannel);
                            }
                            prevChannel = currentChannel;
                            List<EPGEvent> epgEvents = new ArrayList();
                            map.put(currentChannel, epgEvents);
                            for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                                long starttesting1;
                                long endtesting1;
                                EPGEvent ePGEvent;
                                String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                                String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                                String Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                                String Desc = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getDesc();
                                Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                                Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                                EPGEvent r20;
                                if (epgTempStop != null && epgStartDateToTimestamp.equals(epgTempStop)) {
                                    EPGEvent epgEvent = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        epgEvent.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(epgEvent);
                                    }
                                    prevEvent = epgEvent;
                                    currentChannel.addEvent(epgEvent);
                                    epgEvents.add(epgEvent);
                                } else if (epgTempStop != null) {
                                    try {
                                        r20 = new EPGEvent(currentChannel, epgTempStop.longValue(), epgStartDateToTimestamp.longValue(), this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                        if (prevEvent != null) {
                                            r20.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(r20);
                                        }
                                        prevEvent = r20;
                                        currentChannel.addEvent(r20);
                                        epgEvents.add(r20);
                                        r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                        if (prevEvent != null) {
                                            r20.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(r20);
                                        }
                                        prevEvent = r20;
                                        currentChannel.addEvent(r20);
                                        epgEvents.add(r20);
                                    } catch (Throwable th) {
                                        ex = th;
                                    }
                                } else {
                                    r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        r20.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(r20);
                                    }
                                    prevEvent = r20;
                                    currentChannel.addEvent(r20);
                                    epgEvents.add(r20);
                                }
                                epgTempStop = epgStopDateToTimestamp;
                                long nowTime = System.currentTimeMillis();
                                if (j == xmltvProgrammePojos.size() - 1 && epgTempStop.longValue() < nowTime) {
                                    starttesting1 = epgTempStop.longValue();
                                    endtesting1 = starttesting1 + Long.parseLong("7200000");
                                    for (int l = 0; l < 50; l++) {
                                        ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                        if (prevEvent != null) {
                                            ePGEvent.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(ePGEvent);
                                        }
                                        prevEvent = ePGEvent;
                                        currentChannel.addEvent(ePGEvent);
                                        epgEvents.add(ePGEvent);
                                        starttesting1 = endtesting1;
                                        endtesting1 = starttesting1 + Long.parseLong("7200000");
                                    }
                                }
                                if (j == 0 && epgStartDateToTimestamp.longValue() > nowTime) {
                                    starttesting1 = nowTime - Long.parseLong("86400000");
                                    endtesting1 = epgStartDateToTimestamp.longValue();
                                    for (int m = 0; m < 50; m++) {
                                        ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                        if (prevEvent != null) {
                                            ePGEvent.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(ePGEvent);
                                        }
                                        prevEvent = ePGEvent;
                                        currentChannel.addEvent(ePGEvent);
                                        epgEvents.add(ePGEvent);
                                        starttesting1 = endtesting1;
                                        endtesting1 = starttesting1 + Long.parseLong("7200000");
                                    }
                                }
                            }
                            //continue;
                            i++;
                            currentChannel2 = currentChannel;
                        }
                    }
                    currentChannel = currentChannel2;
                    i++;
                    currentChannel2 = currentChannel;
                } catch (Throwable th2) {
                    ex = th2;
                    currentChannel = currentChannel2;
                }
            }
            currentChannel = currentChannel2;
        }
        if (currentChannel != null) {
            currentChannel.setNextChannel(firstChannel);
        }
        if (firstChannel != null) {
            firstChannel.setPreviousChannel(currentChannel);
        }
        return new EPGDataImpl(map);
        //throw new RuntimeException(ex.getMessage(), ex);
    }

    private EPGData parseDataall_channels(String catID) {
        Throwable ex;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        EPGChannel firstChannel = null;
        EPGChannel prevChannel = null;
        EPGChannel currentChannel = null;
        EPGEvent prevEvent = null;
        Map<EPGChannel, List<EPGEvent>> map = Maps.newLinkedHashMap();
        ArrayList<LiveStreamsDBModel> allChannels = new LiveStreamsDatabaseHandler(this.context).getAllLiveStreasWithCategoryId(catID);
        this.categoryWithPasword = new ArrayList();
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || allChannels == null) {
            this.liveListDetailAvailable = allChannels;
        } else {
            this.listPassword = getPasswordSetCategories();
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(allChannels, this.listPassword);
            }
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        }
        if (this.liveListDetailAvailable != null) {
            int i = 0;
            EPGChannel currentChannel2 = null;
            while (i < this.liveListDetailAvailable.size()) {
                try {
                    String channelName = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getName();
                    String channelID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
                    String streamIcon = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamIcon();
                    currentChannel = new EPGChannel(streamIcon, channelName, i, ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamId(), ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getNum(), ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId());
                    if (firstChannel == null) {
                        firstChannel = currentChannel;
                    }
                    if (prevChannel != null) {
                        try {
                            currentChannel.setPreviousChannel(prevChannel);
                            prevChannel.setNextChannel(currentChannel);
                        } catch (Throwable th) {
                            ex = th;
                        }
                    }
                    prevChannel = currentChannel;
                    List<EPGEvent> epgEvents = new ArrayList();
                    map.put(currentChannel, epgEvents);
                    int k;
                    if (channelID.equals("")) {
                        long starttesting = System.currentTimeMillis() - Long.parseLong("86400000");
                        long endtesting = starttesting + Long.parseLong("7200000");
                        for (k = 0; k < 50; k++) {
                            EPGEvent ePGEvent = new EPGEvent(currentChannel, starttesting, endtesting, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                            if (prevEvent != null) {
                                ePGEvent.setPreviousEvent(prevEvent);
                                prevEvent.setNextEvent(ePGEvent);
                            }
                            prevEvent = ePGEvent;
                            currentChannel.addEvent(ePGEvent);
                            epgEvents.add(ePGEvent);
                            starttesting = endtesting;
                            endtesting = starttesting + Long.parseLong("7200000");
                        }
                    } else {
                        ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(channelID);
                        Long epgTempStop = null;
                        if (xmltvProgrammePojos == null || xmltvProgrammePojos.size() == 0) {
                            long starttesting1 = System.currentTimeMillis() - Long.parseLong("86400000");
                            long endtesting1 = starttesting1 + Long.parseLong("7200000");
                            for (k = 0; k < 50; k++) {
                                EPGEvent ePGEvent2 = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                if (prevEvent != null) {
                                    ePGEvent2.setPreviousEvent(prevEvent);
                                    prevEvent.setNextEvent(ePGEvent2);
                                }
                                prevEvent = ePGEvent2;
                                currentChannel.addEvent(ePGEvent2);
                                epgEvents.add(ePGEvent2);
                                starttesting1 = endtesting1;
                                endtesting1 = starttesting1 + Long.parseLong("7200000");
                            }
                        } else {
                            for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                                String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                                String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                                String Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                                String Desc = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getDesc();
                                Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                                Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                                EPGEvent r20;
                                if (epgTempStop != null && epgStartDateToTimestamp.equals(epgTempStop)) {
                                    EPGEvent epgEvent = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        epgEvent.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(epgEvent);
                                    }
                                    prevEvent = epgEvent;
                                    currentChannel.addEvent(epgEvent);
                                    epgEvents.add(epgEvent);
                                } else if (epgTempStop != null) {
                                    r20 = new EPGEvent(currentChannel, epgTempStop.longValue(), epgStartDateToTimestamp.longValue(), this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                    if (prevEvent != null) {
                                        r20.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(r20);
                                    }
                                    prevEvent = r20;
                                    currentChannel.addEvent(r20);
                                    epgEvents.add(r20);
                                    r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        r20.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(r20);
                                    }
                                    prevEvent = r20;
                                    currentChannel.addEvent(r20);
                                    epgEvents.add(r20);
                                } else {
                                    r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        r20.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(r20);
                                    }
                                    prevEvent = r20;
                                    currentChannel.addEvent(r20);
                                    epgEvents.add(r20);
                                }
                                epgTempStop = epgStopDateToTimestamp;
                            }
                            continue;
                        }
                    }
                    i++;
                    currentChannel2 = currentChannel;
                } catch (Throwable th2) {
                    ex = th2;
                    currentChannel = currentChannel2;
                }
            }
            currentChannel = currentChannel2;
        }
        if (currentChannel != null) {
            currentChannel.setNextChannel(firstChannel);
        }
        if (firstChannel != null) {
            firstChannel.setPreviousChannel(currentChannel);
        }
        return new EPGDataImpl(map);
        //throw new RuntimeException(ex.getMessage(), ex);
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedCategories(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }
}
