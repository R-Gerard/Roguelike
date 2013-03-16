package com.callidusrobotics.object.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.callidusrobotics.object.actor.ActorFactionData.ActorFactionElement;
import com.callidusrobotics.util.XmlMarshaller;

public class ActorFactionDataTest {

  final XmlMarshaller marshaller = new XmlMarshaller(ActorFactionData.class);
  ActorFactionData data;

  @After
  public void after() throws Exception {
    final String xml = marshaller.marshal(data);
    final ActorFactionData data2 = (ActorFactionData) marshaller.unmarshal(xml);

    Assert.assertNotNull(data2);
    System.out.println(xml);
  }

  @Test
  public void makeDemoFactions() throws Exception {
    data = new ActorFactionData();
    data.factions = new ArrayList<ActorFactionData.ActorFactionElement>();

    final List<String> enemies = Arrays.asList(ActorUtil.DEMO_MONSTERS_FACTION);
    final List<String> neutrals = Arrays.asList(ActorUtil.DEMO_TOWNSFOLK_FACTION);
    final List<String> allies = Collections.emptyList();

    data.factions.add(new ActorFactionElement(ActorFactionData.PLAYER_FACTION, enemies, neutrals, allies));
    data.factions.add(new ActorFactionElement(ActorUtil.DEMO_TOWNSFOLK_FACTION, enemies, null, null));
  }

  @Test
  public void make7drlFactions() throws Exception {
    data = new ActorFactionData();
    data.factions = new ArrayList<ActorFactionElement>();

    final List<String> enemies = Arrays.asList(ActorUtil._7DRL_MONSTERS_FACTION);
    final List<String> neutrals = Arrays.asList(ActorUtil._7DRL_TOWNSFOLK_FACTION, ActorUtil._7DRL_DEPUTIES_FACTION);
    final List<String> allies = Collections.emptyList();

    data.factions.add(new ActorFactionElement(ActorFactionData.PLAYER_FACTION, enemies, neutrals, allies));
  }
}
