package com.example.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/webService")
@Stateful
@RequestScoped
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class MyWebService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@GET
	@Path("/{message}")
	public String echoService(@PathParam("message") String message) {
		System.out.println("GET on message");
		return message;
	}

	// Returns a Player - based on their User.facebookID
	@GET
	@Path("/Player/{facebookID}")
	@Produces("application/json")
	public Player getPlayer(@PathParam("facebookID") long facebookID) {
		System.out.println("GET on specific Player for Facebook User ID: ["
				+ facebookID + "]");

		Player foundPlayer = getPlayerByFacebookID(em, facebookID);

		System.out.println("Returning Player: " + foundPlayer);
		return foundPlayer;
	}

	// Returns a Player's point total - based on their User.facebookID
	// If no player with that ID exists, we return 100 (the default for a new
	// player)
	@GET
	@Path("/PlayerPoints/{facebookID}")
	@Produces("application/json")
	public long getPlayerPoints(@PathParam("facebookID") long facebookID) {
		System.out
				.println("GET on specific Player's Points for Facebook User ID: ["
						+ facebookID + "]");

		Player foundPlayer = getPlayerByFacebookID(em, facebookID);
		if (null == foundPlayer) {
			return 100;
		} else {
			System.out.println("Found Player: " + foundPlayer);
			System.out.println("Returning his points of: "
					+ foundPlayer.getPoints());
			return foundPlayer.getPoints();
		}
	}

	// Returns a User - based on their facebookID
	@GET
	@Path("/User/{facebookID}")
	@Produces("application/json")
	public User getUser(@PathParam("facebookID") long facebookID) {
		System.out.println("GET on specific User for Facebook User ID: ["
				+ facebookID + "]");

		User foundUser = getUserByFacebookID(em, facebookID);

		System.out.println("Returning User: " + foundUser);
		return foundUser;
	}

	// Returns a a list of all Players
	@GET
	@Path("/AllPlayers")
	@Produces("text/xml")
	public Collection<Player> getAllPlayers() {
		System.out.println("GET on All Players");

		TypedQuery<Player> query = em.createQuery("from Player p ",
				Player.class);
		query.setParameter(1, 0l);
		List<Player> result = query.getResultList();
		System.out.println("Returning List: " + result);
		return result;
	}

	// Returns a list of all Users
	@GET
	@Path("/AllUsers")
	@Produces("text/xml")
	public Collection<User> getAllUsers() {
		System.out.println("GET on All Users");

		TypedQuery<User> query = em.createQuery("from User u", User.class);
		List<User> result = query.getResultList();
		System.out.println("Returning List: " + result);
		return result;
	}

	// Returns a User List containing all Friends of a given Player having
	// {facebookID}
	// Assumes there is already an existing Player (1 and only 1) with
	// {facebookID}
	@GET
	@Path("/PlayersFriends/{facebookID}")
	@Produces("application/json")
	public Collection<User> getPlayersFriends(
			@PathParam("facebookID") long facebookID) {
		System.out.println("GET on Player's friends for Facebook User ID: ["
				+ facebookID + "]");

		Player foundPlayer = null;
		foundPlayer = getPlayerByFacebookID(em, facebookID);

		if (null == foundPlayer) {
			System.out
					.println("Trying to get FriendsList of a new Player (or matched to multiple Players), this goes agains our assertion, and we return null.");
			return null;
		}

		TypedQuery<User> query = em.createQuery(
				"from User u where u.FacebookID in ?", User.class);
		query.setParameter(1, foundPlayer.getFriendList());
		List<User> result = query.getResultList();

		System.out.println("Returning Friend List, first one is: "
				+ result.get(0));
		return result;
	}

	// Creates a new User if that FacebookID does not already exist
	// Returns a User object, either newly created, or the one already in the
	// system matching facebookID.
	@POST
	@Path("/UserRequest/{facebookID}/{name}")
	@Consumes("application/json")
	public User userPOSTRequest(@PathParam("facebookID") long facebookID,
			@PathParam("name") String name) {

		System.out.println("POST on a UserRequest for facebookID: ["
				+ facebookID + "]");

		String imageURL = resolveURL(facebookID);
		User foundUser = null;
		foundUser = getUserByFacebookID(em, facebookID);

		if (null == foundUser) {
			// NEW USER
			System.out.println("New User request. Adding User to the system.");
			User user = new User(facebookID, name, imageURL);
			em.persist(user);
			foundUser = user;
		} else {
			// RETURNING USER
			System.out.println("Welcome back. You are a current user.");
		}

		System.out.println("Returning User: " + foundUser);
		return foundUser;
	}// userPOSTRequest

	// Creates a new Player if that FacebookID does not already exist
	// Returns a Player object, either newly created, or the one already in the
	// system matching facebookID.
	@POST
	@Path("/PlayerRequest/{facebookID}/{name}")
	@Consumes("application/json")
	public Player playerPOSTRequest(@PathParam("facebookID") long facebookID,
			@PathParam("name") String name, ArrayList<String> JsonInput) {

		// Set up the 3 arrays we need from the master JSONInput 2D array
		ArrayList<Long> friendIDsList = new ArrayList<Long>();
		ArrayList<String> friendNamesList = new ArrayList<String>();
		ArrayList<String> friendImageUrlList = new ArrayList<String>();
		int eachArrayLength = 0;
		long friendID;
		String findNewArray = "";
		do {
			eachArrayLength += 1;
			findNewArray = JsonInput.get(eachArrayLength);
		} while (!findNewArray.equalsIgnoreCase("newArray"));

		for (int i = 1; i < eachArrayLength; i++) {
			friendID = Long.decode(JsonInput.get(i));
			friendIDsList.add(friendID);
			friendImageUrlList.add(resolveURL(friendID));
		}
		for (int i = eachArrayLength + 1; i < JsonInput.size(); i++) {
			friendNamesList.add(JsonInput.get(i));
		}

		System.out.println("POST on a PlayerRequest for Player facebookID: ["
				+ facebookID + "]");

		String imageURL = resolveURL(facebookID);
		User foundUser = null;
		foundUser = getUserByFacebookID(em, facebookID);
		if (null == foundUser) {
			// Create the new User to add it as PlayerInfo on the new Player
			System.out
					.println("New User for PlayerInfo for Player request. Adding User to the system.");
			User user = new User(facebookID, name, imageURL);
			em.persist(user);
			foundUser = user;
		}

		Player foundPlayer = null;
		foundPlayer = getPlayerByFacebookID(em, facebookID);

		if (null == foundPlayer) {
			// NEW PLAYER
			System.out
					.println("New Player request. Adding Player to the system.");
			Player player = new Player(foundUser);
			em.persist(player);
			em.flush();
			foundPlayer = player;
			Player newPlayer = new Player();
			em.persist(newPlayer);
		} else {
			// RETURNING PLAYER
			System.out.println("Welcome back. You are a current player.");
		}

		// For either new, or returning users, we need to update their friends
		// list.
		UpdateFriendsListForPlayer(em, foundPlayer, friendIDsList,
				friendNamesList, friendImageUrlList);

		System.out.println("Returning Player: " + foundPlayer);
		return foundPlayer;
	}// playerPOSTRequest

	// Removes an existing Player if that FacebookID exists
	// Returns a String of "Player removed with FacbookID: ####"
	// Or String of "No removal - No player found with FacebookID: #####"
	@POST
	@Path("/PlayerRemovalRequest/{facebookID}/")
	@Consumes("application/json")
	public String playerRemovalRequest(@PathParam("facebookID") long facebookID) {
		System.out
				.println("POST on a Player Removal Request for Player facebookID: ["
						+ facebookID + "]");

		Player foundPlayer = null;
		foundPlayer = getPlayerByFacebookID(em, facebookID);

		if (null == foundPlayer) {
			return "No removal - No player found with FacebookID: "
					+ facebookID;
		} else {
			em.remove(foundPlayer);
			em.flush();
			return "Player removed with FacbookID: " + facebookID;
		}
	}// playerRemovalRequest

	// **************** HELPER FUNCTIONS **********************

	// Return a Player object, or null if no Player exists with
	// PlayerInfo.FacebookID. Returns null and logs an error if multiple Players
	// have that same ID
	public Player getPlayerByFacebookID(EntityManager em, long facebookID) {
		Player returnPlayer = null;
		try {

			TypedQuery<Player> query = em.createQuery(
					"from Player p where p.playerInfo.facebookID = ?",
					Player.class);
			query.setParameter(1, facebookID);
			List<Player> result = query.getResultList();
			if (result.isEmpty()) {
				return returnPlayer;
			} else {
				if (result.size() > 1) {
					System.out
							.println("ERROR: We have ["
									+ result.size()
									+ "] Players that have the FacebookID of ["
									+ facebookID
									+ "]. I'll return the first one entered into the system");
					return result.get(0);
				} else {
					// We returned 1 and only 1 User from the query
					returnPlayer = result.get(0);
					return returnPlayer;
				}
			}
		} catch (Exception e) {
			System.out
					.println("Encountered error in getPlayerByFacebookID function : "
							+ e);
			return returnPlayer;
		}
	}// getPlayerByFacebookID

	// Return a User object, or null if no User exists with FacebookID. Returns
	// null and logs an error if multiple User have that same ID
	public User getUserByFacebookID(EntityManager em, long facebookID) {
		User returnUser = null;
		try {

			TypedQuery<User> query = em.createQuery(
					"from User u where u.facebookID = ?", User.class);
			query.setParameter(1, facebookID);
			List<User> result = query.getResultList();
			if (result.isEmpty()) {
				return returnUser;
			} else {
				if (result.size() > 1) {
					System.out
							.println("ERROR: We have ["
									+ result.size()
									+ "] Users that have the FacebookID of ["
									+ facebookID
									+ "]. I'll return the first one entered into the system");
					return result.get(0);
				} else {
					// We returned 1 and only 1 User from the query
					returnUser = result.get(0);
					return returnUser;
				}
			}
		} catch (Exception e) {
			System.out
					.println("Encountered error in getUserByFacebookID function : "
							+ e);
			return returnUser;
		}
	}// getPlayerByFacebookID

	public void UpdateFriendsListForPlayer(EntityManager em, Player player,
			ArrayList<Long> friendIDs, ArrayList<String> friendNames,
			ArrayList<String> friendImageURLs) {
		// Since it's not that much data, and for simplicity sake we're just
		// going to clear
		// the FriendsList for the user (if they had one stored) and rebuild it.
		// This is needed since people add and remove friends on Facebook all
		// the time.
		player.setFriendList(null);
		em.persist(player);

		// Now we rebuild the friends list with our info that was originally
		// passed into the POST command
		User friendUser;
		long friendID;
		String friendName;
		String friendImageURL;
		ArrayList<Long> newFriendList = new ArrayList<Long>();

		for (int i = 0; i < friendIDs.size(); i++) {
			friendID = friendIDs.get(i);
			friendName = friendNames.get(i);
			friendImageURL = friendImageURLs.get(i);

			friendUser = getUserByFacebookID(em, friendID);
			if (null == friendUser) {
				// NEW USER - System has never seen this Facebook user before.
				// We must add the user before they can be added as a friend to
				// this player.
				System.out
						.println("New User needed to be added to the system. - "
								+ friendID
								+ ", "
								+ friendName
								+ ", "
								+ friendImageURL);
				User user = new User(friendID, friendName, friendImageURL);
				em.persist(user);
				friendUser = user;
			}

			// Now that we're sure we have this friend as a User in our system,
			// update the Players Friendlist.
			newFriendList.add(friendID);
		}

		player.setFriendList(newFriendList);
		em.persist(player);
	}

	public String resolveURL(long facebookID) {
		return "http://graph.facebook.com/" + facebookID + "/picture";
	}

}
