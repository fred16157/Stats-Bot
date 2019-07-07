package com.test.StatsBot;

import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.common.Season;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.merakianalytics.orianna.types.dto.league.LeaguePositions;
import main.com.github.joecourtneyw.Auth;
import main.com.github.joecourtneyw.R6J;
import main.com.github.joecourtneyw.R6Player;
import main.com.github.joecourtneyw.declarations.Platform;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import com.merakianalytics.orianna.*;


import java.awt.*;

public class BotCommandMod {
    String[] params;
    MessageCreateEvent event;
    TextChannel ReplyChannel;
    public boolean HandleCommand(MessageCreateEvent event)
    {
        this.event = event;
        params = event.getMessageContent().split(" ");
        ReplyChannel = event.getMessage().getChannel();
        try
        {
            switch(params[0].substring(1))  {
                case "test":
                case "테스트":
                    Test();
                    break;
                case "lol":
                case "롤":
                    ReplyLolStats(params[1], params[2]);
                    break;
                case "r6":
                case "레식":
                    ReplyR6Stats(params[1], params[2]);
                    break;
                default:
                    return false;
            }
            return true;
        }
        catch(Exception Ex)
        {
            Ex.printStackTrace();
            return false;
        }
    }

    public void Test()
    {
        ReplyChannel.sendMessage("테스트 명령어입니다.");
    }
    public void ReplyLolStats(String region, String SummonerName)
    {
        EmbedBuilder eb = new EmbedBuilder();
        Orianna.setRiotAPIKey("RGAPI-2f434cb8-0bf1-43ca-8db7-4ca691f6f010");
        Orianna.setDefaultRegion(Region.valueOf(region));
        Summoner s = Orianna.summonerNamed(SummonerName).get();
        if(!s.exists()) {
            ReplyChannel.sendMessage("존재하지 않는 소환사의 이름입니다.");
            return;
        }
        eb.setTitle(s.getName() + "의 최근 20판 롤 전적입니다.");
        eb.addField("레벨", String.valueOf(s.getLevel()),true);
        eb.addField("지역", s.getRegion().toString(), true);
        eb.addField("마지막 접속", s.getUpdated().toString());
        eb.addInlineField("총 숙련도 점수", String.valueOf(s.getChampionMasteryScore().getScore()));
        MatchHistory mh = s.matchHistory().get();
        int kill = 0, death = 0, assist = 0, win = 0, loss = 0;
        for(Match m : mh.subList(0, 20))
        {
            Participant p = null;
            for(Participant gp : m.getParticipants())
            {
                if(gp.getSummoner().equals(s))
                {
                    p = gp;
                }
            }
            if(p == null)
            {
                ReplyChannel.sendMessage("사용자의 전적을 가져올 수 없었습니다.");
                return;
            }

           if(p.getStats() != null)
           {
               kill += p.getStats().getKills();
               death += p.getStats().getDeaths();
               assist += p.getStats().getAssists();
               if(p.getStats().isWinner())
               {
                   win++;
               }
               else {
                   loss++;
               }
           }
        }
        eb.addInlineField("킬", String.valueOf(kill));
        eb.addInlineField("데스", String.valueOf(death));
        eb.addInlineField("어시", String.valueOf(assist));
        eb.addInlineField("승리", String.valueOf(win));
        eb.addInlineField("패배", String.valueOf(loss));
        eb.setColor(new Color(0, 255, 0));
        ReplyChannel.sendMessage(eb);
    }
    public void ReplyR6Stats(String Name, String platform)
    {
        EmbedBuilder eb = new EmbedBuilder();
        R6J api = new R6J(new Auth("fred16157@naver.com", "TheSuspect2"));
        if(api.playerExists(Name, Platform.valueOf(platform)))
        {
            R6Player rp = api.getPlayerByName(Name, Platform.valueOf(platform));
            eb.setTitle(Name + "의 레식 전적입니다.");
            eb.addField("지역", rp.getRegion().getName());
            eb.addInlineField("총 킬수", String.valueOf(rp.getKills()));
            eb.addInlineField("총 데스수", String.valueOf(rp.getDeaths()));
            eb.addInlineField("현재 랭크", String.valueOf(rp.getRank()));
            eb.addInlineField("현재 MMR", String.valueOf(rp.getMmr()));
        }
        else
        {
            ReplyChannel.sendMessage("존재하지 않는 유저의 이름입니다.");
            return;
        }
    }
}
