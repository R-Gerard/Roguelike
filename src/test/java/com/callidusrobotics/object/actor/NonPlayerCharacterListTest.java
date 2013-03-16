package com.callidusrobotics.object.actor;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.callidusrobotics.util.XmlMarshaller;

public class NonPlayerCharacterListTest {
  final XmlMarshaller marshaller = new XmlMarshaller(NonPlayerCharacterList.class);
  NonPlayerCharacterList data;

  @Before
  public void before() throws Exception {
    data = new NonPlayerCharacterList();
  }

  @After
  public void after() throws Exception {
    final String xml = marshaller.marshal(data);
    final NonPlayerCharacterList data2 = (NonPlayerCharacterList) marshaller.unmarshal(xml);

    Assert.assertNotNull(data2);
    System.out.println(xml);
  }

  @Test
  public void makeDemoList() throws Exception {
    data.identifier = "demo";
    data.npcs = Arrays.asList(
        ActorUtil.makeDemoTownsfolk(),
        ActorUtil.makeDemoMonster());
  }

  @Test
  public void make7drlTownsfolkList() throws Exception {
    data.identifier = "townsfolk";
    data.npcs = Arrays.asList(
        ActorUtil.make7drlDeputy(),
        ActorUtil.make7drlProfessorAtwood(),
        ActorUtil.make7drlReporter(),
        ActorUtil.make7drlSheriff(),
        ActorUtil.make7drlStudent());
  }

  @Test
  public void make7drlMonstersList() throws Exception {
    data.identifier = "monsters";
    data.npcs = Arrays.asList(
        ActorUtil.make7drlElderThing(),
        ActorUtil.make7drlLengSpider(),
        ActorUtil.make7drlMiGo(),
        ActorUtil.make7drlNightgaunt(),
        ActorUtil.make7drlRatThing(),
        ActorUtil.make7drlStarSpawn(),
        ActorUtil.make7drlWarlock(),
        ActorUtil.make7drlYithian());
  }
}
