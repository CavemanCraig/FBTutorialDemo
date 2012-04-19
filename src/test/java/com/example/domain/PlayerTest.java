package com.example.domain;

import java.util.ArrayList;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PlayerTest {
    @Inject private MyWebService myWebService;

    @Deployment public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(MyWebService.class)
                .addClasses(User.class)
                .addClasses(Player.class)
                .addClasses(Link.class)
                		.addAsResource("META-INF/persistence.xml")
                		.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testIsDeployed() {
        Assert.assertNotNull(myWebService);
    }

    @Test
    public void testPlayerInContainer(){
    	ArrayList<String> friendIDList = new ArrayList<String>();
		friendIDList.add("newArray");
		friendIDList.add("67890");
		friendIDList.add("newArray");
		friendIDList.add("One Friend");
		Player player = myWebService.playerPOSTRequest(999, "Ima Player", friendIDList);
		User user = myWebService.getUser(67890);
		
		//Asserts after POST
		Assert.assertTrue(player.getFriendList().size()==1);
		Assert.assertTrue(player.getPlayerInfo().getName().equals("Ima Player"));
		Assert.assertTrue(user.getImageURL().equals(myWebService.resolveURL(67890)));
		
		long points = myWebService.getPlayerPoints(999);
		
		//Assert after GET
		Assert.assertTrue(points==100);
		
		//Remove the Player and make sure it happened
		String result = myWebService.playerRemovalRequest(999);
		Assert.assertTrue(result.equals("Player removed with FacbookID: 999"));
    }
}